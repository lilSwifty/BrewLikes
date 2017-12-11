package com.iths.manisedighi.brewlikes;

/**
 * Category (beer) class.
 *
 * @author Milja Virtanen
 * @since 2017-11-21
 */
public class Category {

    private long id;
    private String name;

    public Category(String name) {
        this.name = name;
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

    public Category() {
    }

    @Override
    public String toString() {
        return this.name;
    }
}
