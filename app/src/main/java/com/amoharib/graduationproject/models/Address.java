package com.amoharib.graduationproject.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Address implements Parcelable {
    private String id;
    private String addressName;
    private String districtName;
    private String streetNumber;
    private String houseBuilding;
    private String apartmentOffice;
    private String phone;
    private String deliveryInstructions;

    public Address() {
    }

    public Address(String addressName, String districtName, String streetNumber, String houseBuilding, String apartmentOffice, String phone, String deliveryInstructions) {
        this.addressName = addressName;
        this.districtName = districtName;
        this.streetNumber = streetNumber;
        this.houseBuilding = houseBuilding;
        this.apartmentOffice = apartmentOffice;
        this.phone = phone;
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getHouseBuilding() {
        return houseBuilding;
    }

    public void setHouseBuilding(String houseBuilding) {
        this.houseBuilding = houseBuilding;
    }

    public String getApartmentOffice() {
        return apartmentOffice;
    }

    public void setApartmentOffice(String apartmentOffice) {
        this.apartmentOffice = apartmentOffice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.addressName);
        dest.writeString(this.districtName);
        dest.writeString(this.streetNumber);
        dest.writeString(this.houseBuilding);
        dest.writeString(this.apartmentOffice);
        dest.writeString(this.phone);
        dest.writeString(this.deliveryInstructions);
    }

    protected Address(Parcel in) {
        this.id = in.readString();
        this.addressName = in.readString();
        this.districtName = in.readString();
        this.streetNumber = in.readString();
        this.houseBuilding = in.readString();
        this.apartmentOffice = in.readString();
        this.phone = in.readString();
        this.deliveryInstructions = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
