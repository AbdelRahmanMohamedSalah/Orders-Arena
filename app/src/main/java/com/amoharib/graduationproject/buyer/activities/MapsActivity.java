package com.amoharib.graduationproject.buyer.activities;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amoharib.graduationproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private LatLng mCenterLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.selection_map);
//        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (map != null) {
                    map.clear();
                }
                mCenterLatLng = map.getCameraPosition().target;

                try {
                    Location location = new Location("");
                    location.setLatitude(mCenterLatLng.latitude);
                    location.setLongitude(mCenterLatLng.longitude);
                    System.out.println("Lat : " + mCenterLatLng.latitude + " Lng : " + mCenterLatLng.longitude);

                } catch (Exception e) {

                }

            }
        });
    }
}
