package com.amoharib.graduationproject.models;

import java.io.Serializable;



public class CartItem implements Serializable {
    private Food food;
    private MarketItem marketItem;
    private int quantity;
    private String customOrder;
    private Size size;
    private PharmacyItem pharmacyItem;

    public CartItem() {
    }

    public CartItem(Food food, int quantity, String customOrder, Size size) {
        this.food = food;
        this.quantity = quantity;
        this.customOrder = customOrder;
        this.size = size;
    }
    public CartItem(MarketItem marketItem, int quantity, String customOrder, Size size) {
        this.marketItem = marketItem;
        this.quantity = quantity;
        this.customOrder = customOrder;

        this.size = size;
    }

    public CartItem(int quantity, String customOrder, PharmacyItem pharmacyItem) {
        this.quantity = quantity;
        this.customOrder = customOrder;
        this.pharmacyItem = pharmacyItem;
    }

    public PharmacyItem getPharmacyItem() {
        return pharmacyItem;
    }

    public void setPharmacyItem(PharmacyItem pharmacyItem) {
        this.pharmacyItem = pharmacyItem;
    }

    public MarketItem getMarketItem() {
        return marketItem;
    }

    public void setMarketItem(MarketItem marketItem) {
        this.marketItem = marketItem;
    }


    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(String customOrder) {
        this.customOrder = customOrder;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
