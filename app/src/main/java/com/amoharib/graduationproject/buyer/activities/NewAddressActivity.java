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
import com.google.firebase.auth.FirebaseUser;

import fr.ganfra.materialspinner.MaterialSpinner;

public class NewAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText addressInput;
    private TextInputEditText districtInput;
    private TextInputEditText streetInput;
    private TextInputEditText houseInput;
    private TextInputEditText apartmentInput;
    private TextInputEditText phoneInput;
    private TextInputEditText deliveryInstructionInput;
    private Button saveAddressBtn;
    private FirebaseUser user;
    private MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
    }

    private void saveAddress() {
        String uid = user.getUid();
        Address address = new Address(addressInput.getText().toString(),
                districtInput.getText().toString(),
                streetInput.getText().toString(),
                houseInput.getText().toString(),
                apartmentInput.getText().toString(),
                phoneInput.getText().toString(),
                deliveryInstructionInput.getText().toString());

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

        DataService.getInstance().addAddress(uid, address, new DataListeners.DataListener() {
            @Override
            public void onReceiveStatus(boolean status) {
                Toast.makeText(NewAddressActivity.this, String.valueOf(status), Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(NewAddressActivity.this);
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addressInput = (TextInputEditText) findViewById(R.id.addressInput);
        streetInput = (TextInputEditText) findViewById(R.id.streetInput);
        houseInput = (TextInputEditText) findViewById(R.id.houseInput);
        apartmentInput = (TextInputEditText) findViewById(R.id.apartmentInput);
        phoneInput = (TextInputEditText) findViewById(R.id.phoneInput);
        deliveryInstructionInput = (TextInputEditText) findViewById(R.id.deliveryInstructionInput);
        saveAddressBtn = (Button) findViewById(R.id.save_address_btn);
        districtInput = (TextInputEditText) findViewById(R.id.districtInput);
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
