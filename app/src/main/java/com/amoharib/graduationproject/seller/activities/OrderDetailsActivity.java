package com.amoharib.graduationproject.seller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.adapters.BillAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.models.User;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.OrderStatus;
import com.amoharib.graduationproject.utils.StringUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

public class OrderDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView userName;
    private TextView totalPriceText;
    private TextView orderTime;
    private RecyclerView billRecycler;
    private TextView compressedAddress;
    private Button refuseBtn;
    private Button acceptBtn;
    private ShimmerFrameLayout userNameShimmer;
    private View userNameOverlay;
    private ShimmerFrameLayout userPhoneShimmer;
    private View userPhoneOverlay;
    private TextView userPhoneNumber;


    private Order order;

    private BillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        order = getIntent().getParcelableExtra("order");

        populateViews();
        updateOrderStatus(OrderStatus.SEEN);
    }

    private void updateOrderStatus(final OrderStatus orderStatus) {
        DataService.getInstance().updateOrderStatus(order, orderStatus, new DataListeners.DataListener() {
            @Override
            public void onReceiveStatus(boolean status) {
                if (orderStatus == OrderStatus.OUT) {
                    onBackPressed();
                }
            }
        });
    }

    private void populateViews() {
        initUserDetails();
        populateOrderList();
    }

    private void populateOrderList() {
        adapter = new BillAdapter(order.getItems(),0);
        billRecycler.setLayoutManager(new LinearLayoutManager(this));
        billRecycler.setAdapter(adapter);
    }

    private void initUserDetails() {
        userNameShimmer.startShimmerAnimation();
        userPhoneShimmer.startShimmerAnimation();

        DataService.getInstance().getUserById(order.getUserId(), new DataListeners.UserListener() {
            @Override
            public void onDataReceived(User user) {
                userName.setText(user.getName());
                userPhoneNumber.setText(user.getPhone());

                userNameOverlay.setVisibility(View.GONE);
                userNameShimmer.stopShimmerAnimation();
                userPhoneOverlay.setVisibility(View.GONE);
                userPhoneShimmer.stopShimmerAnimation();
            }
        });

        DataService.getInstance().getAddressByUid(order.getUserId(), order.getUserAddressId(), new DataListeners.UserAddressListener() {
            @Override
            public void onDataReceived(Address address) {
                compressedAddress.setText(StringUtils.getFormattedAddress(address));
            }
        });

        orderTime.setText(StringUtils.getFormattedDate(order.getTimestamp()));

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(OrderStatus.OUT);
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        userName = (TextView) findViewById(R.id.userName);
        totalPriceText = (TextView) findViewById(R.id.totalPriceText);
        orderTime = (TextView) findViewById(R.id.orderTime);
        billRecycler = (RecyclerView) findViewById(R.id.billRecycler);
        compressedAddress = (TextView) findViewById(R.id.compressed_address);
        refuseBtn = (Button) findViewById(R.id.refuseBtn);
        acceptBtn = (Button) findViewById(R.id.acceptBtn);
        userNameShimmer = (ShimmerFrameLayout) findViewById(R.id.userNameShimmer);
        userNameOverlay = (View) findViewById(R.id.userNameOverlay);
        userPhoneShimmer = (ShimmerFrameLayout) findViewById(R.id.userPhoneShimmer);
        userPhoneOverlay = (View) findViewById(R.id.userPhoneOverlay);
        userPhoneNumber = (TextView) findViewById(R.id.userPhoneNumber);
    }
}
