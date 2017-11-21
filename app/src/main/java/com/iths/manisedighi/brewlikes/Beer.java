package com.iths.manisedighi.brewlikes;

/**
 * Created by Milja on 2017-11-21.
 */

public class Beer {

    private long id;
    private String name;
    private String category = null;
    private double price;
    private double taste;
    private double average;
    private String comment;
    private String location;
    private String photoPath; //file://.... <- path pointing to image

    public Beer() {}

    //Constructor utan GPS
    //TODO Add photopath (filnamn)
    public Beer(String name, double price, double taste, String comment) {
        this.name = name;
        this.price = price;
        this.taste = taste;
        this.average = ((price+taste)/2);
        this.comment = comment;
    }

    //Constructor med GPS
    public Beer(String name, int price, int taste, String comment, String location, String photoPath) {
        this.name = name;
        this.price = price;
        this.taste = taste;
        this.average = ((price+taste)/2);
        this.comment = comment;
        this.location = location;
        this.photoPath = photoPath;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public double getTaste() {
        return taste;
    }

    public void setTaste(int taste) {
        this.taste = taste;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(int average) {
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

    public void setPhotoLocation(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

