package com.amoharib.graduationproject.buyer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.buyer.viewholders.UserOrderViewHolder;

import java.util.ArrayList;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderViewHolder> {
    public ArrayList<Order> orders;

    public UserOrderAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public UserOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_item, parent, false);
        return new UserOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserOrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.updateView(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
