package com.amoharib.graduationproject.hypermarket.viewholders;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.CartItem;

public class OrderMarketViewHolder extends RecyclerView.ViewHolder {

    private TextView quantity, foodName, foodCustomOrder, subTotalPrice;
    public AppCompatImageButton incQuantityBtn, decQuantityBtn;

    public OrderMarketViewHolder(View itemView) {
        super(itemView);
        quantity = itemView.findViewById(R.id.quantityText);
        foodName = itemView.findViewById(R.id.foodName);
        foodCustomOrder = itemView.findViewById(R.id.foodCustom);
        subTotalPrice = itemView.findViewById(R.id.subtotalPrice);
        incQuantityBtn = itemView.findViewById(R.id.incQuantityBtn);
        decQuantityBtn = itemView.findViewById(R.id.decQuantityBtn);

    }

    public void updateUI(CartItem cartItem) {
        quantity.setText(String.valueOf(cartItem.getQuantity()));
        foodName.setText(cartItem.getMarketItem().getName());
        foodCustomOrder.setText(cartItem.getCustomOrder());
        //subTotalPrice.setText(String.valueOf(cartItem.getQuantity() * Float.parseFloat(cartItem.getFood().getPrice())));

        if (cartItem.getQuantity() == 1) decQuantityBtn.setEnabled(false);
    }
}
