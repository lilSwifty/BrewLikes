package com.iths.manisedighi.brewlikes;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by moalenngren on 2017-11-29.
 */

public class PlaceInfo {

    private String name;
    private String address;
    private String id;
    private LatLng latLng;
    // private Uri websiteUri;
    // private String phoneNumber;
    // private float rating;
    // private String attributions;

    /**
     * Constructor with all the info about the location
     * @param name - Name of the place
     * @param address - Adress to the place
     * @param id - Id?
     * @param latLng - Lat and long for the place
     */
    public PlaceInfo(String name, String address, String id, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latLng = latLng;
    }

    /**
     * Empty constructor
     */
    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
