package com.amoharib.graduationproject.hypermarket.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoharib.graduationproject.R;


public class CategoryMarketViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryName;
    public ImageView expandIcon;

    public CategoryMarketViewHolder(View itemView) {
        super(itemView);
        categoryName = (TextView) itemView.findViewById(R.id.categoryTextMarket);
        expandIcon = (ImageView) itemView.findViewById(R.id.expandedIconMarket);
    }

    public void updateUI(String categoryName, boolean expanded) {
        this.categoryName.setText(categoryName);
        expandIcon.setBackgroundResource(!expanded ? R.drawable.ic_expand_more : R.drawable.ic_expand_less);
    }
}


