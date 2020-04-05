package com.amoharib.graduationproject.hypermarket.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.hypermarket.viewholders.UserOrderMarketViewHolder;
import com.amoharib.graduationproject.models.Order;

import java.util.ArrayList;

    public class UserOrderMarketAdapter extends RecyclerView.Adapter<UserOrderMarketViewHolder> {
        public ArrayList<Order> orders;

        public UserOrderMarketAdapter(ArrayList<Order> orders) {
            this.orders = orders;
        }

        @Override
        public UserOrderMarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_item, parent, false);
            return new UserOrderMarketViewHolder(v);
        }

        @Override
        public void onBindViewHolder(UserOrderMarketViewHolder holder, int position) {
            Order order = orders.get(position);
            holder.updateView(order);
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }
    }
