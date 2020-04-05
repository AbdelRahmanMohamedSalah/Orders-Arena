package com.amoharib.graduationproject.hypermarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.graduationproject.Pharmacy.Activities.OrderPharmacy;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.OrderActivity;
import com.amoharib.graduationproject.buyer.viewholders.OrderViewHolder;
import com.amoharib.graduationproject.hypermarket.activities.OrderMarketActivity;
import com.amoharib.graduationproject.models.CartItem;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.graduationproject.buyer.activities.OrderActivity;
import com.amoharib.graduationproject.hypermarket.activities.OrderMarketActivity;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.viewholders.OrderViewHolder;

import java.util.ArrayList;

import java.util.ArrayList;

public class OrderMarketAdapter extends RecyclerView.Adapter<OrderViewHolder> {


    public static final int RESTAURANT = 0, MARKET = 1,pharmacy=2;

        private ArrayList<CartItem> cartItems;
        private Context activity;

        public OrderMarketAdapter(ArrayList<CartItem> cartItems, Context activity) {
            this.cartItems = cartItems;
            this.activity = activity;
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new OrderViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
            final CartItem cartItem = cartItems.get(position);

            if (activity instanceof OrderActivity) {
                holder.updateUI(cartItem, RESTAURANT);

                holder.incQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((OrderActivity) activity).incQuantity(holder.getAdapterPosition());
                        if (!holder.decQuantityBtn.isEnabled()) holder.decQuantityBtn.setEnabled(true);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });

                holder.decQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cartItem.getQuantity() > 1) {
                            ((OrderActivity) activity).decQuantity(holder.getAdapterPosition());
                        }
                        if (cartItem.getQuantity() == 1) {
                            holder.decQuantityBtn.setEnabled(false);
                        }
                        notifyItemChanged(holder.getAdapterPosition());

                    }
                });
            } else if (activity instanceof OrderMarketActivity) {
                holder.updateUI(cartItem, MARKET);

                holder.incQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((OrderMarketActivity) activity).incQuantity(holder.getAdapterPosition());
                        if (!holder.decQuantityBtn.isEnabled()) holder.decQuantityBtn.setEnabled(true);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });

                holder.decQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (cartItem.getQuantity() > 1) {
                            ((OrderMarketActivity) activity).decQuantity(holder.getAdapterPosition());
                        }
                        if (cartItem.getQuantity() == 1) {
                            holder.decQuantityBtn.setEnabled(false);
                        }
                        notifyItemChanged(holder.getAdapterPosition());

                    }
                });
            }else if (activity instanceof OrderPharmacy){

                holder.updateUI(cartItem,pharmacy);
                holder.incQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((OrderPharmacy) activity).incQuantity(holder.getAdapterPosition());
                        if (!holder.decQuantityBtn.isEnabled()) holder.decQuantityBtn.setEnabled(true);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
                holder.decQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (cartItem.getQuantity() > 1) {
                            ((OrderPharmacy) activity).decQuantity(holder.getAdapterPosition());
                        }
                        if (cartItem.getQuantity() == 1) {
                            holder.decQuantityBtn.setEnabled(false);
                        }
                        notifyItemChanged(holder.getAdapterPosition());

                    }
                });



            }
        }

        @Override
        public int getItemCount() {
            return cartItems.size();
        }
    }
