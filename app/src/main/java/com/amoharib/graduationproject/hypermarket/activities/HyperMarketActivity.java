package com.amoharib.graduationproject.hypermarket.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.fragments.OrdersFragment;
import com.amoharib.graduationproject.buyer.fragments.ProfileFragment;
import com.amoharib.graduationproject.buyer.fragments.RestaurantsFragment;
import com.amoharib.graduationproject.hypermarket.fragments.HyperMarketOrdersFragment;
import com.amoharib.graduationproject.hypermarket.fragments.HyperMarketProfileFragment;
import com.amoharib.graduationproject.hypermarket.fragments.HyperMarketsFragment;
import com.amoharib.graduationproject.services.DataService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

public class HyperMarketActivity extends AppCompatActivity {

    private HyperMarketsFragment hyperMarketsFragment;
    private HyperMarketOrdersFragment hyperMarketOrdersFragment;
    private HyperMarketProfileFragment hyperMarketProfileFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    setFragment(hyperMarketsFragment);
                    return true;
                case R.id.nav_profile:
                    setFragment(hyperMarketProfileFragment);
                    return true;
                case R.id.nav_orders:
                    setFragment(hyperMarketOrdersFragment);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyper_market);

        String token = FirebaseInstanceId.getInstance().getToken();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DataService.getInstance().addTokenToUser(userId, token);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_hyper_market);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        hyperMarketsFragment=new HyperMarketsFragment();
        hyperMarketProfileFragment = new HyperMarketProfileFragment();
        hyperMarketOrdersFragment = new HyperMarketOrdersFragment();

        navigation.setSelectedItemId(R.id.nav_home);
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_hyper_market, fragment).commit();
    }
}
