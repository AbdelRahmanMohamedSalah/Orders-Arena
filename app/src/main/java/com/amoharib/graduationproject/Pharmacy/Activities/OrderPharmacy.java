package com.amoharib.graduationproject.Pharmacy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.AddressSelectionActivity;
import com.amoharib.graduationproject.buyer.adapters.OrderAdapter;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.utils.StaticConfig;

import java.util.ArrayList;

public class OrderPharmacy extends AppCompatActivity {
    RecyclerView recycler_card;
    Button checkout_btnPharmacy;
    ArrayList<CartItem> allItems = StaticConfig.CART_ITEMS;
    OrderAdapter orderAdapter;
    private Toolbar orderToolbar_market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pharmacy);
        recycler_card = (RecyclerView) findViewById(R.id.recycler_card);
        checkout_btnPharmacy = (Button) findViewById(R.id.checkout_btnPharmacy);
        orderAdapter = new OrderAdapter(allItems, this);
        orderToolbar_market = (Toolbar) findViewById(R.id.order_toolbar_market);
        recycler_card.setAdapter(orderAdapter);
        recycler_card.setLayoutManager(new LinearLayoutManager(this));
        if (getSupportActionBar() == null) {
            setSupportActionBar(orderToolbar_market);
        }

        calculateTotal();

        checkout_btnPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderPharmacy.this,AddressSelectionPharmacy.class));
            }
        });

    }

    public void incQuantity(int position) {
        this.allItems.get(position).setQuantity(this.allItems.get(position).getQuantity() + 1);
    }

    public void decQuantity(int position) {
        this.allItems.get(position).setQuantity(this.allItems.get(position).getQuantity() - 1);
    }

    private void calculateTotal() {

    }

}
