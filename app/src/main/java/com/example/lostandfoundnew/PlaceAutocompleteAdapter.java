package com.example.lostandfoundnew;

import android.content.Context;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaceAutocompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(1);

    private static final String TAG = "PlaceAutocompleteAd";

    private LatLngBounds mBounds;

    private GeoDataClient mGeoDataClient;

    private AutocompleteFilter mPlaceFilter;

    private ArrayList<AutocompletePrediction> mResultList;

    public PlaceAutocompleteAdapter(Context paramContext, GeoDataClient paramGeoDataClient, LatLngBounds paramLatLngBounds, AutocompleteFilter paramAutocompleteFilter) {
        super(paramContext, R.layout.place_autocomplete_fragment, R.id.place_autocomplete_search_input);
        mGeoDataClient = paramGeoDataClient;
        mBounds = paramLatLngBounds;
        mPlaceFilter = paramAutocompleteFilter;
    }

    private  ArrayList<AutocompletePrediction> getAutocomplete(CharSequence paramCharSequence) {
        Log.i("PlaceAutocompleteAd", "Starting autocomplete query for: " + paramCharSequence);
        Task task = mGeoDataClient.getAutocompletePredictions(paramCharSequence.toString(), mBounds, mPlaceFilter);
        try {
            Tasks.await(task, 60L, TimeUnit.SECONDS);
            try {
                AutocompletePredictionBufferResponse autocompletePredictionBufferResponse = (AutocompletePredictionBufferResponse)task.getResult();
                Log.i("PlaceAutocompleteAd", "Query completed. Received " + autocompletePredictionBufferResponse.getCount() + " predictions.");
                return DataBufferUtils.freezeAndClose(autocompletePredictionBufferResponse);
            } catch (RuntimeExecutionException paramSequence) {
                Toast.makeText(getContext(), "Error contacting API: " + paramCharSequence.toString(), Toast.LENGTH_SHORT).show();
                Log.e("PlaceAutocompleteAd", "Error getting autocomplete prediction API call", paramSequence);
                return null;
            }

        } catch (ExecutionException e) {e.printStackTrace(); }
          catch (InterruptedException e) {e.printStackTrace(); }
         catch (TimeoutException e) {e.printStackTrace();}

        try {
            AutocompletePredictionBufferResponse autocompletePredictionBufferResponse = (AutocompletePredictionBufferResponse)task.getResult();
            Log.i("PlaceAutocompleteAd", "Query completed. Received " + autocompletePredictionBufferResponse.getCount() + " predictions.");
            return DataBufferUtils.freezeAndClose(autocompletePredictionBufferResponse);
        } catch (RuntimeExecutionException paramSequence) {
            Toast.makeText(getContext(), "Error contacting API: " + paramCharSequence.toString(), Toast.LENGTH_SHORT).show();
            Log.e("PlaceAutocompleteAd", "Error getting autocomplete prediction API call", paramSequence);
            return null;
        }
    }

    public int getCount() { return mResultList.size(); }

    public Filter getFilter() { return new Filter() {
        public CharSequence convertResultToString(Object param1Object) { return (param1Object instanceof AutocompletePrediction) ? ((AutocompletePrediction)param1Object).getFullText(null) : super.convertResultToString(param1Object); }

        protected FilterResults performFiltering(CharSequence  param1CharSequence) {
            FilterResults filterResults = new FilterResults();
            ArrayList arrayList = new ArrayList();
            if (param1CharSequence != null)
                arrayList = getAutocomplete(param1CharSequence);
            filterResults.values = arrayList;
            if (arrayList != null) {
                filterResults.count = arrayList.size();
                return filterResults;
            }
            filterResults.count = 0;
            return filterResults;
        }

        protected void publishResults(CharSequence param1CharSequence, FilterResults param1FilterResults) {
            if (param1FilterResults != null && param1FilterResults.count > 0) {
              //  PlaceAutocompleteAdapter.access$102(PlaceAutocompleteAdapter.this, (ArrayList)param1FilterResults.values);
                notifyDataSetChanged();
                return;
            }
            PlaceAutocompleteAdapter.this.notifyDataSetInvalidated();
        }
    }; }

    public AutocompletePrediction getItem(int paramInt) { return (AutocompletePrediction)this.mResultList.get(paramInt); }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        paramView = super.getView(paramInt, paramView, paramViewGroup);
        AutocompletePrediction autocompletePrediction = getItem(paramInt);
        TextView textView1 = paramView.findViewById( R.id.place_autocomplete_search_input);
       // TextView textView2 = paramView.findViewById(16908309);
        textView1.setText(autocompletePrediction.getPrimaryText(STYLE_BOLD));
     //   textView2.setText(autocompletePrediction.getSecondaryText(STYLE_BOLD));
        return paramView;
    }

    public void setBounds(LatLngBounds paramLatLngBounds) { mBounds = paramLatLngBounds; }
}
