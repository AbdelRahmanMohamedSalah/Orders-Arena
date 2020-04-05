package com.amoharib.graduationproject.seller.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.seller.activities.AddEditMenuItemActivity;
import com.amoharib.graduationproject.seller.adapters.SellerMenuAdapter;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {

    public final int REQUEST_CODE = 1;

    private RecyclerView itemsRecycler;
    private FloatingActionButton addNewItemFab;
    private SectionedRecyclerViewAdapter sellerMenuAdapter = new SectionedRecyclerViewAdapter();

    String restId;
    private ProgressBar progressBar;

    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item, container, false);
        initView(v);

        Drawable drawable = getResources().getDrawable(R.drawable.vertical_divider_dark);
        int inset = 50;
        InsetDrawable insetDrawable = new InsetDrawable(drawable, inset, 0, inset, 0);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(insetDrawable);

        itemsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsRecycler.addItemDecoration(dividerItemDecoration);
        itemsRecycler.setAdapter(sellerMenuAdapter);

        restId = StringUtils.getUsernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        addNewItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AddEditMenuItemActivity.class), REQUEST_CODE);
            }
        });

        downloadMenu();

        return v;
    }

    private void downloadMenu() {
        itemsRecycler.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        sellerMenuAdapter.removeAllSections();
        DataService.getInstance().getMenuForRestaurantId(restId, new DataListeners.OnMenuListener() {
            @Override
            public void onMenuRetrieved(ArrayList<Food> foods, String category) {
                if (itemsRecycler.getVisibility() == View.INVISIBLE) {
                    itemsRecycler.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                sellerMenuAdapter.addSection(category, new SellerMenuAdapter(foods, category, ItemFragment.this));
                updateAdapter();
            }
        });

    }

    public void notifyAdapterWithItemRemoved(String section, int position) {
        sellerMenuAdapter.notifyItemRemovedFromSection(section, position);

    }

    public void updateAdapter() {
        sellerMenuAdapter.notifyDataSetChanged();
    }

    private void initView(View v) {
        itemsRecycler = (RecyclerView) v.findViewById(R.id.items_recycler);
        addNewItemFab = (FloatingActionButton) v.findViewById(R.id.add_new_item_fab);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            downloadMenu();
        }
    }
}
