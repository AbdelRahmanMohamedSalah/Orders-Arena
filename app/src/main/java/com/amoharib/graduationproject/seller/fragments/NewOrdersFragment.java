package com.amoharib.graduationproject.seller.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.seller.adapters.OrdersAdapter;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrdersFragment extends Fragment implements DataListeners.OnOrderListener {


    private ProgressBar progressBar;
    private RecyclerView newOrdersRecycler;
    private OrdersAdapter ordersAdapter;
    private String uid;

    public NewOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_orders, container, false);
        initView(v);

        ordersAdapter = new OrdersAdapter(new ArrayList<Order>(), this);
        newOrdersRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        newOrdersRecycler.setAdapter(ordersAdapter);
        uid = StringUtils.getUsernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        return v;
    }

    private void initView(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        newOrdersRecycler = (RecyclerView) v.findViewById(R.id.newOrdersRecycler);
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        newOrdersRecycler.setVisibility(View.INVISIBLE);
        DataService.getInstance().getRestaurantOrders(uid, this);
    }

    @Override
    public void onDataReceived(ArrayList<Order> orders) {
        ordersAdapter.orders = orders;
        ordersAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        newOrdersRecycler.setVisibility(View.VISIBLE);
    }
}
