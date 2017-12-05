package com.iths.manisedighi.brewlikes;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Milja on 2017-11-21.
 */

/**
 * Beer class.
 */
public class Beer {

    private long id;
    private String name = null;
    private String categoryName = null;
    private int categoryId = 0;
    private double price = 0;
    private double taste = 0;
    private double average = 0;
    private String comment = null;
    private String location = null;
    private String photoPath = null;
    private double lat = 0;
    private double lng = 0;
    private LatLng latLng;


    //Empty constructor
    public Beer() {}

    //Constructor utan GPS
    public Beer(String name, int categoryId, double price, double taste, String comment, String photoPath) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.taste = taste;
        this.average = ((price+taste)/2);
        this.comment = comment;
        this.photoPath = photoPath;
    }

    //Constructor med GPS
    public Beer(String name, int categoryId, double price, double taste, String comment, String photoPath, String location, double latitude, double longitude) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.taste = taste;
        this.average = ((price+taste)/2);
        this.comment = comment;
        this.photoPath = photoPath;
        this.location = location;
        this.lat = latitude;
        this.lng = longitude;

        /*
        //TEST FOR GETTING LATLNG VALUES
        String ll = latLng.toString();
        String[] llArray = ll.split(",");
        this.lat = Double.parseDouble(llArray[0]);
        this.lng = Double.parseDouble(llArray[1]);
        */
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTaste() {
        return taste;
    }

    public void setTaste(double taste) {
        this.taste = taste;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
