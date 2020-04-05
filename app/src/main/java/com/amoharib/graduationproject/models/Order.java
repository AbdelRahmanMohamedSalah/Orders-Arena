package com.amoharib.graduationproject.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

import com.amoharib.graduationproject.utils.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    private String id, restId, userId, userAddressId;
    private OrderStatus status = OrderStatus.SENT;
    private ArrayList<CartItem> items;
    private long timestamp;

    public Order() {
    }

    public Order(String id, String restId, String userId, String userAddressId, ArrayList<CartItem> items, long timestamp) {
        this.id = id;
        this.restId = restId;
        this.userId = userId;
        this.userAddressId = userAddressId;
        this.items = items;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.restId);
        dest.writeString(this.userId);
        dest.writeString(this.userAddressId);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeList(this.items);
        dest.writeLong(this.timestamp);
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.restId = in.readString();
        this.userId = in.readString();
        this.userAddressId = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : OrderStatus.values()[tmpStatus];
        this.items = new ArrayList<CartItem>();
        in.readList(this.items, CartItem.class.getClassLoader());
        this.timestamp = in.readLong();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
