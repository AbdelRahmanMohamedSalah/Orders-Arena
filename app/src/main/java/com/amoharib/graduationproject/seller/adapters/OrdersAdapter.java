package com.amoharib.graduationproject.seller.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.seller.activities.OrderDetailsActivity;
import com.amoharib.graduationproject.seller.viewholders.OrderViewHolder;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    public ArrayList<Order> orders;
    private Fragment fragment;

    public OrdersAdapter(ArrayList<Order> orders, Fragment fragment) {
        this.orders = orders;
        this.fragment = fragment;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final Order order = orders.get(position);
        long timestamp = order.getTimestamp();
        holder.updateView(timestamp);

        holder.viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.startActivity(new Intent(fragment.getActivity(), OrderDetailsActivity.class).putExtra("order", order));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }
}
