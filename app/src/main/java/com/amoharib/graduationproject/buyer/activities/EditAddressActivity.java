package com.amoharib.graduationproject.buyer.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EditAddressActivity extends AppCompatActivity {

    private TextInputEditText addressInput;
    private TextInputEditText districtInput;
    private TextInputEditText streetInput;
    private TextInputEditText houseInput;
    private TextInputEditText apartmentInput;
    private TextInputEditText phoneInput;
    private TextInputEditText deliveryInstructionInput;
    private Button saveAddressBtn;
    private Address address;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        address = getIntent().getParcelableExtra("address");
        initView();


        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddress();
            }
        });

    }

    private void editAddress() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        address.setAddressName(addressInput.getText().toString());
        address.setDistrictName(districtInput.getText().toString());
        address.setStreetNumber(streetInput.getText().toString());
        address.setHouseBuilding(houseInput.getText().toString());
        address.setApartmentOffice(apartmentInput.getText().toString());
        address.setPhone(phoneInput.getText().toString());
        address.setDeliveryInstructions(deliveryInstructionInput.getText().toString());

        if (TextUtils.isEmpty(addressInput.getText())) {
            addressInput.setError("Required Field");
            return;
        }
        if (TextUtils.isEmpty(streetInput.getText())) {
            streetInput.setError("Required Field");
            return;
        }
        if (TextUtils.isEmpty(houseInput.getText())) {
            houseInput.setError("Required Field");
            return;
        }
        if (TextUtils.isEmpty(apartmentInput.getText())) {
            apartmentInput.setError("Required Field");
            return;
        }
        if (TextUtils.isEmpty(phoneInput.getText())) {
            phoneInput.setError("Required Field");
            return;
        }

        DataService.getInstance().editAddress(uid, address, new DataListeners.addAddressListener() {
            @Override
            public void onAddressesReceived(ArrayList<Address> addresses) {

            }

            @Override
            public void onAddressDeleted(boolean status, int position) {

            }

            @Override
            public void onAddressEdited(boolean status) {
                if (status) {
                    Toast.makeText(EditAddressActivity.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    NavUtils.navigateUpFromSameTask(EditAddressActivity.this);
                } else {
                    Toast.makeText(EditAddressActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        addressInput = (TextInputEditText) findViewById(R.id.addressInput);
        districtInput = (TextInputEditText) findViewById(R.id.districtInput);
        streetInput = (TextInputEditText) findViewById(R.id.streetInput);
        houseInput = (TextInputEditText) findViewById(R.id.houseInput);
        apartmentInput = (TextInputEditText) findViewById(R.id.apartmentInput);
        phoneInput = (TextInputEditText) findViewById(R.id.phoneInput);
        deliveryInstructionInput = (TextInputEditText) findViewById(R.id.deliveryInstructionInput);
        saveAddressBtn = (Button) findViewById(R.id.save_address_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        addressInput.setText(address.getAddressName());
        districtInput.setText(address.getDistrictName());
        streetInput.setText(address.getStreetNumber());
        houseInput.setText(address.getHouseBuilding());
        apartmentInput.setText(address.getApartmentOffice());
        phoneInput.setText(address.getPhone());
        deliveryInstructionInput.setText(address.getDeliveryInstructions());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
