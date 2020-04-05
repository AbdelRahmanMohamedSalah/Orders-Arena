package com.amoharib.graduationproject.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        //sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DataService.getInstance().addTokenToUser(userId, token);
    }
}
