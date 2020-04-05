package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.Size;
import com.facebook.drawee.view.SimpleDraweeView;

public class FoodItemActivity extends AppCompatActivity {
    private final int RESULT_CODE = 2;

    private TextView foodName;
    private TextView foodDescription;
    private TextView foodPrice;
    private ImageView incQuantityBtn;
    private TextView quantityText;
    private ImageView decQuantityBtn;
    private Button addToCartBtn;
    private SimpleDraweeView foodIcon;
    private EditText customOrderText;
    private LinearLayout sizeContainer;
    private TextView extrasTitle;
    private LinearLayout extrasContainer;

    private Food food;
    private Size selectedSize;

    private int quantity = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);
        initView();

        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("food");


        populateViews();
        initBtnListeners();

    }

    private void populateViews() {
        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText(food.getSizes().get(0).getPrice() + " LE");
        foodIcon.setImageURI(Uri.parse(food.getIcon()));

        initItemSizes();
        initItemExtras();

    }

    private void initItemSizes() {
        for (int i = 0; i < food.getSizes().size(); i++) {
            final Size size = food.getSizes().get(i);

            View v = LayoutInflater.from(this).inflate(R.layout.item_size_selector, sizeContainer, false);
            TextView sizeName = (TextView) v.findViewById(R.id.item_size);
            RadioButton sizePrice = (RadioButton) v.findViewById(R.id.item_price);

            sizeName.setText(size.getSize());
            sizePrice.setText(String.valueOf(size.getPrice()));


            sizePrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //System.out.println(size.getSize());
                    if (isChecked) {
                        selectedSize = size;
                        updateSizes();
                        foodPrice.setText(selectedSize.getPrice() + " LE");
                    }
                }
            });

            sizePrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (i == 0) sizePrice.setChecked(true);
            sizeContainer.addView(v);
        }
    }

    private void updateSizes() {
        for (int i = 0; i < sizeContainer.getChildCount(); i++) {
            View v = sizeContainer.getChildAt(i);
            RadioButton sizePrice = (RadioButton) v.findViewById(R.id.item_price);

            if (!food.getSizes().get(i).getSize().equals(selectedSize.getSize())) {
                sizePrice.setChecked(false);
            }
        }
    }

    private void initItemExtras() {

    }

    private void initBtnListeners() {
        incQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityText.setText(String.valueOf(++quantity));
                if (!decQuantityBtn.isEnabled()) {
                    decQuantityBtn.setEnabled(true);
                }
            }
        });

        decQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantityText.setText(String.valueOf(--quantity));
                    if (quantity == 1) {
                        decQuantityBtn.setEnabled(false);
                    }
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                CartItem cartItem = new CartItem(food, quantity, customOrderText.getText().toString().trim(), selectedSize);
                data.putExtra("food", cartItem);
                setResult(RESULT_CODE, data);
                finish();
            }
        });
    }

    private void initView() {
        foodName = (TextView) findViewById(R.id.foodName);
        foodDescription = (TextView) findViewById(R.id.foodDescription);
        foodPrice = (TextView) findViewById(R.id.foodPrice);
        incQuantityBtn = (ImageView) findViewById(R.id.incQuantityBtn);
        quantityText = (TextView) findViewById(R.id.quantityText);
        decQuantityBtn = (ImageView) findViewById(R.id.decQuantityBtn);
        addToCartBtn = (Button) findViewById(R.id.addToCartBtn);
        foodIcon = (SimpleDraweeView) findViewById(R.id.foodIcon);
        customOrderText = (EditText) findViewById(R.id.customOrderText);
        sizeContainer = (LinearLayout) findViewById(R.id.size_container);
        extrasTitle = (TextView) findViewById(R.id.extras_title);
        extrasContainer = (LinearLayout) findViewById(R.id.extras_container);
        decQuantityBtn.setEnabled(false);
        quantityText.setText(String.valueOf(quantity));
    }
}
