package com.amoharib.graduationproject.buyer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.amoharib.graduationproject.buyer.activities.MenuActivity;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.viewholders.CategoryViewHolder;
import com.amoharib.graduationproject.buyer.viewholders.FoodItemViewHolder;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;



public class MenuAdapter extends StatelessSection implements Filterable {

    private ArrayList<Food> list, tempList;
    private boolean expanded = false;
    private String title;
    private Context context;

    public MenuAdapter(ArrayList<Food> list, ArrayList<Food> tempList, String title, Context context) {
        super(new SectionParameters.Builder(R.layout.food_item)
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
                ((MenuActivity) context).updateAdapter();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Food food = list.get(position);
        final FoodItemViewHolder foodItemViewHolder = (FoodItemViewHolder) holder;
        foodItemViewHolder.updateUI(food);

        foodItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity)context).startFoodItemActivity(food);
            }
        });
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? list.size() : 0;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Food> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = tempList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Food>) results.values;
                ((MenuActivity)context).updateAdapter();
            }
        };
    }

    protected ArrayList<Food> getFilteredResults(String constraint) {
        ArrayList<Food> results = new ArrayList<>();

        for (Food item : tempList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}
