package com.amoharib.graduationproject.buyer.viewholders;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.R;
import com.facebook.drawee.view.SimpleDraweeView;



public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private TextView restaurantTitle;
    private TextView restaurantDesc;
    private SimpleDraweeView restaurantLogo;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        restaurantTitle = (TextView) itemView.findViewById(R.id.restaurantTitle);
        restaurantDesc = (TextView) itemView.findViewById(R.id.restaurantDesc);
        restaurantLogo = (SimpleDraweeView) itemView.findViewById(R.id.restaurantLogo);
    }

    public void updateUI(Restaurant restaurant){
        restaurantLogo.setImageURI(Uri.parse(restaurant.getLogo()));
        restaurantTitle.setText(restaurant.getTitle());
        restaurantDesc.setText(restaurant.getDescription());
    }


}
