package com.amoharib.graduationproject.buyer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoharib.graduationproject.R;


public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryName;
    public ImageView expandIcon;
    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryName = (TextView) itemView.findViewById(R.id.categoryText);
        expandIcon = (ImageView) itemView.findViewById(R.id.expandedIcon);
    }

    public void updateUI(String categoryName, boolean expanded){
        this.categoryName.setText(categoryName);
        expandIcon.setBackgroundResource(!expanded ? R.drawable.ic_expand_more: R.drawable.ic_expand_less);
    }
}
