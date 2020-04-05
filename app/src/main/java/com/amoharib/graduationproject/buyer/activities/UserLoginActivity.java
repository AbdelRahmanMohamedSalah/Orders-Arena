package com.amoharib.graduationproject.buyer.activities;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.User;
import com.amoharib.graduationproject.services.DataService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class UserLoginActivity extends AppCompatActivity {

    private final static String TAG = "UserLoginActivity";
    private final static int VERIFY_PHONE = 1;
    private final static int LOGIN_WITH_FACEBOOK = 0;

    private int currentState = LOGIN_WITH_FACEBOOK;

    private Button loginBtn;
    private CallbackManager mCallbackManager;
    private FirebaseAuth firebaseAuth;
    private AccessToken facebookAccessToken;
    private LoginManager loginManager;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneCallback;
    private PhoneAuthProvider phoneAuthProvider;
    private EditText phoneNumber;
    private String number = "";
    private Dialog dialog;
    private Button verifyBtn;
    private EditText codeEditText;
    private TextView phoneToVerify;
    private Button closeBtn;
    private ProgressDialog progressDialog;


    private String verificationId;

    private boolean isPhoneVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setupFacebook();
        setupFirebase();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentState) {
                    case LOGIN_WITH_FACEBOOK:
                        loginWithFacebook();
                        break;
                    case VERIFY_PHONE:
                        loginWithMobile();
                        break;
                }
            }
        });

    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        phoneAuthProvider = PhoneAuthProvider.getInstance();

        phoneCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid phone number.",
                            Snackbar.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verifyBtn.setEnabled(true);
                verificationId = s;

            }
        };


    }

    private void setupFacebook() {
        loginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookAccessToken = loginResult.getAccessToken();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(UserLoginActivity.this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(UserLoginActivity.this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginWithMobile() {
        if (TextUtils.isEmpty(phoneNumber.getText())) {
            phoneNumber.setError("Required Field");
            return;
        }
        if (phoneNumber.getText().length() < 11) {
            phoneNumber.setError("Not a valid number");
            return;
        }

        number = "+2" + phoneNumber.getText().toString();
        phoneAuthProvider.verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                phoneCallback
        );

        openDialog();
    }

    private void openDialog() {

        phoneToVerify.setText(number);
        codeEditText.setText("");
        dialog.show();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoneCodeVerification();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void startPhoneCodeVerification() {
        if (TextUtils.isEmpty(codeEditText.getText())) {
            codeEditText.setError("Required Field");
            return;
        }

        progressDialog.setMessage("Verifying Code...");
        progressDialog.show();

        String code = codeEditText.getText().toString();
        checkCodeValidation(code);
    }

    private void checkCodeValidation(String code) {
        AuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(phoneAuthCredential);
    }

    private void signInWithCredential(AuthCredential authCredential) {
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        isPhoneVerified = false;
                        Snackbar.make(findViewById(android.R.id.content), "Invalid code.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    isPhoneVerified = true;
                    progressDialog.setMessage("Verifying Information...");
                    Snackbar.make(findViewById(android.R.id.content), "Verified", Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                    task.getResult().getUser().delete();
                    firebaseAuth.signOut();
                    handleFacebookAccessToken(facebookAccessToken);
                }
            }
        });
    }

    private void loginWithFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            loginManager.logInWithReadPermissions(UserLoginActivity.this, Arrays.asList("email", "public_profile"));
        } else {
            handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                System.out.println("User status : " + task.getResult().getAdditionalUserInfo().isNewUser());
                if (!task.isSuccessful()) {
                    Toast.makeText(UserLoginActivity.this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show();
                } else if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                    if (isPhoneVerified) {
                        User newUser = new User(firebaseUser.getUid(),
                                firebaseUser.getDisplayName(),
                                firebaseUser.getEmail(),
                                firebaseUser.getPhotoUrl().toString(),
                                number);

                        System.out.println("Registering User");
                        DataService.getInstance().addNewUser(newUser, new DataListeners.UserListener() {
                            @Override
                            public void onDataReceived(User user) {
                                if (user != null) {
                                    gotoMainActivity();
                                } else {
                                    System.out.println("Failed");
                                }
                            }
                        });

                    } else {
                        task.getResult().getUser().delete();
                        LoginManager.getInstance().logOut();
                        firebaseAuth.signOut();
                        showPhoneEditText();
                    }

                } else {
                    gotoMainActivity();
                }
            }
        });
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    private void gotoMainActivity() {
        dialog.dismiss();
        Intent intent = new Intent(UserLoginActivity.this, CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        loginBtn = (Button) findViewById(R.id.login_button);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        phoneNumber.setVisibility(View.INVISIBLE);
        initDialog();
        initProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.phone_verify_dialog_layout);

        codeEditText = (EditText) dialog.findViewById(R.id.code);
        verifyBtn = (Button) dialog.findViewById(R.id.verifyBtn);
        closeBtn = (Button) dialog.findViewById(R.id.closeBtn);
        phoneToVerify = (TextView) dialog.findViewById(R.id.enteredPhoneNumber);
    }

    private void updateSubmitBtnUI(int STATE) {
        switch (STATE) {
            case LOGIN_WITH_FACEBOOK:
                loginBtn.setText(R.string.continue_with_facebook);
                break;
            case VERIFY_PHONE:
                loginBtn.setText(R.string.verify_phone);
                break;
        }
        currentState = STATE;
    }

    private void showPhoneEditText() {
        TranslateAnimation enterAnimation = new TranslateAnimation(
                phoneNumber.getWidth(),
                0,
                0,
                0
        );
        Animation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setInterpolator(new DecelerateInterpolator());
        fadeInAnimation.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(enterAnimation);
        animationSet.addAnimation(fadeInAnimation);
        animationSet.setDuration(1000);

        phoneNumber.startAnimation(animationSet);
        phoneNumber.setVisibility(View.VISIBLE);
        updateSubmitBtnUI(VERIFY_PHONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            gotoMainActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
