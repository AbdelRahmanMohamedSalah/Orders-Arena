package com.amoharib.graduationproject.Pharmacy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.Pharmacy.adapters.MenuPharmAdapter;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.AllPharmacy;
import com.amoharib.graduationproject.models.PharmacyItem;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StaticConfig;
import com.andremion.counterfab.CounterFab;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MenuPharmacy extends AppCompatActivity {
    private AllPharmacy pharmacy;
    private TextView phrmacyTitle, pharmacyDesc;
    private SimpleDraweeView pharmacyLogo;
    String PharmId;
    PharmacyItem pharmacyItem;
    private SectionedRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    CounterFab floatingActionButton;
    public static DataListeners.addCard addCardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pharmacy);
        phrmacyTitle = (TextView) findViewById(R.id.phamTitle);
        pharmacyDesc = (TextView) findViewById(R.id.pharmDesc);
        floatingActionButton=(CounterFab) findViewById(R.id.cartFabParm);
        pharmacyLogo = (SimpleDraweeView) findViewById(R.id.phartLogo);
        adapter = new SectionedRecyclerViewAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.treatmentRecycler);
        StaticConfig.CART_ITEMS.clear();
        Intent intent = getIntent();
        pharmacy = (AllPharmacy) intent.getSerializableExtra("pharmacy");
        phrmacyTitle.setText(pharmacy.getTitle());
        Picasso.get().load(pharmacy.getLogo()).into(pharmacyLogo);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        pharmacyDesc.setText(pharmacy.getDescription());
        PharmId = pharmacy.getId();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        DataService.getInstance().getMenuForPharmacyId(PharmId, new DataListeners.OnMenuPharmacyListener() {
            @Override
            public void onMenuPharmacyRetrieved(ArrayList<PharmacyItem> pharmacyItems, String category) {
                //   adapter.addSection(new MenuAdapter(pharmacyItems,pharmacyItems,category,MenuPharmacy.this))
                adapter.addSection(new MenuPharmAdapter(pharmacyItems, pharmacyItems, category, MenuPharmacy.this));
                updateAdapter();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StaticConfig.CART_ITEMS.isEmpty()){
                    Toast.makeText(MenuPharmacy.this, "your Card impty", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(MenuPharmacy.this,OrderPharmacy.class));
                }


            }
        });

        addCardListener = new DataListeners.addCard() {
            @Override
            public void setCount(int count) {
                floatingActionButton.setCount(count);
            }
        };
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

}
