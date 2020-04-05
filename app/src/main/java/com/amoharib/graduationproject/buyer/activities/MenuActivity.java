package com.amoharib.graduationproject.buyer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.buyer.adapters.MenuAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.andremion.counterfab.CounterFab;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MenuActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    private final int RESULT_CODE = 2;

    private RecyclerView foodRecycler;
    private TextView restaurantTitle;
    private TextView restaurantDesc;
    private SimpleDraweeView restaurantLogo;
    private SearchView foodSearchView;

    private SectionedRecyclerViewAdapter adapter;

    private Restaurant restaurant;
    private CounterFab cartFab;

    public static ArrayList<CartItem> cartItems;
    public static String restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        cartItems = new ArrayList<>();
        restaurantID = restaurant.getId();


        DataService.getInstance().getMenuForRestaurantId(restaurantID, new DataListeners.OnMenuListener() {
            @Override
            public void onMenuRetrieved(ArrayList<Food> foods, String category) {
                adapter.addSection(new MenuAdapter(foods, foods, category, MenuActivity.this));
                updateAdapter();
            }
        });

    }


    private void initView() {
        foodRecycler = (RecyclerView) findViewById(R.id.foodRecycler);
        restaurantTitle = (TextView) findViewById(R.id.restaurantTitle);
        restaurantDesc = (TextView) findViewById(R.id.restaurantDesc);
        restaurantLogo = (SimpleDraweeView) findViewById(R.id.restaurantLogo);
        foodSearchView = (SearchView) findViewById(R.id.foodSearchView);
        cartFab = (CounterFab) findViewById(R.id.cartFab);


        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        restaurantTitle.setText(restaurant.getTitle());
        restaurantDesc.setText(restaurant.getDescription());
        restaurantLogo.setImageURI(Uri.parse(restaurant.getLogo()));

        Drawable drawable = getResources().getDrawable(R.drawable.vertical_divider_dark);
        int inset = 50;
        InsetDrawable insetDrawable = new InsetDrawable(drawable, inset, 0 , inset, 0);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(insetDrawable);

        adapter = new SectionedRecyclerViewAdapter();
        foodRecycler.setLayoutManager(new LinearLayoutManager(this));
        foodRecycler.addItemDecoration(dividerItemDecoration);
        foodRecycler.setAdapter(adapter);

        foodSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        cartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(MenuActivity.this, "Your cart is empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MenuActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void startFoodItemActivity(Food food) {
        Intent intent = new Intent(this, FoodItemActivity.class);
        intent.putExtra("food", food);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {

                CartItem cartItem = (CartItem) data.getSerializableExtra("food");
                addToCart(cartItem);
                cartFab.setCount(cartItems.size());
            }
        }
    }

    private void addToCart(CartItem cartItem) {
        for (CartItem item : cartItems) {
            if (cartItem.getFood().getId().equals(item.getFood().getId()) && cartItem.getCustomOrder().equals(item.getCustomOrder())) {
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
                    MenuActivity.super.onBackPressed();
                }
            }).setNegativeButton("NO", null).create().show();
        } else {
            super.onBackPressed();
        }
    }
}
