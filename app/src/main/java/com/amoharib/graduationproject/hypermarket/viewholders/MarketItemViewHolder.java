package com.amoharib.graduationproject.hypermarket.viewholders;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.MarketItem;
import com.facebook.drawee.view.SimpleDraweeView;


public class MarketItemViewHolder extends RecyclerView.ViewHolder {

    private TextView marketName, marketDes, itemPrice, multiSize;
    private SimpleDraweeView itemIcon;

    public MarketItemViewHolder(View itemView) {
        super(itemView);

        marketName = (TextView) itemView.findViewById(R.id.foodNameMarket);
        marketDes = (TextView) itemView.findViewById(R.id.foodDescriptionMarket);
        itemPrice = (TextView) itemView.findViewById(R.id.foodPriceMarket);
        itemIcon = (SimpleDraweeView) itemView.findViewById(R.id.foodIconMarket);
        multiSize = (TextView) itemView.findViewById(R.id.multi_sizesMarket);


    }

    public void updateUI(MarketItem food) {
        marketName.setText(food.getName());
        marketDes.setText(food.getDescription());
        itemPrice.setText(String.format("%sLE", food.getSizes().get(0).getPrice()));
        if (food.getSizes().size() > 1) multiSize.setVisibility(View.VISIBLE);
        itemIcon.setImageURI(Uri.parse(food.getIcon()));
    }
}
