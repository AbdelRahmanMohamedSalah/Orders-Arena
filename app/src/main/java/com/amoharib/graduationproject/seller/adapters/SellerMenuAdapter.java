package com.amoharib.graduationproject.seller.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.viewholders.CategoryViewHolder;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.seller.activities.AddEditMenuItemActivity;
import com.amoharib.graduationproject.seller.activities.SellerDashboardActivity;
import com.amoharib.graduationproject.seller.fragments.ItemFragment;
import com.amoharib.graduationproject.seller.viewholders.SellerMenuItemViewHolder;
import com.amoharib.graduationproject.utils.RestControlStatus;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class SellerMenuAdapter extends StatelessSection {
    private ArrayList<Food> foods;
    private boolean expanded = false;
    private String title;
    private ItemFragment itemFragment;
    private Intent intent;

    public SellerMenuAdapter(ArrayList<Food> foods, String title, ItemFragment itemFragment) {
        super(new SectionParameters.Builder(R.layout.menu_item).headerResourceId(R.layout.food_category).build());
        this.foods = foods;
        this.title = title;
        this.itemFragment = itemFragment;
        this.intent = new Intent(itemFragment.getActivity(), AddEditMenuItemActivity.class);
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? foods.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        categoryViewHolder.updateUI(title, expanded);
        categoryViewHolder.expandIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                categoryViewHolder.expandIcon.setBackgroundResource(!expanded ? R.drawable.ic_expand_more : R.drawable.ic_expand_less);
                itemFragment.updateAdapter();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SellerMenuItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Food food = foods.get(position);
        SellerMenuItemViewHolder sellerMenuItemViewHolder = (SellerMenuItemViewHolder) holder;
        sellerMenuItemViewHolder.updateUI(food, title).addOnDataChangeListener(new SellerMenuItemViewHolder.OnDataChangeListener() {
            @Override
            public void onDataChanged(RestControlStatus restControlStatus) {
                switch (restControlStatus) {
                    case EDITED:
                        intent.putExtra("food", food);
                        intent.putExtra("category", title);
                        itemFragment.startActivityForResult(intent, itemFragment.REQUEST_CODE);
                        itemFragment.updateAdapter();
                        break;
                    case DELETED:
                        foods.remove(position);
                        itemFragment.notifyAdapterWithItemRemoved(title, position);
                        break;
                }
            }
        });
    }
}
