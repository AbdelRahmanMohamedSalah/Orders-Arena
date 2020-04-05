package com.amoharib.graduationproject.buyer.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoharib.graduationproject.buyer.activities.AddressSelectionActivity;
import com.amoharib.graduationproject.buyer.activities.EditAddressActivity;
import com.amoharib.graduationproject.buyer.activities.PaymentActivity;
import com.amoharib.graduationproject.hypermarket.activities.AddressSelectionMarketActivity;
import com.amoharib.graduationproject.hypermarket.activities.PaymentMarketActivity;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.buyer.viewholders.AddressViewHolder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;



public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> implements DataListeners.addAddressListener {


    private ArrayList<Address> addresses;
    private Activity activity;

    public AddressAdapter(ArrayList<Address> addresses, Activity activity) {
        this.addresses = addresses;
        this.activity = activity;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address, parent, false);
        return new AddressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, final int position) {
        final Address address = addresses.get(position);
        holder.updateUI(address);

    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }


    @Override
    public void onAddressesReceived(ArrayList<Address> addresses) {

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
