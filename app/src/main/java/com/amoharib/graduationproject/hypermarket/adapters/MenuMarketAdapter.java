package com.amoharib.graduationproject.hypermarket.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.hypermarket.activities.MenuHyperMarketActivity;
import com.amoharib.graduationproject.hypermarket.viewholders.CategoryMarketViewHolder;
import com.amoharib.graduationproject.hypermarket.viewholders.MarketItemViewHolder;
import com.amoharib.graduationproject.models.MarketItem;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MenuMarketAdapter extends StatelessSection implements Filterable {

    private ArrayList<MarketItem> list, tempList;
    private boolean expanded = false;
    private String title;
    private Context context;

    public MenuMarketAdapter(ArrayList<MarketItem> list, ArrayList<MarketItem> tempList, String title, Context context) {
        super(new SectionParameters.Builder(R.layout.market_item)
                .headerResourceId(R.layout.market_category)
                .build());

        this.list = list;
        this.tempList = tempList;
        this.title = title;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new CategoryMarketViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final CategoryMarketViewHolder categoryMarketViewHolder = (CategoryMarketViewHolder) holder;
        categoryMarketViewHolder.updateUI(title, expanded);
        categoryMarketViewHolder.expandIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                categoryMarketViewHolder.expandIcon.setBackgroundResource(!expanded ? R.drawable.ic_expand_more : R.drawable.ic_expand_less);
                ((MenuHyperMarketActivity) context).updateAdapter();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new MarketItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MarketItem marketItem = list.get(position);
        final MarketItemViewHolder marketItemViewHolder = (MarketItemViewHolder) holder;
        marketItemViewHolder.updateUI(marketItem);

        marketItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuHyperMarketActivity) context).startMarketItemActivity(marketItem);
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
                ArrayList<MarketItem> filteredResults = null;
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
                list = (ArrayList<MarketItem>) results.values;
                ((MenuHyperMarketActivity) context).updateAdapter();
            }
        };
    }

    protected ArrayList<MarketItem> getFilteredResults(String constraint) {
        ArrayList<MarketItem> results = new ArrayList<>();

        for (MarketItem item : tempList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}


///
