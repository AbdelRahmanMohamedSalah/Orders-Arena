package com.amoharib.graduationproject.hypermarket.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.AddressSelectionActivity;
import com.amoharib.graduationproject.buyer.activities.EditAddressActivity;
import com.amoharib.graduationproject.buyer.activities.PaymentActivity;
import com.amoharib.graduationproject.buyer.adapters.AddressAdapter;
import com.amoharib.graduationproject.buyer.viewholders.AddressViewHolder;
import com.amoharib.graduationproject.hypermarket.activities.AddressSelectionMarketActivity;
import com.amoharib.graduationproject.hypermarket.activities.PaymentMarketActivity;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.services.DataService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddressPharmacyAdapter extends RecyclerView.Adapter<AddressViewHolder> implements DataListeners.addAddressListener {
    private ArrayList<Address> addresses;
    private Activity activity;

    public AddressPharmacyAdapter(ArrayList<Address> addresses, Activity activity) {
        this.addresses = addresses;
        this.activity = activity;
    }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address, parent, false);
        return new AddressViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, final int position) {
        final Address address = addresses.get(position);
        holder.updateUI(address);
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, EditAddressActivity.class).putExtra("address", address));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService.getInstance().deleteAddress(FirebaseAuth.getInstance().getCurrentUser().getUid(), address.getId(), position, AddressPharmacyAdapter.this);
            }
        });

        if (activity instanceof AddressSelectionActivity) {


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, PaymentActivity.class).putExtra("address", address));
                }
            });
        }
        else if (activity instanceof AddressSelectionMarketActivity){


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, PaymentMarketActivity.class).putExtra("address", address));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    @Override
    public void onAddressesReceived(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public void onAddressDeleted(boolean status, int position) {
        if (status) {
            addresses.remove(position);
            notifyDataSetChanged();
            Toast.makeText(activity, "Address Deleted Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onAddressEdited(boolean status) {

    }

}
