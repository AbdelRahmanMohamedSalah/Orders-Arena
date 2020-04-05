package com.amoharib.graduationproject.models;

import java.io.Serializable;

public class Size implements Serializable {
    private String id;
    private String size;
    private double price;

    public Size() {
    }

    public Size(String size, double price) {
        this.size = size;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
