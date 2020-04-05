package com.amoharib.graduationproject.Pharmacy.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.AllPharmacy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

public class PharmacyViewHolder extends RecyclerView.ViewHolder {
    SimpleDraweeView pharmacyLogo;
    TextView pharmacyTitle,pharmacyDesc;

    public PharmacyViewHolder(View itemView) {
        super(itemView);
        pharmacyLogo=(SimpleDraweeView)itemView.findViewById(R.id.pharmacyLogo);
        pharmacyTitle=(TextView)itemView.findViewById(R.id.pharmacyTitle);
        pharmacyDesc=(TextView)itemView.findViewById(R.id.pharmacyDesc);

    }

    public void updateUI(AllPharmacy allPharmacy){

       Picasso.get().load(allPharmacy.getLogo()).into(pharmacyLogo);
          pharmacyTitle.setText(allPharmacy.getTitle());
        pharmacyDesc.setText(allPharmacy.getDescription());

    }
}
