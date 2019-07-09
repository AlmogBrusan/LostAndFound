package com.example.lostandfoundnew;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ItemUpload extends AppCompatActivity {
    private static final String FINE_CAMERA = "android.permission.CAMERA";
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    static int REQUEST_IMAGE_CAPTURE = 10;
    private static final int RESULT_LOAD_IMAGE1 = 15;
    private static final String TAG = "TedPicker";
    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final int camera_request_code = 1234;
    static int requestcodepicklocation = 11;
    List Imageslist = new ArrayList();
    RelativeLayout btnpicklocation;
    Button btnsubmit;
    Context context;
    DatabaseReference databaseReference;
    TextView edtTextpicklc;
    EditText edtTittle;
    EditText edtdescription;
    EditText edtphonenum;
    FirebaseAuth firebaseAuth;
    int flag = 0;
    String formated_address;
    String image = "";
    ArrayList<Uri> image_uris = new ArrayList();
    String[] imageurlarray = new String[5];
    String key;
    String imageUri1;
    double latitude;
    double longitude;
    DatabaseReference lostref;
    private boolean mCamera_permissiongrannted = false;
    private ViewGroup mSelectedImagesContainer;
    StorageReference mStorageref;
    Calendar myCalendar2;
    FirebaseDatabase mydatabase;
    Spinner spnrctrg;
    int status;
    TextView txtdateupload;
    ImagePicker imagePicker;
    String uniqueID;
    ImageView uploadimage;
    String user_login_id;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_item_upload);
        btnsubmit = findViewById(R.id.btnupload);
        txtdateupload = findViewById(R.id.edtdateuplod);
        edtTextpicklc = findViewById(R.id.edtlocationpick);
        edtTittle = findViewById(R.id.edttitle);
        edtdescription = findViewById(R.id.edtdescription);
        edtphonenum = findViewById(R.id.edtPhone);
        spnrctrg = findViewById(R.id.spinr_category);
        uploadimage = findViewById(R.id.uploadimage);
        Bundle bundle = getIntent().getExtras();
        btnpicklocation = findViewById(R.id.relpickloction);
        context = this;
        databaseReference = FirebaseDatabase.getInstance().getReference("database");
        lostref = FirebaseDatabase.getInstance().getReference("lostitem");
        firebaseAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance();
        key = databaseReference.push().getKey();
        uniqueID = key.toString();
        user_login_id = firebaseAuth.getCurrentUser().getUid().toString().trim();
        mStorageref = FirebaseStorage.getInstance().getReference();

        uploadimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                checkpermission();
                refreshImagePicker();
                imagePicker.choosePicture(true);
                imageUri1= imagePicker.getImageFile().toURI().toString();
                Picasso.get().load(imageUri1).into(uploadimage);
            }
        });
        if (bundle != null)
            status = ((Integer)bundle.get("status")).intValue();
        btnpicklocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (isServiceOK() && islocationenable()) {
                    Intent intent = new Intent(getApplicationContext(), Map2.class);
                    startActivityForResult(intent, ItemUpload.requestcodepicklocation);
                }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (edtphonenum.getText().toString().equals("") || edtTittle.getText().toString().equals("") || edtdescription.getText().toString().equals("") || txtdateupload.getText().toString().equals("") || edtTextpicklc.getText().toString().equals("") || spnrctrg.getSelectedItem().toString().equals("Category")) {
                    if (edtphonenum.getText().toString().equals(""))
                        edtphonenum.setError("Field cannot be empty");
                    if (edtTittle.getText().toString().equals(""))
                        edtTittle.setError("Field cannot be empty");
                    if (edtdescription.getText().toString().equals(""))
                        edtdescription.setError("Field cannot be empty");
                    if (edtTextpicklc.getText().toString().equals(""))
                        edtTextpicklc.setError("Field cannot be empty");
                    if (txtdateupload.getText().toString().equals(""))
                        txtdateupload.setError("Field cannot be empty");
                    if (spnrctrg.getSelectedItem().toString().equals("Category"))
                        ((TextView)spnrctrg.getSelectedView()).setError("Error message");
                    return;
                }
                if (status == 0) {
                    upload_information();
                    Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
                    intent1.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                    return;
                }
                lostitemupload();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        myCalendar2 = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker param1DatePicker, int param1Int1, int param1Int2, int param1Int3) {
                myCalendar2.set(1, param1Int1);
                myCalendar2.set(2, param1Int2);
                myCalendar2.set(5, param1Int3);
                updateLabel();
            }
        };
        txtdateupload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { (new DatePickerDialog(ItemUpload.this, date, myCalendar2.get(1), myCalendar2.get(2), myCalendar2.get(5))).show(); }
        });
    }

    private void refreshImagePicker() {

        imagePicker = new ImagePicker(this,null,  imageUri -> {
                    Picasso.get().load(imageUri).into(uploadimage);
                    imageUri1=imageUri.toString();
                  int  a=0;

                });


    }


    private void checkpermission() {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "android.permission.CAMERA";
        arrayOfString[1] = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") == 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
               mCamera_permissiongrannted = true;
                    return;
            }
            ActivityCompat.requestPermissions(this, arrayOfString, 1234);
            return;
        }
        ActivityCompat.requestPermissions(this, arrayOfString, 1234);
    }

    public static Bitmap decodeBase64(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    private void getImages(Config paramConfig) {
        ImagePickerActivity.setConfig(paramConfig);
        Intent intent = new Intent(this, ImagePickerActivity.class);
        if (this.image_uris != null)
            intent.putParcelableArrayListExtra("image_uris",image_uris);
        startActivityForResult(intent, 13);
    }

    private void imagepicker_Init() {
        Config config = new Config();
        config.setCameraHeight(R.dimen.app_camera_height);
        config.setToolbarTitleRes(R.string.custom_title);
        config.setSelectionMin(1);
        config.setSelectionLimit(4);
        config.setSelectedBottomHeight(R.dimen.bottom_height);
        config.setFlashOn(true);
        getImages(config);
    }

    private boolean islocationenable() {
        LocationManager locationManager= (LocationManager)this.context.getSystemService(LOCATION_SERVICE);
        boolean bool1 = false;
        boolean bool2 = false;
        try {
            boolean bool = locationManager.isProviderEnabled("gps");
            bool1 = bool;
        } catch (Exception exception) {}
        try {
            boolean bool = locationManager.isProviderEnabled("network");
            bool2 = bool;
        } catch (Exception exception) {}
        if (!bool1 && !bool2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Gps network not enabled").setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                    Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                    context.startActivity(intent);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) { param1DialogInterface.dismiss(); }
            });
            builder.show();
            return false;
        }
        return true;
    }

    private void lostitemupload() {
        String str1 =edtTittle.getText().toString().trim();
        String str2 =edtdescription.getText().toString().trim();
        String str3 =edtphonenum.getText().toString().trim();
        String str4 =spnrctrg.getSelectedItem().toString().trim();
        String str5 =uniqueID.trim();
        String str6 =txtdateupload.getText().toString().trim();
        String str7 =edtTextpicklc.getText().toString().trim();
        double d1 =latitude;
        double d2 =longitude;
        String str8 = user_login_id.trim();
        ItemModel item_Model = new ItemModel(str1, str7, str4, imageUri1, d2, d1, str2, 1, str5, str6, str3, str8);
       lostref.child(key).setValue(item_Model).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> param1Task) {
                if (param1Task.isSuccessful())
                    Toast.makeText(getApplicationContext(), "information saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMedia() {
        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1)
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        int i = (int)TypedValue.applyDimension(1, 100.0F, getResources().getDisplayMetrics());
        int j = (int)TypedValue.applyDimension(1, 100.0F, getResources().getDisplayMetrics());
        (new AsynctaskDecode()).execute(new ArrayList[] { image_uris });
        for (Uri uri : image_uris) {
            View view = LayoutInflater.from(this).inflate(R.layout.imagepickercard, null);
            ImageView imageView = (ImageView)view.findViewById(R.id.media_image);
            Glide.with(this).load(uri.toString()).fitCenter().into(imageView);
            mSelectedImagesContainer.addView(view);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(i, j));
        }
    }

    private void updateLabel() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        txtdateupload.setText(simpleDateFormat.format(myCalendar2.getTime()));
    }

    private void upload_information() {
        String str1 = edtTittle.getText().toString().trim();
        String str2 = edtdescription.getText().toString().trim();
        String str3 = edtphonenum.getText().toString().trim();
        String str4 = spnrctrg.getSelectedItem().toString().trim();
        String str5 = uniqueID.trim();
        String str6 = txtdateupload.getText().toString().trim();
        String str7 = edtTextpicklc.getText().toString().trim();
        double d1 = latitude;
        double d2 = longitude;
        String str8 = user_login_id.trim();
        ItemModel item_Model = new ItemModel(str1, str7, str4, imageUri1, d2, d1, str2, 0, str5, str6, str3, str8);
        databaseReference.child(key).setValue(item_Model).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> param1Task) {
                if (param1Task.isSuccessful())
                    Toast.makeText(getApplicationContext(), "information saved", Toast.LENGTH_SHORT).show();
            }
        });
    }



        public Bitmap getResizedBitmap(Bitmap paramBitmap, int paramInt) {
            int i = paramBitmap.getWidth();
            int j = paramBitmap.getHeight();
            float f = i / j;
            if (f > 1.0F) {
                j = (int)(paramInt / f);
                i = paramInt;
                paramInt = j;
                return Bitmap.createScaledBitmap(paramBitmap, i, paramInt, true);
            }
            i = (int)(paramInt * f);
            return Bitmap.createScaledBitmap(paramBitmap, i, paramInt, true);
        }

        public boolean isServiceOK() {
            int i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            if (i == 0)
                return true;
            if (GoogleApiAvailability.getInstance().isUserResolvableError(i)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, i, 9001).show();
                return false;
            }
            Toast.makeText(this, "You cant make map request", Toast.LENGTH_SHORT).show();
            return false;
        }

        protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1,paramInt2,paramIntent);
            if (paramInt1 == requestcodepicklocation && paramInt2 == -1) {
                longitude = paramIntent.getDoubleExtra("longitude", 0.0D);
                latitude = paramIntent.getDoubleExtra("latitude", 0.0D);
                formated_address = paramIntent.getStringExtra("addresss");
                edtTextpicklc.setText(formated_address);
                return;
            }

            imagePicker.handleActivityResult(paramInt1,paramInt2, paramIntent);

            return;
        }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

        private class AsynctaskDecode extends AsyncTask<ArrayList, Void, String[]> {
            private AsynctaskDecode() {}

            protected String[] doInBackground(ArrayList[] param1ArrayOfArrayList) {
                for (Uri uri : image_uris) {
                    ImageView imageView = LayoutInflater.from(getBaseContext()).inflate(R.layout.imagepickercard, null).findViewById(R.id.media_image);
                    Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(new File(uri.toString())).getPath());
                    bitmap = getResizedBitmap(bitmap, 500);
                    imageurlarray[flag] = ItemUpload.encodeTobase64(bitmap);
                    ItemUpload item_Upload = ItemUpload.this;
                    item_Upload.flag++;
                }
                flag = 0;
                return imageurlarray;
            }

            protected void onPostExecute(String[] param1ArrayOfString) { super.onPostExecute(param1ArrayOfString); }

            protected void onPreExecute() { super.onPreExecute(); }
        }
    }
