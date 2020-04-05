package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.adapters.AddressAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.services.DataService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddressSelectionActivity extends AppCompatActivity implements DataListeners.addAddressListener {

    private RecyclerView addressRecycler;
    private Button addNewAddressBtn;

    public ArrayList<Address> addresses = new ArrayList<>();
    private AddressAdapter adapter;
    private Toolbar toolbar;
    private Button selectAddressFromMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new AddressAdapter(addresses, this);
        addressRecycler.setLayoutManager(new LinearLayoutManager(this));
        addressRecycler.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DataService.getInstance().getAddresses(uid, this);


        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressSelectionActivity.this, NewAddressActivity.class));
            }
        });

        selectAddressFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressSelectionActivity.this, MapsActivity.class));
            }
        });
    }

    private void initView() {
        addressRecycler = (RecyclerView) findViewById(R.id.addressRecycler);
        addNewAddressBtn = (Button) findViewById(R.id.add_new_address_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        selectAddressFromMap = (Button) findViewById(R.id.selectAddressFromMap);
    }

    @Override
    public void onAddressesReceived(ArrayList<Address> addresses) {
        if (addresses != null) {
            adapter.setAddresses(addresses);
            adapter.notifyDataSetChanged();

        } else {
            Toast.makeText(this, "Some Error Has occurred, Please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddressDeleted(boolean status, int position) {

    }

    @Override
    public void onAddressEdited(boolean status) {

    }
}
