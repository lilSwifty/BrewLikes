package com.iths.manisedighi.brewlikes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by moalenngren on 2017-11-22.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Log tag for this specific activity
    private static final String TAG = "MapActivity";

    //Permissions from the manifest
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //The zoom amount in the map view
    private final static float DEFAULT_ZOOM = 15f;

    //Boolean that checks if location permissions are granted or not
    private boolean locationPermissionsGranted;

    private static final int LOCATION_PERMISSION_CODE = 1234;
    private GoogleMap mMap;

    //The variable that will handle the map API
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
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
                            moveMapToLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
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
    private void moveMapToLocation(LatLng latLng, float zoom) {
        Log.d(TAG, "moveMapTpLocation: moving the map to current location. Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            //If you want to hide the "myLocationButton" up in the right corner
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // TODO - mMap.getUiSettings().WHATEVER_TRY_THESE_OUT!!!!!
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
                initializeMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
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

}
