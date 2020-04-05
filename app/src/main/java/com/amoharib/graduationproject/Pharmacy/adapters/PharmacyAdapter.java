package com.amoharib.graduationproject.Pharmacy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoharib.graduationproject.Pharmacy.Activities.MenuPharmacy;
import com.amoharib.graduationproject.Pharmacy.viewholders.PharmacyViewHolder;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.models.AllPharmacy;

import java.util.ArrayList;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyViewHolder> {

    private ArrayList<AllPharmacy> listPharm, tempList;
    private Context context;

    public PharmacyAdapter(ArrayList<AllPharmacy> listPharm, ArrayList<AllPharmacy> tempList, Context context) {
        this.listPharm = listPharm;
        this.tempList = tempList;
        this.context = context;
    }

//    public void passPharmacy(ArrayList<AllPharmacy> allPharmacies){
//        listPharm=allPharmacies;
//        tempList=allPharmacies;
//        notifyDataSetChanged();
//
//    }


    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pharmform, parent, false);

        return new PharmacyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, final int position) {
        final AllPharmacy allPharmacy = listPharm.get(position);
        holder.updateUI(allPharmacy);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(context, MenuPharmacy.class);
                    intent.putExtra("pharmacy",allPharmacy);
                    context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listPharm.size();
    }
}
