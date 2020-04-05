package com.amoharib.graduationproject.seller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SellerLoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        initView();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLoginBtn(true);
                if (checkInputs()) {
                    String email = username.getText().toString().concat("@firebase.com");
                    firebaseAuth.signInWithEmailAndPassword(email, password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                updateLoginBtn(false);
                                Toast.makeText(SellerLoginActivity.this, "Wrong email or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerLoginActivity.this, SellerRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateLoginBtn(boolean isLogging) {
        if (isLogging) {
            loginProgress.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
            loginBtn.setText("");
        } else {
            loginProgress.setVisibility(View.GONE);
            loginBtn.setEnabled(true);
            loginBtn.setText(getResources().getString(R.string.login));
        }
    }

    private boolean checkInputs() {
        if (TextUtils.isEmpty(username.getText())) {
            username.setError("Required Field");
            return false;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Required Field");
            return false;
        }
        if (password.getText().length() < 6) {
            password.setError("The password should be more than 6 characters");
            return false;
        }
        return true;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    Intent intent = new Intent(SellerLoginActivity.this, SellerDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);


    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
