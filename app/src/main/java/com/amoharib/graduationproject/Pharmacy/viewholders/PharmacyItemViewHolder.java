package com.amoharib.graduationproject.Pharmacy.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.PharmacyItem;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

public class PharmacyItemViewHolder extends RecyclerView.ViewHolder {
    private TextView catPharmName, catPharmDescription, catParmPrice;
    private SimpleDraweeView catParmIcon;

    public PharmacyItemViewHolder(View itemView) {
        super(itemView);

        catPharmName = (TextView) itemView.findViewById(R.id.catPharmName);
        catPharmDescription = (TextView) itemView.findViewById(R.id.catParmDesc);
        catParmPrice = (TextView) itemView.findViewById(R.id.catParmPrice);
        catParmIcon = (SimpleDraweeView) itemView.findViewById(R.id.CatPharmIcon);
    }

    public void updateUI(PharmacyItem pharmacyItem){
        catPharmName.setText(pharmacyItem.getName());
        catParmPrice.setText(pharmacyItem.getPrice());
        catPharmDescription.setText(pharmacyItem.getDescription());
        Picasso.get().load(pharmacyItem.getIcon()).into(catParmIcon);


    }
}
