package com.amoharib.graduationproject.buyer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.R;

public class BillViewHolder extends RecyclerView.ViewHolder {
    private TextView foodName, foodCustom, subTotalPrice, quantityText;
    private LinearLayout container;
    public BillViewHolder(View itemView) {
        super(itemView);
        foodName = (TextView) itemView.findViewById(R.id.foodName);
        foodCustom = (TextView) itemView.findViewById(R.id.foodCustom);
        subTotalPrice = (TextView) itemView.findViewById(R.id.subTotalPrice);
        quantityText = (TextView) itemView.findViewById(R.id.quantityText);
        container = (LinearLayout) itemView.findViewById(R.id.quantity_title_container);
    }

    public void updateUI(CartItem item) {
        quantityText.setText(item.getQuantity() + "x");
        foodName.setText(item.getFood().getName());
        foodCustom.setText(item.getCustomOrder());
        subTotalPrice.setText(String.valueOf(item.getQuantity() * item.getSize().getPrice()));
        if (TextUtils.isEmpty(foodCustom.getText())) {
            foodCustom.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 8);
            container.setLayoutParams(layoutParams);
        }
    }
        public void updateUI_market(CartItem item){
            quantityText.setText(item.getQuantity() + "x");
            foodName.setText(item.getMarketItem().getName());
            foodCustom.setText(item.getCustomOrder());
            subTotalPrice.setText(String.valueOf(item.getQuantity() * item.getSize().getPrice()));
            if(TextUtils.isEmpty(foodCustom.getText())){
                foodCustom.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 8);
                container.setLayoutParams(layoutParams);
            }

    }
}
