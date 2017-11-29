package com.iths.manisedighi.brewlikes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moalenngren on 2017-11-22.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    //Log tag for this specific activity
    private static final String TAG = "MapActivity";

    //Permissions from the manifest
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //The zoom amount in the map view
    private static final float DEFAULT_ZOOM = 15f;
    //The coordinate bounds that covers the whole world
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    //Boolean that checks if location permissions are granted or not
    private boolean locationPermissionsGranted;

    private static final int LOCATION_PERMISSION_CODE = 1234;
    private GoogleMap mMap;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;

    //The variable that will handle the map API
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Widgets
    private AutoCompleteTextView searchText;
    private ImageView gpsIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchText = findViewById(R.id.searchFieldText);
        gpsIcon = (ImageView) findViewById(R.id.ic_gps);

        getLocationPermission();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Initializes the search and overrides the "enter button" to be a search button
     * Also calls the method geoLocate() that tries to locate the text
     */
    private void initializeSearch(){
        Log.d(TAG, "initializeSearch: initializing the search function");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        searchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);

        searchText.setAdapter(mPlaceAutocompleteAdapter);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                Log.d(TAG, "initializeSearch: onEditorAction");
               if(actionId == EditorInfo.IME_ACTION_SEARCH
                       || actionId == EditorInfo.IME_ACTION_DONE
                       || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                       || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                   Log.d(TAG, "initializeSearch: if search button is pressed");
                   geoLocate();
               }
                return false;
            }


        });
        Log.d(TAG, "initializeSearch: search code failed");

        gpsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        hideKeyboard();
    }

    /**
     * Takes the string user entered in the search field and tries to find a location for it
     */
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch(IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: Found the location: " + address.toString());
            //Sends the address to the moveMapLocation to change the map view to that address
            moveMapToLocation(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    /**
     * Getter method for the location of the device
     */
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the current location of the device");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationPermissionsGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "getDeviceLocation: found device location");
                            Location currentLocation = (Location) task.getResult();
                            moveMapToLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "getDeviceLocation: couldn't find device location (null)");
                            Toast.makeText(MapActivity.this, "Couldn't get device location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    /**
     * Moves the "camera" of the map to the right location
     * @param latLng - The latitude and longitude for the current location
     * @param zoom - The possibility to zoom in and out of the map
     */
    private void moveMapToLocation(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveMapToLocation: moving the map to current location. Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //Passes the arguments to the drop pin method
        dropPin(latLng, zoom, title);
        hideKeyboard();
    }

    /**
     * Drops a pin on the location (everywhere but users actual position)
     * @param latLng - The latitude and longitude
     * @param zoom - How much it will zoom in on location
     * @param title - Title (text) of the pin
     */
    private void dropPin(LatLng latLng, float zoom, String title){
        Log.d(TAG, "dropPin: dropping pin");
        if(title != "My Location"){
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(markerOptions);
        }
        hideKeyboard();
    }

    /**
     * Prepares the map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (locationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onMapReady: if if permissions granted");
                return;
            }
            Log.d(TAG, "onMapReady: set my location enabled true");
            mMap.setMyLocationEnabled(true);
            //If you want to hide the "myLocationButton" up in the right corner
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // TODO - mMap.getUiSettings().WHATEVER_TRY_THESE_OUT!!!!!
            initializeSearch();
        }
    }

    /**
     * Initializes the map
     */
    private void initializeMap(){
        Log.d(TAG, "initializeMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    /**
     * Checks if app gets permission to use users location
     */
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationPermissionsGranted = true;
                Log.d(TAG, "getLocationPermission: setting location permissions to true");
                initializeMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
                Log.d(TAG, "getLocationPermission: setting location permissions");
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
        }

    }

    /**
     * Checks if app gets permission to use users location
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);  //Probably won't be needed
        Log.d(TAG, "onRequestPermissionsResult: called");
        locationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_CODE: {
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {
                          if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            locationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: Permission failed");
                            return;
                          }
                    }
                    Log.d(TAG, "onRequestPermissionResult: Permission granted");
                    locationPermissionsGranted = true;
                    initializeMap();
                }
            }
        }
    }

    /**
     * Hides the keyboard
     */
    private void hideKeyboard(){
        Log.d(TAG,"hideKeyboard: hides the keyboard");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // *********************** GOOGLE API AUTOCOMPLETE SUGGESTIONS **********************

    /**
     * Handles what happens when user clicks suggested location
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeID = item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeID);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };

    /**
     * Callback that gives us a place object with all the information about the location
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResultCallback: Place query did not complete successfully: " + places.getStatus().toString());
                places.release(); //To prevent memory leak
                return;
            }

            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setId(place.getId());
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResultCallBack: Getting all info from the location: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResultCallBack: Nullpointerexception: " + e.getMessage());
            }

            moveMapToLocation(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude)
                    , DEFAULT_ZOOM, mPlace.getName());
            places.release();
            hideKeyboard();
        }
    };
}


