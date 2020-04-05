package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.amoharib.graduationproject.buyer.adapters.OrderAdapter;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.R;
import java.util.ArrayList;


public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerOrder;
    private Button checkoutBtn;
    private Toolbar orderToolbar;
    private OrderAdapter orderAdapter;

    private ArrayList<CartItem> cartItems = MenuActivity.cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();

        orderAdapter = new OrderAdapter(cartItems, this);
        recyclerOrder.setAdapter(orderAdapter);

        calculateTotal();
    }

    public void incQuantity(int position){
        this.cartItems.get(position).setQuantity(this.cartItems.get(position).getQuantity() + 1);
    }
    public void decQuantity(int position){
        this.cartItems.get(position).setQuantity(this.cartItems.get(position).getQuantity() - 1);
    }


    private void calculateTotal(){

    }

    private void initView() {
        recyclerOrder = (RecyclerView) findViewById(R.id.recycler_order);
        checkoutBtn = (Button) findViewById(R.id.checkout_btn);
        orderToolbar = (Toolbar) findViewById(R.id.order_toolbar);

        if(getSupportActionBar() == null){
            setSupportActionBar(orderToolbar);
        }
        getSupportActionBar().setTitle("");

        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderActivity.this, AddressSelectionActivity.class));
            }
        });
    }
}
