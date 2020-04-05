package com.amoharib.graduationproject.buyer.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.amoharib.graduationproject.buyer.adapters.RestaurantAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView restaurantRecycler;

    private ProgressBar restaurantProgress;

    private ArrayList<Restaurant> restaurants;
    private RestaurantAdapter restaurantAdapter;

    public RestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurants, container, false);
        initView(v);

        restaurants = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(restaurants, restaurants, this);
        restaurantRecycler.setAdapter(restaurantAdapter);

        DataService.getInstance().getAllRestaurants(new DataListeners.OnRestaurantsListener() {
            @Override
            public void onDataRetrieved(ArrayList<Restaurant> returnedRestaurants) {
                restaurantAdapter.updateRestaurants(returnedRestaurants);
                restaurantRecycler.setVisibility(View.VISIBLE);
                restaurantProgress.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                restaurantAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                restaurantAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void initView(View v) {
        searchView = (SearchView) v.findViewById(R.id.searchView);
        restaurantRecycler = (RecyclerView) v.findViewById(R.id.restaurantRecycler);
        restaurantProgress = (ProgressBar) v.findViewById(R.id.restaurantProgress);

        restaurantRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantRecycler.addItemDecoration(new RestaurantsFragment.VerticalDecorator(5));
    }


    class VerticalDecorator extends RecyclerView.ItemDecoration {

        private final int SPACER;

        VerticalDecorator(int spacer) {
            this.SPACER = spacer;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.bottom = SPACER;
        }
    }

}
