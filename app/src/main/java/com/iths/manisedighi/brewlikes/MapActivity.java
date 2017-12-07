package com.iths.manisedighi.brewlikes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
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
    //The coordinate bounds that covers the (most important parts of the) world
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private static final int PLACE_PICKER_REQUEST = 1;
    //Boolean that checks if location permissions are granted or not
    private boolean locationPermissionsGranted;
    private static final int LOCATION_PERMISSION_CODE = 1234;
    private GoogleMap mMap;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace = new PlaceInfo();
    //The variable that will handle the map API
    private FusedLocationProviderClient fusedLocationProviderClient;
    //Widgets
    private AutoCompleteTextView mSearchText;
    private ImageView gpsIcon;
    private DBHelper dbHelper;

    //ID from intent to make sure which activity is sending the intent
    private int ID;
    private  Long beerIDFromIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = findViewById(R.id.searchFieldText);
        gpsIcon = (ImageView) findViewById(R.id.ic_gps);
        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        beerIDFromIntent = intent.getLongExtra("beerId", 0);

        getLocationPermission();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Initializes the search and overrides the "enter button" to be a search button
     */
    private void initializeSearch(){
        Log.d(TAG, "initializeSearch: initializing the search function");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        gpsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        if(ID == 1) {
            placePicker();
        }
        hideSoftKeyboard(MapActivity.this);
    }

    public void placePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "GooglePlayServicesRepairableException: " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "GooglePlayServicesNotAvailableException: " + e.getMessage());
        }

    }



    //TODO - Surround All the way to here


    /**
     * Makes the place picker (nearby suggestions) pop up when "check in map" is launched
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    /**
     * Takes the string user entered in the search field and tries to find a location for it
     */
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();
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
            moveMapToLocation(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    /**
     * Getter method for the location of the device
     */
    private void getDeviceLocation() {
        //if(ID == 1) {
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
                               // moveMapToLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
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
   // }

    /**
     * Moves the "camera" of the map to the right location
     * @param latLng - The latitude and longitude for the current location
     * @param zoom - The possibility to zoom in and out of the map
     */
    private void moveMapToLocation(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveMapToLocation: moving the map to location. " + latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(ID == 1) {
            mMap.setInfoWindowAdapter(new CustomCheckinWindowAdapter(MapActivity.this));

            //When custom info window is clicked
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Log.i(TAG, "onInfoWindowCLick: clicked check in button");
                    Intent intent = new Intent();
                    intent.putExtra("location", mPlace.getName());
                    intent.putExtra("lat", mPlace.getLatLng().latitude);
                    intent.putExtra("lng", mPlace.getLatLng().longitude);
                    intent.putExtra("website", mPlace.getWebsiteUri());
                    intent.putExtra("phoneNumber", mPlace.getPhoneNumber());
                    intent.putExtra("address", mPlace.getAddress());
                    Log.i(TAG, "onInfoWindowCLick: sets location to " + mPlace.getName());
                    Log.i(TAG, "onInfoWindowCLick: sets latitude to " + mPlace.getLatLng().latitude);
                    Log.i(TAG, "onInfoWindowCLick: sets longitude to " + mPlace.getLatLng().longitude);
                    Log.i(TAG, "onInfoWindowCLick: sets address to " + mPlace.getAddress());
                    Log.i(TAG, "onInfoWindowCLick: sets website to " + mPlace.getWebsiteUri());
                    Log.i(TAG, "onInfoWindowCLick: sets phone number to " + mPlace.getPhoneNumber());
                    setResult(1, intent);
                    finish();
                }
            });
        }

        if(ID != 3) {
            dropPin(latLng, zoom, title, mPlace);
        }
    }

    /**
     * Drops a pin on the location (everywhere but users actual position)
     * @param latLng - The latitude and longitude
     * @param zoom - How much it will zoom in on location
     * @param title - Title (text) of the pin
     */
    private void dropPin(LatLng latLng, float zoom, String title, PlaceInfo placeInfo){
        Log.d(TAG, "dropPin: receiving placeinfo: " + placeInfo);

       if(ID == 1 || ID == 2){
          mMap.clear();  
       }

        Log.d(TAG, "dropPin: dropping pin");
        if(title != "My Location"){

            if(ID == 1) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.brewlikes_pin));
                Marker marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();

            } /*else{
                if(placeInfo != null){
                    try{
                        String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                                "Phone number: " + placeInfo.getPhoneNumber() + "\n" +
                                "Website: " + placeInfo.getWebsiteUri() + "\n";

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(placeInfo.getName())
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.brewlikes_pin));
                        Marker mMarker = mMap.addMarker(markerOptions);

                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

                        if(ID ==2){
                            mMarker.showInfoWindow();
                        }
                    }catch (NullPointerException e){
                        Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage());
                    }
                } else{
                    mMap.addMarker(new MarkerOptions().position(latLng));
                }
            } */
        }
    }

    private void dropPin(LatLng latLng, float zoom, String title, Beer beer){
        Log.d(TAG, "dropPin: receiving place info: " + beer.getLatLng());

        String snippet = "";
        if(!beer.getAddress() == null){
            snippet = snippet + beer.getAddress();
        }
        if(!beer.getWeb() == null){
            snippet = snippet + beer.getWeb();
        }
        if(!beer.getTel() == null){
            snippet = snippet + beer.getTel();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(beer.getLocation())
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.brewlikes_pin));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
        Marker marker = mMap.addMarker(markerOptions);
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
            if (ID == 1) {
                Log.d(TAG, "Checking intent ID: 1");
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
                //Hides the "myLocationButton" up in the right corner
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                // TODO - mMap.getUiSettings().WHATEVER_TRY_THESE_OUT!!!!!
                initializeSearch();

            } else if (ID == 2) {
                Log.d(TAG, "Checking intent ID: 2");
                Beer b = dbHelper.getBeerById(beerIDFromIntent);
                String locationName = b.getLocation();
                Log.d(TAG, "Got location: " + b.getLocation() + "and lat: " + b.getLat() + ", lng: " + b.getLng());
                LatLng latLng = new LatLng(b.getLat(), b.getLng());
                moveMapToLocation(latLng, DEFAULT_ZOOM, locationName);
                //search bar + icons + check in view set visibility view Gone!!!!


            } else if (ID == 3) { //From map view navigation button
                Log.d(TAG, "Checking intent ID: 3");
                List<Beer> beers = dbHelper.getAllBeers();

                for (Beer b : beers) {
                    if(b.getLat() != 0.0 && b.getLng() != 0.0) {
                        dropPin(new LatLng(b.getLat(), b.getLng()), DEFAULT_ZOOM, b.getLocation(), b);
                    }
                }
                    initializeSearch();
            }
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

    // *********************** GOOGLE API AUTOCOMPLETE SUGGESTIONS **********************

    /**
     * Handles what happens when user clicks suggested location
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard(MapActivity.this);

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
                //mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri().toString());
                mPlace.setId(place.getId());
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResultCallBack: Getting all info from the location: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResultCallBack: Nullpointerexception: " + e.getMessage());
            }
                moveMapToLocation(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude)
                        , DEFAULT_ZOOM, mPlace.getName());
                places.release();
        }
    };

    /**
     * Hides the keyboard to make more space on the screen
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        Log.d(TAG,"hideSoftKeyboard: hides the keyboard");
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


}


