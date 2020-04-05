package com.amoharib.graduationproject.models;

import java.io.Serializable;

public class PharmacyItem implements Serializable {

    private String id ,description,icon,name,price,type;

    public PharmacyItem() {
    }

    public PharmacyItem(String id, String description, String icon, String name, String price, String type) {
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
