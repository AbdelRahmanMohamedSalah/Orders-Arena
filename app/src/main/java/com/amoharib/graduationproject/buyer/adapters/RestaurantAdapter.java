package com.amoharib.graduationproject.buyer.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.amoharib.graduationproject.buyer.activities.MenuActivity;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.viewholders.RestaurantViewHolder;

import java.util.ArrayList;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> implements Filterable {

    private ArrayList<Restaurant> restaurants, tempList;
    private Fragment fragment;

    public RestaurantAdapter(ArrayList<Restaurant> restaurants, ArrayList<Restaurant> tempList, Fragment context) {
        this.restaurants = restaurants;
        this.tempList = tempList;
        this.fragment = context;
    }

    public void updateRestaurants(ArrayList<Restaurant> returnedRestaurants) {
        restaurants = returnedRestaurants;
        tempList = returnedRestaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest, parent, false);
        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        holder.updateUI(restaurant);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getActivity(), MenuActivity.class);
                intent.putExtra("restaurant", restaurant);
                fragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Restaurant> filteredResults = null;
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
                restaurants = (ArrayList<Restaurant>) results.values;
                RestaurantAdapter.this.notifyDataSetChanged();
            }
        };
    }

    private ArrayList<Restaurant> getFilteredResults(String constraint) {
        ArrayList<Restaurant> results = new ArrayList<>();

        for (Restaurant item : tempList) {
            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

}
