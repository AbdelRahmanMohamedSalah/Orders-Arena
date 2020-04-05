package com.amoharib.graduationproject.Pharmacy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.utils.StaticConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

public class PharmacyItem extends AppCompatActivity {
    com.amoharib.graduationproject.models.PharmacyItem item;
    TextView dragsName, dragsDescription, dragsPrice, dragType, quantityTextPharm;
    EditText customOrderTextPharm;
    Button addToCartBtnPharm;
    SimpleDraweeView dragsIcon;
    ImageView decQuantityBtnPharm, incQuantityBtnPharm;
    private int quantity = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_item);
        dragsName = (TextView) findViewById(R.id.dragsName);
        dragsDescription = (TextView) findViewById(R.id.dragsDescription);
        dragsPrice = (TextView) findViewById(R.id.dragsPrice);
        quantityTextPharm = (TextView) findViewById(R.id.quantityTextPharm);
        dragType = (TextView) findViewById(R.id.dragType);
        customOrderTextPharm = (EditText) findViewById(R.id.customOrderTextPharm);
        addToCartBtnPharm = (Button) findViewById(R.id.addToCartBtnPharm);
        dragsIcon = (SimpleDraweeView) findViewById(R.id.dragsIcon);
        decQuantityBtnPharm = (ImageView) findViewById(R.id.decQuantityBtnPharm);
        incQuantityBtnPharm = (ImageView) findViewById(R.id.incQuantityBtnParm);
        Intent intent = getIntent();
        item = (com.amoharib.graduationproject.models.PharmacyItem) intent.getSerializableExtra("itemPharm");
        dragsName.setText(item.getName());
        dragsDescription.setText(item.getDescription());
        dragsPrice.setText(item.getPrice());
        dragType.setText(item.getType());
        Picasso.get().load(item.getIcon()).into(dragsIcon);
        quantityTextPharm.setText(String.valueOf(quantity));

        initBtnListeners();
    }

    private void initBtnListeners() {
        incQuantityBtnPharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityTextPharm.setText(String.valueOf(++quantity));
                if (!decQuantityBtnPharm.isEnabled()) {
                    decQuantityBtnPharm.setEnabled(true);
                }
            }
        });

        decQuantityBtnPharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantityTextPharm.setText(String.valueOf(--quantity));
                    if (quantity == 1) {
                        decQuantityBtnPharm.setEnabled(false);
                    }
                }
            }
        });

        addToCartBtnPharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CartItem cartItem = new CartItem(Integer.parseInt(quantityTextPharm.getText().toString()), customOrderTextPharm.getText().toString(), item);
                addToCart(cartItem);
                MenuPharmacy.addCardListener.setCount(StaticConfig.CART_ITEMS.size());
                onBackPressed();
                Toast.makeText(PharmacyItem.this, "Add to Card Is Done", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addToCart(CartItem cartItem) {
        for (CartItem item : StaticConfig.CART_ITEMS) {
            if (cartItem.getPharmacyItem().getId().equals(item.getPharmacyItem().getId()) && cartItem.getCustomOrder().equals(item.getCustomOrder())) {
                item.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        StaticConfig.CART_ITEMS.add(cartItem);
    }
}
