package com.amoharib.graduationproject.hypermarket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.AddressSelectionActivity;

import com.amoharib.graduationproject.buyer.adapters.OrderAdapter;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.models.Order;


import java.util.ArrayList;

public class OrderMarketActivity extends AppCompatActivity {

    private RecyclerView recyclerOrder_market;
    private Button checkoutBtn_market;
    private Toolbar orderToolbar_market;
    private OrderAdapter orderMarketAdapter;

    private ArrayList<CartItem> cartItems = MenuHyperMarketActivity.cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_market);

        initView();

        orderMarketAdapter = new OrderAdapter(cartItems, this);
        recyclerOrder_market.setAdapter(orderMarketAdapter);

        calculateTotal();
    }

    public void incQuantity(int position) {
        this.cartItems.get(position).setQuantity(this.cartItems.get(position).getQuantity() + 1);
    }

    public void decQuantity(int position) {
        this.cartItems.get(position).setQuantity(this.cartItems.get(position).getQuantity() - 1);
    }


    private void calculateTotal() {

    }

    private void initView() {
        recyclerOrder_market = (RecyclerView) findViewById(R.id.recycler_order_market);
        checkoutBtn_market = (Button) findViewById(R.id.checkout_btn_market);
        orderToolbar_market = (Toolbar) findViewById(R.id.order_toolbar_market);

        if (getSupportActionBar() == null) {
            setSupportActionBar(orderToolbar_market);
        }
        getSupportActionBar().setTitle("");

        recyclerOrder_market.setLayoutManager(new LinearLayoutManager(this));

        checkoutBtn_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.amoharib.graduationproject.hypermarket.activities.OrderMarketActivity.this, AddressSelectionMarketActivity.class));
            }
        });
    }
}

