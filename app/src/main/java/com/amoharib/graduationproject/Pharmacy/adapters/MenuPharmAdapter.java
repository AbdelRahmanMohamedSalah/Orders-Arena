package com.amoharib.graduationproject.Pharmacy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filterable;
import android.widget.Toast;

import com.amoharib.graduationproject.Pharmacy.Activities.MenuPharmacy;
import com.amoharib.graduationproject.Pharmacy.viewholders.PharmacyItemViewHolder;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.MenuActivity;
import com.amoharib.graduationproject.buyer.viewholders.CategoryViewHolder;
import com.amoharib.graduationproject.buyer.viewholders.FoodItemViewHolder;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.PharmacyItem;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MenuPharmAdapter extends StatelessSection {

    private ArrayList<PharmacyItem> list, tempList;
    private boolean expanded = false;
    private String title;
    private Context context;

    public MenuPharmAdapter(ArrayList<PharmacyItem> list, ArrayList<PharmacyItem> tempList, String title, Context context) {
        super(new SectionParameters.Builder(R.layout.pharm_item)
                .headerResourceId(R.layout.food_category)
                .build());

        this.list = list;
        this.tempList = tempList;
        this.title = title;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        categoryViewHolder.updateUI(title, expanded);
        categoryViewHolder.expandIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                categoryViewHolder.expandIcon.setBackgroundResource(!expanded ? R.drawable.ic_expand_more : R.drawable.ic_expand_less);
                ((MenuPharmacy) context).updateAdapter();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new PharmacyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final PharmacyItem food = list.get(position);
        final PharmacyItemViewHolder pharmacyItemViewHolder = (PharmacyItemViewHolder) holder;
        pharmacyItemViewHolder.updateUI(food);
        pharmacyItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, com.amoharib.graduationproject.Pharmacy.Activities.PharmacyItem.class);
                intent.putExtra("itemPharm",food);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? list.size() : 0;
    }
}

