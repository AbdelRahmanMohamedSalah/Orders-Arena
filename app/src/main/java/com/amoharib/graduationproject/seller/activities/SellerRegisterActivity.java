package com.amoharib.graduationproject.seller.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.services.DataService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;
import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;

public class SellerRegisterActivity extends AppCompatActivity {

    private ImageView logoImage;
    private TextInputEditText restName;
    private TextInputEditText restUsername;
    private TextInputEditText restPassword;
    private TextInputEditText restConfirmPassword;
    private TextInputEditText restDescription;
    private Button submitBtn;

    private ProgressBar logoProgress;

    private Uri logoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        initView();


        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.build(new DialogConfiguration()
                                .setTitle("Choose Your Image")
                                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL),
                        new ImageResultListener() {
                            @Override
                            public void onImageResult(ImageResult imageResult) {
                                logoImage.setImageBitmap(imageResult.getBitmap());
                                logoUri = imageResult.getUri();
                            }
                        }).show(getSupportFragmentManager());
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputs()) {
                    String username = restUsername.getText().toString().trim().toLowerCase();
                    String password = restPassword.getText().toString();
                    DataService.getInstance().registerNewRestaurant(username, password, new DataListeners.RestaurantRegistrationListener() {
                        @Override
                        public void onRestaurantRegistered(FirebaseUser firebaseUser) {
                            if (firebaseUser != null) {
                                String uid = restUsername.getText().toString();
                                uploadLogo(uid, firebaseUser.getIdToken(true).toString());
                            }
                        }
                    });
                }
            }
        });
    }

    private void uploadLogo(final String uid, final String token) {
        final String location = String.format("%s/logo.jpg", uid);
        logoProgress.setVisibility(View.VISIBLE);
        submitBtn.setEnabled(false);
        DataService.getInstance().uploadImage(location, logoUri, new DataListeners.UploadImageListener() {
            @Override
            public void onImageUploadProgress(int progress) {
                logoProgress.setProgress(progress);
            }

            @Override
            public void onImageUploaded(UploadTask.TaskSnapshot taskSnapshot) {
                String imageUri = taskSnapshot.getDownloadUrl().toString();
                Restaurant restaurant = new Restaurant(restName.getText().toString(),
                        restDescription.getText().toString(),
                        imageUri,
                        uid,
                        token);

                DataService.getInstance().addRestaurantToDB(restaurant, new DataListeners.DataListener() {
                    @Override
                    public void onReceiveStatus(boolean status) {
                        if (status) {
                            Toast.makeText(SellerRegisterActivity.this, "Restaurant Registered Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SellerRegisterActivity.this, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                            submitBtn.setEnabled(true);
                        }
                    }
                });
            }

            @Override
            public void onImageUploadFailed(Exception ex) {
                Toast.makeText(SellerRegisterActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                submitBtn.setEnabled(true);
                logoProgress.setProgress(0);
            }
        });
    }

    private boolean checkInputs() {
        if (logoUri == null) {
            Toast.makeText(this, "Please Choose Your Logo", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(restName.getText())) {
            restName.setError("Required Field");
            return false;
        }
        if (TextUtils.isEmpty(restUsername.getText())) {
            restUsername.setError("Required Field");
            return false;
        }
        if (TextUtils.isEmpty(restPassword.getText())) {
            restPassword.setError("Required Field");
            return false;
        }
        if (restPassword.getText().length() < 6) {
            restPassword.setError("Your password must be more than 6 characters");
            return false;
        }
        if (!TextUtils.equals(restConfirmPassword.getText(), restPassword.getText())) {
            restConfirmPassword.setError("Passwords not matched");
            return false;
        }
        if (TextUtils.isEmpty(restDescription.getText())) {
            restDescription.setError("Required Field");
            return false;
        }
        return true;
    }

    private void initView() {
        logoImage = (ImageView) findViewById(R.id.logoImage);
        restName = (TextInputEditText) findViewById(R.id.restName);
        restUsername = (TextInputEditText) findViewById(R.id.restUsername);
        restPassword = (TextInputEditText) findViewById(R.id.restPassword);
        restConfirmPassword = (TextInputEditText) findViewById(R.id.restConfirmPassword);
        restDescription = (TextInputEditText) findViewById(R.id.restDescription);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        logoProgress = (ProgressBar) findViewById(R.id.logoProgress);
    }
}
