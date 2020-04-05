package com.amoharib.graduationproject.models;

import java.io.Serializable;



public class Restaurant implements Serializable {
    private String title, description, logo, id, token;

    public Restaurant() {
    }

    public Restaurant(String title, String description, String logo, String id, String token) {
        this.title = title;
        this.description = description;
        this.logo = logo;
        this.id = id;
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
