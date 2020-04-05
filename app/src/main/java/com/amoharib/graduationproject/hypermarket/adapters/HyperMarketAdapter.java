package com.amoharib.graduationproject.hypermarket.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.amoharib.graduationproject.hypermarket.activities.HyperMarketActivity;
import com.amoharib.graduationproject.hypermarket.activities.MenuHyperMarketActivity;
import com.amoharib.graduationproject.models.HyperMarket;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.hypermarket.viewholders.HyperMarketViewHolder;

import java.util.ArrayList;


public class HyperMarketAdapter extends RecyclerView.Adapter<HyperMarketViewHolder> implements Filterable {


    private ArrayList<HyperMarket> hypermarkets, tempList;
    private Fragment fragment;

    public HyperMarketAdapter(ArrayList<HyperMarket> hyperMarkets, ArrayList<HyperMarket> tempList, Fragment context) {
        this.hypermarkets = hyperMarkets;
        this.tempList = tempList;
        this.fragment = context;
    }

    public void updateRestaurants(ArrayList<HyperMarket> returnedHyperMarkets) {
        hypermarkets = returnedHyperMarkets;
        tempList = returnedHyperMarkets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HyperMarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hypermarket_markets, parent, false);
        return new HyperMarketViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HyperMarketViewHolder holder, int position) {
        final HyperMarket hyperMarket = hypermarkets.get(position);
        holder.updateUI(hyperMarket);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getActivity(), MenuHyperMarketActivity.class);
                intent.putExtra("hypermarket", hyperMarket);
                fragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hypermarkets.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<HyperMarket> filteredResults = null;
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
                hypermarkets = (ArrayList<HyperMarket>) results.values;
                com.amoharib.graduationproject.hypermarket.adapters.HyperMarketAdapter.this.notifyDataSetChanged();
            }
        };
    }

    private ArrayList<HyperMarket> getFilteredResults(String constraint) {
        ArrayList<HyperMarket> results = new ArrayList<>();

        for (HyperMarket item : tempList) {
            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }




}
