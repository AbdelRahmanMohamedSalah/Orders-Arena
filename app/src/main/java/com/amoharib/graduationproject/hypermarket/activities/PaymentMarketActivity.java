package com.amoharib.graduationproject.hypermarket.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.MenuActivity;
import com.amoharib.graduationproject.buyer.activities.TrackOrderActivity;
import com.amoharib.graduationproject.buyer.adapters.BillAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.buyer.adapters.BillAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class PaymentMarketActivity extends AppCompatActivity {
    private Context activity;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CLIENT_ID = "AZxhIip9Ff-dk4WHfUR9UJFc1EFDq64HPEeI05L776OiUXFeRFITrBjBjXUOxRB1f-Zv_j5qXruEeYQF";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CLIENT_ID);

    private Button paymentBtn;
    private Toolbar toolbar;
    private TextView totalPriceText;
    private RecyclerView billRecycler;
    private Address address;
    private TextView compressedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        address = getIntent().getParcelableExtra("address");

        initView();
        initActivityViews();


//        Intent intent = new Intent(this, PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
//        startService(intent);


        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startPaymentProcess();
                proceedToCheckout();
            }
        });
    }

    private void proceedToCheckout() {
        final String restId = MenuHyperMarketActivity.hypermarketID;


        DataService.getInstance().addMarketOrder(restId, FirebaseAuth.getInstance().getCurrentUser().getUid(), address.getId(), MenuHyperMarketActivity.cartItems, new DataListeners.OnOrderAdditionListener() {
            @Override
            public void onOrderAdded(String orderId, boolean status) {

                startActivity(new Intent(com.amoharib.graduationproject.hypermarket.activities.PaymentMarketActivity.this, TrackOrderMarketActivity.class)
                        .putExtra("hypermarket", restId)
                        .putExtra("orderMarketId", orderId)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                );
                finish();
            }
        });

    }

    private void initActivityViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        compressedAddress.setText(StringUtils.getFormattedAddress(address));

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        Double totalPrice = 0d;
        for (CartItem item : MenuHyperMarketActivity.cartItems) {
            totalPrice += item.getQuantity() * item.getSize().getPrice();
        }

        totalPriceText.setText("$" + decimalFormat.format(totalPrice));

        billRecycler.setLayoutManager(new LinearLayoutManager(this));
        billRecycler.setNestedScrollingEnabled(false);

        BillAdapter adapter = new BillAdapter(MenuHyperMarketActivity.cartItems,1);
        billRecycler.setAdapter(adapter);
    }

    private void initView() {
        paymentBtn = (Button) findViewById(R.id.paymentBtn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        totalPriceText = (TextView) findViewById(R.id.totalPriceText);
        billRecycler = (RecyclerView) findViewById(R.id.billRecycler);
        compressedAddress = (TextView) findViewById(R.id.compressed_address);
    }

    private void startPaymentProcess() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal("0.01"), "USD", "Bill Payment", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {

                        Toast.makeText(this, "We Have got your payment", Toast.LENGTH_LONG).show();
                        System.out.println(paymentConfirmation.getPayment().toJSONObject().toString(4));
                        System.out.println(paymentConfirmation.toJSONObject().toString(4));

                    } catch (JSONException e) {
                        Log.e("PaymentActivity", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("PaymentActivity", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "PaymentActivity",
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
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
