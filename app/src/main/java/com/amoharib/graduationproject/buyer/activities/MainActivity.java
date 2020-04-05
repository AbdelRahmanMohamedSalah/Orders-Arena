package com.amoharib.graduationproject.buyer.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.amoharib.graduationproject.buyer.fragments.OrdersFragment;
import com.amoharib.graduationproject.buyer.fragments.ProfileFragment;
import com.amoharib.graduationproject.buyer.fragments.RestaurantsFragment;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private RestaurantsFragment restaurantsFragment;
    private OrdersFragment ordersFragment;
    private ProfileFragment profileFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_orders:
                    setFragment(ordersFragment);
                    return true;
                case R.id.nav_home:
                    setFragment(restaurantsFragment);
                    return true;
                case R.id.nav_profile:
                    setFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String token = FirebaseInstanceId.getInstance().getToken();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DataService.getInstance().addTokenToUser(userId, token);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        restaurantsFragment = new RestaurantsFragment();
        profileFragment = new ProfileFragment();
        ordersFragment = new OrdersFragment();

        navigation.setSelectedItemId(R.id.nav_home);
    }


    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

}
