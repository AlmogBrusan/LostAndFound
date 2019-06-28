package com.example.lostandfoundnew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map2 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final float DEFOULt_ZOOM = 15.0F;
    private static final String FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(33.0D, 73.0D), new LatLng(67.0D, 25.0D));
    private static final int locationrequest_code = 1234;
    ImageButton btnsubmit;
    double cordlat;
    double cordlng;
    Location current_location;
    ImageView gpsimg;
    double[] lat = new double[1];
    double[] longt = new double[1];
    private GeoDataClient mGeoDataClient;
    TextView mLocationMarkerText;
    private boolean mLocation_permissiongrannted = false;
    GoogleMap mMap;
    private AutoCompleteTextView mSearchText;
    Marker marker;
    LatLng mcentrelatling;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    RelativeLayout relativeLayout;
    Snackbar snackbar;
    Vibrator vibe;

    private void geoLocate() {
        String str = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List list = new ArrayList();
        try {
            List list1 = geocoder.getFromLocationName(str, 1);
            list = list1;
        } catch (IOException exception) {}
        if (list.size() > 0) {
            Address address = (Address)list.get(0);
            hideSoftKey();
            movecamera(new LatLng(address.getLatitude(), address.getLongitude()), 15.0F, address.getAddressLine(0));
            mSearchText.setText("");
        }
    }

    private void get_device_location() {
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocation_permissiongrannted)
                mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener() {
                    public void onComplete(@NonNull Task param1Task) {
                        if (param1Task.isSuccessful())
                            try {
                               current_location = (Location)param1Task.getResult();
                                movecamera(new LatLng(current_location.getLatitude(),current_location.getLongitude()), 15.0F, "My Location");
                                return;
                            } catch (Exception exception) {
                                Toast.makeText(getApplicationContext(), "unable to get location", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        Toast.makeText(Map2.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            return;
        } catch (SecurityException securityException) {
            return;
        }
    }

    private void getpermission() {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "android.permission.ACCESS_FINE_LOCATION";
        arrayOfString[1] = "android.permission.ACCESS_COARSE_LOCATION";
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") == 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                mLocation_permissiongrannted = true;
                initmap();
                return;
            }
            ActivityCompat.requestPermissions(this, arrayOfString, 1234);
            return;
        }
        ActivityCompat.requestPermissions(this, arrayOfString, 1234);
    }

    private void hideSoftKey() {
        View view = getCurrentFocus();
        if (view != null)
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void inIt() {
        mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(mplaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
                if (param1Int == 3 || param1Int == 6 || param1KeyEvent.getAction() == 0 || param1KeyEvent.getAction() == 66) {
                    hideSoftKey();
                    geoLocate();
                }
                return false;
            }
        });
        gpsimg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { get_device_location(); }
        });
        hideSoftKey();
    }

    private void initmap() { ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
        public void onMapReady(GoogleMap param1GoogleMap) {
            Toast.makeText(Map2.this, "Chose a location", Toast.LENGTH_SHORT).show();
            mMap = param1GoogleMap;
            if (mLocation_permissiongrannted) {
                get_device_location();
                if (ActivityCompat.checkSelfPermission(Map2.this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(Map2.this, "android.permission.ACCESS_COARSE_LOCATION") != 0)
                    return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            inIt();
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                public void onCameraChange(CameraPosition param2CameraPosition) {
                    mcentrelatling = param2CameraPosition.target;
                    try {
                        Location location = new Location("");
                        location.setLatitude(mcentrelatling.latitude);
                        location.setLongitude(mcentrelatling.longitude);
                        geocod_address();
                        return;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return;
                    }
                }
            });
        }
    }); }

    private void locationpicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Submit you Location").setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Intent intent = new Intent();
                intent.putExtra("latitude", cordlat);
                intent.putExtra("latitude", cordlat);
                setResult(-1, intent);
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
                marker.remove();
            }
        }).setCancelable(false).create().setCanceledOnTouchOutside(false);
        builder.show().getWindow().setGravity(48);
    }

    private void markerdraw() { this.mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
        public void onMapLongClick(LatLng param1LatLng) {
            vibe.vibrate(50L);
            if (marker != null)
                marker.remove();
           marker =mMap.addMarker((new MarkerOptions()).position(new LatLng(param1LatLng.latitude, param1LatLng.longitude)).draggable(true).visible(true));
           lat[0] = param1LatLng.latitude;
           longt[0] = param1LatLng.longitude;
           locationpicker();
        }
    }); }

    private void movecamera(LatLng paramLatLng, float paramFloat, String paramString) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paramLatLng, paramFloat));
        if (!paramString.equals("My Location"))
            hideSoftKey();
        hideSoftKey();
    }

    public void geocod_address() {
        Geocoder geocoder = new Geocoder(this);
        List list = new ArrayList();
        try {
            List list1 = geocoder.getFromLocation(this.mcentrelatling.latitude, this.mcentrelatling.longitude, 1);
            list = list1;
        } catch (IOException ioexception) {}
        if (list.size() > 0) {
            Address address = (Address)list.get(0);
            String str = "" + address.getFeatureName() + "," + address.getSubLocality() + "," + address.getLocality() + "," + address.getCountryName();
            this.mLocationMarkerText.setText(str);
            this.cordlng = this.mcentrelatling.longitude;
            this.cordlat = this.mcentrelatling.latitude;
        }
    }

    public void onConnectionFailed(@NonNull ConnectionResult paramConnectionResult) { Toast.makeText(getApplicationContext(), paramConnectionResult.getErrorMessage(), 0).show(); }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_map2);
        this.mSearchText = findViewById(R.id.input_search);
        this.gpsimg = findViewById(R.id.gpsimgview);
        this.mGeoDataClient = Places.getGeoDataClient(this, null);
        this.relativeLayout = findViewById(R.id.mapslocREl);
        this.vibe = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        this.mLocationMarkerText = findViewById(R.id.locationMarkertext);
        this.btnsubmit = findViewById(R.id.btnsubmitlocation);
        getpermission();
        this.btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent();
                intent.putExtra("longitude",cordlng);
                intent.putExtra("latitude",cordlat);
                intent.putExtra("addresss", mLocationMarkerText.getText());
                setResult(-1, intent);
                finish();
            }
        });
    }

    public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfInt) {
        mLocation_permissiongrannted = false;
        switch (paramInt) {
            default:
                return;
            case 1234:
                break;
        }
        if (paramArrayOfInt.length > 0) {
            for (paramInt = 0; paramInt < paramArrayOfInt.length; paramInt++) {
                if (paramArrayOfInt[paramInt] != 0) {
                    mLocation_permissiongrannted = false;
                    return;
                }
            }
            mLocation_permissiongrannted = true;
            initmap();
            return;
        }
    }
}
