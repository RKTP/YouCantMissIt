package com.example.julian.youcantmissit;

import java.lang.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SearchLocationActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mClient;
    private Button addButton, cancelButton;
    private SupportMapFragment mMapFragment;
    private GoogleMap gMap;
    private float lat,lng;
    private EditText name;
    private AutoCompleteTextView locationName;
    private GooglePlacesAutoCompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_search_location);

        mClient = new GoogleApiClient.Builder(this).enableAutoManage(this,0,this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extra = new Bundle();
                extra.putString("name", String.valueOf(name.getText()));
                extra.putFloat("lat",lat);
                extra.putFloat("lng", lng);
                Intent exIntent = new Intent();
                exIntent.putExtras(extra);
                setResult(0, exIntent);
                SearchLocationActivity.this.finish();
            }
        });

        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                SearchLocationActivity.this.finish();
            }
        });


        name = (EditText)findViewById(R.id.input_item_name);
        locationName = (AutoCompleteTextView)findViewById(R.id.auto_location_search);
        mAdapter = new GooglePlacesAutoCompleteAdapter(this, mClient, null, null);
        locationName.setAdapter(mAdapter);
        locationName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                Log.e("TAG5", "Place status" + item.getPlaceId() + " || " + item.getDescription().toString());

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,placeId);
                Log.e("TAG6", "Place status" + placeResult.toString() + " || " + item.getDescription().toString());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        });
        mMapFragment.getMapAsync(this);
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("TAG", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            Log.e("TAG1","Place query retrieve. Error: " + places.getStatus().toString());
            final Place place = places.get(0);
            LatLng ll = place.getLatLng();
            lat = (float)ll.latitude;
            lng = (float)ll.longitude;
            moveCamera(lat,lng,locationName.getText().toString());
            Log.e("TAG2","Data set properly done" + places.getStatus().toString());
            places.release();
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        LatLng target = new LatLng(LocationService.myLocation.getLatitude(),LocationService.myLocation.getLongitude());
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 14));
    }

    public void moveCamera(float lat,float lng, String locationName) {
        this.lat = lat;
        this.lng = lng;
        LatLng target = new LatLng(this.lat, this.lng);
        gMap.addMarker(new MarkerOptions().position(target).title(locationName));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 14));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    class GooglePlacesAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {
        private final String TAG = "GooglePlacesAutoCompleteAdapter";
        private final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

        private ArrayList<AutocompletePrediction>  mResultList;
        private GoogleApiClient mGoogleApiClient;
        private LatLngBounds mBounds;
        private AutocompleteFilter mPlaceFilter;

        public GooglePlacesAutoCompleteAdapter(Context context, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter filter ) {
            super(context, android.R.layout.simple_list_item_1, android.R.id.text1);
            mGoogleApiClient = googleApiClient;
            mBounds = bounds;
            mPlaceFilter = filter;
        }

        public int getCount() {
            return mResultList.size();
        }

        public AutocompletePrediction getItem(int position) {
            return mResultList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            AutocompletePrediction item = getItem(position);

            TextView textView = (TextView) row.findViewById(android.R.id.text1);
            textView.setText(item.getPrimaryText(STYLE_BOLD));

            return row;
        }

        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if(constraint!=null) {
                        mResultList = getAutocomplete(constraint);
                        if(mResultList != null) {
                            results.values = mResultList;
                            results.count = mResultList.size();
                        }
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }

                @Override
                public CharSequence convertResultToString(Object resultValue) {
                    if (resultValue instanceof AutocompletePrediction) {
                        return ((AutocompletePrediction) resultValue).getFullText(null);
                    } else {
                        return super.convertResultToString(resultValue);
                    }
                }
            };
        }

        private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
            if (mGoogleApiClient.isConnected()) {
                PendingResult<AutocompletePredictionBuffer> results =
                        Places.GeoDataApi
                                .getAutocompletePredictions(mGoogleApiClient, String.valueOf(constraint),
                                        mBounds, mPlaceFilter);
                AutocompletePredictionBuffer autocompletePredictions = results
                        .await(60, TimeUnit.SECONDS);
                final Status status = autocompletePredictions.getStatus();
                if (!status.isSuccess()) {
                    Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                            Toast.LENGTH_SHORT).show();
                    autocompletePredictions.release();
                    return null;
                }
                return DataBufferUtils.freezeAndClose(autocompletePredictions);
            }
            return null;
        }
    }
}
