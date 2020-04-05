package com.amoharib.graduationproject.buyer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.OrderStatus;
import com.amoharib.graduationproject.utils.VectorDrawableUtils;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.firebase.geofire.GeoFire;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrackOrderActivity extends AppCompatActivity implements DataListeners.OrderStatusListener, OnMapReadyCallback {


    private TextView restName;
    private TimelineView sentTimeLine;
    private TimelineView seenTimeLine;
    private TimelineView onTheWayTimeLine;
    private CircleProgressBar timer;
    private TextView timerText;
    private String restaurantId;
    private String orderId;
    private Restaurant rest;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        restaurantId = getIntent().getStringExtra("restaurant");
        orderId = getIntent().getStringExtra("orderId");

        initView();

        DataService.getInstance().getRestaurant(restaurantId, new DataListeners.RestaurantListener() {
            @Override
            public void onRestaurantRetrieved(Restaurant restaurant) {
                if (restaurant != null) {
                    rest = restaurant;
                    initCustomViews();

                    new CountDownTimer(45 * 60000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            timerText.setText(timeFormat.format(new Date(millisUntilFinished)));
                            Integer timeLeft = 100 - ((int) ((double) millisUntilFinished / ((double) 45 * 60000) * 100));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timer.setProgress(timeLeft, true);
                            } else {
                                timer.setProgress(timeLeft);
                            }
                        }

                        @Override
                        public void onFinish() {
                            timerText.setText(timeFormat.format(new Date(0)));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timer.setProgress(100, true);
                            } else {
                                timer.setProgress(100);
                            }

                        }
                    }.start();
                }
            }
        });


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private void initCustomViews() {
        restName.setText(rest.getTitle());
        DataService.getInstance().addOnOrderStatusChanged(orderId, this);
    }

    private void initView() {
        restName = (TextView) findViewById(R.id.restName);
        sentTimeLine = (TimelineView) findViewById(R.id.sentTimeLine);
        seenTimeLine = (TimelineView) findViewById(R.id.seenTimeLine);
        onTheWayTimeLine = (TimelineView) findViewById(R.id.onTheWayTimeLine);
        timer = (CircleProgressBar) findViewById(R.id.timer);
        timerText = (TextView) findViewById(R.id.timerText);


        sentTimeLine.initLine(TimelineView.getTimeLineViewType(0, 3));
        onTheWayTimeLine.initLine(TimelineView.getTimeLineViewType(2, 3));

    }

    @Override
    public void onStatusChanged(OrderStatus status) {
        changeStatus(status);
    }

    private void changeStatus(OrderStatus status) {
        switch (status) {
            case SENT:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_dotted_circle));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_empty_circle));
                break;
            case SEEN:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_dotted_circle));
                break;
            case OUT:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                getDriverLocation();
                break;
        }

        refreshStatusView();
    }

    private void refreshStatusView() {
        sentTimeLine.invalidate();
        seenTimeLine.invalidate();
        onTheWayTimeLine.invalidate();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        mMap.setMinZoomPreference(2.0f);
        enableCurrentLocation();

    }

    private void enableCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableCurrentLocation();
            }
        }
    }

    Marker driverMarker;

    boolean first = true;

    private void getDriverLocation() {

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("order");
        mRef.child(orderId).child("captainId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("captain/" + dataSnapshot.getValue(String.class) + "/current-location/l");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double latitude = 0.0;
                        double longitude = 0.0;

                        if (driverMarker != null) driverMarker.remove();

                        List<Double> location = (List<Double>) dataSnapshot.getValue();

                        latitude = location.get(0);
                        longitude = location.get(1);

                        LatLng driverLocation = new LatLng(latitude, longitude);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(driverLocation);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));

                        driverMarker = mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(driverLocation));
                        if (first) {
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                            first = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
