package com.amoharib.graduationproject.models;

import java.io.Serializable;
import java.util.ArrayList;



public class Food implements Serializable {
    private String id, name, description, icon;
    private ArrayList<Size> sizes;

    public Food() {
    }

    public Food(String name, String description, String icon, ArrayList<Size> sizes) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sizes = sizes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Size> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Size> sizes) {
        this.sizes = sizes;
    }
}
