package com.amoharib.graduationproject.hypermarket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.R;

import com.amoharib.graduationproject.hypermarket.adapters.MenuMarketAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.CartItem;

import com.amoharib.graduationproject.models.HyperMarket;

import com.amoharib.graduationproject.models.MarketItem;
import com.amoharib.graduationproject.services.DataService;
import com.andremion.counterfab.CounterFab;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MenuHyperMarketActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    private final int RESULT_CODE = 2;

    private RecyclerView marketItemRecycler;
    private TextView hyperMarketTitle;
    private TextView hyperMarketDesc;
    private SimpleDraweeView hyperMarketLogo;
    private SearchView marketItemSearchView;

    private SectionedRecyclerViewAdapter adapter;

    private HyperMarket hyperMarket;
    private CounterFab cartFabMarket;
    private MarketItem marketItem;

    public static ArrayList<CartItem> cartItems;
    public static String hypermarketID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hyper_market);
        initView();

        cartItems = new ArrayList<>();
        hypermarketID = hyperMarket.getId();

        DataService.getInstance().getMenuForMarketId(hypermarketID, new DataListeners.OnMenuMarketListener() {
            @Override
            public void onMenuMarketRetrieved(ArrayList<MarketItem> marketItems, String category) {

                adapter.addSection(new MenuMarketAdapter(marketItems, marketItems, category, MenuHyperMarketActivity.this));
                updateAdapter();
            }
        });


    }

    private void initView() {
        marketItemRecycler = (RecyclerView) findViewById(R.id.marketItemRecycler);
        hyperMarketTitle = (TextView) findViewById(R.id.hyperMarketTitle);
        hyperMarketDesc = (TextView) findViewById(R.id.hyperMarketDesc);
        hyperMarketLogo = (SimpleDraweeView) findViewById(R.id.hyperMarketLogo);
        marketItemSearchView = (SearchView) findViewById(R.id.marketItemSearchView);
        cartFabMarket = (CounterFab) findViewById(R.id.cartFabMarket);


        Intent intent = getIntent();
        hyperMarket = (HyperMarket) intent.getSerializableExtra("hypermarket");

        hyperMarketTitle.setText(hyperMarket.getTitle());
        hyperMarketDesc.setText(hyperMarket.getDescription());
        hyperMarketLogo.setImageURI(Uri.parse(hyperMarket.getLogo()));

        Drawable drawable = getResources().getDrawable(R.drawable.vertical_divider_dark);
        int inset = 50;
        InsetDrawable insetDrawable = new InsetDrawable(drawable, inset, 0, inset, 0);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(insetDrawable);

        adapter = new SectionedRecyclerViewAdapter();
        marketItemRecycler.setLayoutManager(new LinearLayoutManager(this));
        marketItemRecycler.addItemDecoration(dividerItemDecoration);
        marketItemRecycler.setAdapter(adapter);

        marketItemSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        cartFabMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(MenuHyperMarketActivity.this, "Your cart is empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MenuHyperMarketActivity.this, OrderMarketActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void startMarketItemActivity(MarketItem marketItem) {
        Intent intent = new Intent(this, MarketItemActivity.class);
        intent.putExtra("market", marketItem );

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {

                CartItem cartItem = (CartItem) data.getSerializableExtra("market");
                addToCart(cartItem);
                cartFabMarket.setCount(cartItems.size());
            }
        }
    }

    private void addToCart(CartItem cartItem) {
        for (CartItem item : cartItems) {
            System.out.println(cartItem.getMarketItem().getId() + " " + item.getMarketItem().getId());
            if (cartItem.getMarketItem().getId().equals(item.getMarketItem().getId()) && cartItem.getCustomOrder().equals(item.getCustomOrder())) {
                item.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(cartItem);
    }

    @Override
    public void onBackPressed() {
        if (!cartItems.isEmpty()) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Oops, Are you sure to go back and clear your current cart ??");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MenuHyperMarketActivity.super.onBackPressed();
                }
            }).setNegativeButton("NO", null).create().show();
        } else {
            super.onBackPressed();
        }
    }
}
