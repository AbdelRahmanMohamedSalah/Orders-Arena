package com.amoharib.graduationproject.hypermarket.viewholders;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.HyperMarket;

import com.amoharib.graduationproject.models.Restaurant;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Abdel Rahman on 18-May-18.
 */

public class HyperMarketViewHolder extends RecyclerView.ViewHolder {
    private TextView hypermarketTitle;
    private TextView hypermarketDesc;
    private SimpleDraweeView hypermarketLogo;

    public HyperMarketViewHolder(View itemView) {
        super(itemView);
        hypermarketTitle = (TextView) itemView.findViewById(R.id.restaurantTitle_hyper_market);
        hypermarketDesc = (TextView) itemView.findViewById(R.id.restaurantDesc_hyper_market);
        hypermarketLogo = (SimpleDraweeView) itemView.findViewById(R.id.restaurantLogo_hyper_market);
    }

    public void updateUI(HyperMarket hyperMarket){
        hypermarketLogo.setImageURI(Uri.parse(hyperMarket.getLogo()));
        hypermarketTitle.setText(hyperMarket.getTitle());
        hypermarketDesc.setText(hyperMarket.getDescription());
    }

}
