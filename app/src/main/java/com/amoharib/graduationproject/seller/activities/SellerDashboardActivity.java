package com.amoharib.graduationproject.seller.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.SplashActivity;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.seller.fragments.ItemFragment;
import com.amoharib.graduationproject.seller.fragments.NewOrdersFragment;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class SellerDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NEW_ORDERS = "New Orders";
    private static final String ITEMS = "Items";

    private View headerView;
    private ImageView logoImage;
    private TextView restName;
    private Restaurant mRestaurant;
    private FrameLayout fragmentContainer;

    private NewOrdersFragment newOrdersFragment;
    private ItemFragment itemFragment;

    private NavigationView navigationView;

    private TextView ordersCounterNotifier;
    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarText = (TextView) findViewById(R.id.toolbar_text);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);


        String restId = StringUtils.getUsernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DataService.getInstance().getRestaurant(restId, new DataListeners.RestaurantListener() {
            @Override
            public void onRestaurantRetrieved(Restaurant restaurant) {
                mRestaurant = restaurant;
                populateViews();
                addOnNewOrderReceivedListener();
            }
        });

        newOrdersFragment = new NewOrdersFragment();
        itemFragment = new ItemFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, newOrdersFragment).commit();

    }

    private void addOnNewOrderReceivedListener() {
        DataService.getInstance().getOrderCountForRestaurant(mRestaurant.getId(), new DataListeners.NewOrderNotificationListener() {
            @Override
            public void onOrderAdded(long size) {
                ordersCounterNotifier.setText(String.valueOf(size));
            }
        });
    }

    private void populateViews() {
        logoImage = (ImageView) headerView.findViewById(R.id.restLogo);
        restName = (TextView) headerView.findViewById(R.id.restName);
        ordersCounterNotifier = (TextView) navigationView.getMenu().findItem(R.id.nav_new_orders).getActionView();
        ordersCounterNotifier.setTextColor(getResources().getColor(R.color.colorAccent));
        ordersCounterNotifier.setTypeface(ResourcesCompat.getFont(this, R.font.sub_title_font));
        ordersCounterNotifier.setGravity(Gravity.CENTER_VERTICAL);
        ordersCounterNotifier.setText("0");

        Picasso.get().load(mRestaurant.getLogo()).into(logoImage);
        restName.setText(mRestaurant.getTitle());

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_orders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newOrdersFragment).commit();
            toolbarText.setText(NEW_ORDERS);
        } else if (id == R.id.nav_items) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, itemFragment).commit();
            toolbarText.setText(ITEMS);
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
