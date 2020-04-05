package com.amoharib.graduationproject.buyer.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.SplashActivity;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.User;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private CircleImageView circularProfileImage;
    private TextView accountName;
    private TextView userAddress;
    private Button logoutBtn;
    private TextView userPhoneNumber;

    private String uid;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(v);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadUserData();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Logout")
                        .setMessage("Are you sure to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                startActivity(new Intent(getActivity(), SplashActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        return v;
    }

    private void loadUserData() {
        DataService.getInstance().getUserById(uid, new DataListeners.UserListener() {
            @Override
            public void onDataReceived(User user) {
                String imageUrl = StringUtils.getImageUriForUser(user.getImage());
                System.out.println(imageUrl);
                Picasso.get().load(imageUrl).into(circularProfileImage);
                accountName.setText(user.getName());
                userPhoneNumber.setText(user.getPhone());
                loadUserAddress();
            }

        });
    }

    private void loadUserAddress() {
        DataService.getInstance().getAddresses(uid, new DataListeners.addAddressListener() {
            @Override
            public void onAddressesReceived(ArrayList<Address> addresses) {
                if (addresses.isEmpty()) {
                    userAddress.setText(R.string.no_address);
                } else {
                    Address address = addresses.get(0);
                    userAddress.setText(StringUtils.getFormattedAddress(address));
                }
            }

            @Override
            public void onAddressDeleted(boolean status, int position) {

            }

            @Override
            public void onAddressEdited(boolean status) {

            }
        });
    }


    private void initView(View v) {
        circularProfileImage = (CircleImageView) v.findViewById(R.id.circularProfileImage);
        accountName = (TextView) v.findViewById(R.id.account_name);
        userAddress = (TextView) v.findViewById(R.id.userAddress);
        logoutBtn = (Button) v.findViewById(R.id.logoutBtn);
        userPhoneNumber = (TextView) v.findViewById(R.id.userPhoneNumber);
    }
}
