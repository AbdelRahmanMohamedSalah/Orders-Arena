package com.amoharib.graduationproject.hypermarket.fragments;


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

import com.amoharib.graduationproject.hypermarket.adapters.HyperMarketAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.HyperMarket;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HyperMarketsFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView restaurantRecycler;

    private ProgressBar restaurantProgress;

    private ArrayList<HyperMarket> hyperMarkets;
    private HyperMarketAdapter hyperMarketAdapter;

    public HyperMarketsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hyper_markets, container, false);
        initView(v);

        hyperMarkets = new ArrayList<>();
        hyperMarketAdapter = new HyperMarketAdapter(hyperMarkets, hyperMarkets, this);
        restaurantRecycler.setAdapter(hyperMarketAdapter);

        
        DataService.getInstance().getAllHyperMarket(new DataListeners.OnHyperMarketsListener() {
            @Override
            public void onDataRetrieved(ArrayList<HyperMarket> returnedRestaurants) {

                hyperMarketAdapter.updateRestaurants(returnedRestaurants);
                restaurantRecycler.setVisibility(View.VISIBLE);
                restaurantProgress.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hyperMarketAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hyperMarketAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void initView(View v) {
        searchView = (SearchView) v.findViewById(R.id.searchView_hyper_market);
        restaurantRecycler = (RecyclerView) v.findViewById(R.id.restaurantRecycler_hyper_market);
        restaurantProgress = (ProgressBar) v.findViewById(R.id.restaurantProgress_hyper_market);

        restaurantRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantRecycler.addItemDecoration(new com.amoharib.graduationproject.hypermarket.fragments.HyperMarketsFragment.VerticalDecorator(5));
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
