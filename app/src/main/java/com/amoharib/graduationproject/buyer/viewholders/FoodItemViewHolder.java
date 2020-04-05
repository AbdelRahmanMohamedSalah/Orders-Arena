package com.amoharib.graduationproject.buyer.viewholders;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.R;
import com.facebook.drawee.view.SimpleDraweeView;



public class FoodItemViewHolder extends RecyclerView.ViewHolder {

    private TextView foodName, foodDescription, foodPrice, multiSize;
    private SimpleDraweeView foodIcon;

    public FoodItemViewHolder(View itemView) {
        super(itemView);

        foodName = (TextView) itemView.findViewById(R.id.foodName);
        foodDescription = (TextView) itemView.findViewById(R.id.foodDescription);
        foodPrice = (TextView) itemView.findViewById(R.id.foodPrice);
        foodIcon = (SimpleDraweeView) itemView.findViewById(R.id.foodIcon);
        multiSize = (TextView) itemView.findViewById(R.id.multi_sizes);


    }

    public void updateUI(Food food) {
        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText(String.format("%sLE", food.getSizes().get(0).getPrice()));
        if (food.getSizes().size() > 1) multiSize.setVisibility(View.VISIBLE);
        foodIcon.setImageURI(Uri.parse(food.getIcon()));
    }
}
