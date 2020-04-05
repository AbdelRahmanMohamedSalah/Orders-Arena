package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.seller.activities.SellerDashboardActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    private ProgressBar progressLoader;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView logoBg;
    private ImageView logoText;
    private ImageView cartLogo;
    private ImageView textBar;
    private ImageView slogan;
    private LottieAnimationView lottieAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Fresco.initialize(getApplicationContext());
        initView();
        btnListeners();
        initAnimations();

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void initAnimations() {
        Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        final Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        final Animation slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation fadeInAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeInAnimation.setDuration(1300);
        fadeInAnimation2.setDuration(2000);
        slideUpAnimation.setDuration(1000);


        logoBg.startAnimation(fadeInAnimation);
        logoBg.setVisibility(View.VISIBLE);
        logoText.startAnimation(slideUpAnimation);
        logoText.setVisibility(View.VISIBLE);
        cartLogo.startAnimation(fadeInAnimation2);
        cartLogo.setVisibility(View.VISIBLE);
        slideUpAnimation.setDuration(1000);
        textBar.startAnimation(slideUpAnimation);
        textBar.setVisibility(View.VISIBLE);

        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slideDownAnimation.setDuration(1200);
                slogan.startAnimation(slideDownAnimation);
                slogan.setVisibility(View.VISIBLE);
                slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //progressLoader.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in));
                        //progressLoader.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        checkAuth();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void checkAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Intent intent;
                    if (email.contains("firebase")) {
                        intent = new Intent(SplashActivity.this, SellerDashboardActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, CategoryActivity.class);
                    }
                    startActivity(intent);
                    finish();

                } else {
                    progressLoader.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                    finish();
                    //initInputAnimations();
                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    private void btnListeners() {
//        buyerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        sellerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, SellerLoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void initView() {
        progressLoader = (ProgressBar) findViewById(R.id.progress_loader);
        logoBg = (ImageView) findViewById(R.id.logo_bg);
        logoText = (ImageView) findViewById(R.id.logoText);
        cartLogo = (ImageView) findViewById(R.id.cart_logo);
        textBar = (ImageView) findViewById(R.id.text_bar);
        slogan = (ImageView) findViewById(R.id.slogan);

        progressLoader.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.splashAccentColor), PorterDuff.Mode.SRC_IN);
        lottieAnimation = (LottieAnimationView) findViewById(R.id.lottie_animation);


    }

//    private void initTextAnimation() {
//        Animation exitAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
//        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
//
//        AnimationSet animationSet = new AnimationSet(false);
//        animationSet.addAnimation(exitAnimation);
//        animationSet.addAnimation(fadeOutAnimation);
//        animationSet.setDuration(1000);
//
//        splashText.startAnimation(animationSet);
//        animationSet.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                splashText.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    private void initInputAnimations() {
//        Animation enterAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
//        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//
//
//        AnimationSet animationSet = new AnimationSet(false);
//        animationSet.addAnimation(enterAnimation);
//        animationSet.addAnimation(fadeAnimation);
//        animationSet.setDuration(1000);
//
//        selectionLayout.startAnimation(animationSet);
//        animationSet.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                initTextAnimation();
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        selectionLayout.setVisibility(View.VISIBLE);
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
