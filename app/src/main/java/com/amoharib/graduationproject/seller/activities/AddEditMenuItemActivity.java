package com.amoharib.graduationproject.seller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.Size;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;
import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AddEditMenuItemActivity extends AppCompatActivity {

    private static final String ADD_NEW_CATEGORY = "Add New Category";

    private ImageView itemImage;
    private AppCompatSpinner itemCategory;
    private EditText newCategoryName;
    private EditText itemName;
    private EditText itemDescription;
    private ImageView addNewSizeBtn;
    private LinearLayout itemPricesContainer;
    private Button submitBtn;
    private ProgressBar uploadImageProgress;
    private ProgressBar btnProgress;

    private String[] sizesArray;

    private Typeface typeface;

    private ArrayList<Size> sizes = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private String restId;

    private boolean isNewCategorySelected = false;

    private String imageUri = "";
    private Food food;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_menu_item);
        sizesArray = getResources().getStringArray(R.array.item_sizes);
        typeface = ResourcesCompat.getFont(this, R.font.sub_title_font);
        restId = StringUtils.getUsernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        initView();
        initCategories();

        food = (Food) getIntent().getSerializableExtra("food");
        category = getIntent().getStringExtra("category");


        if (food != null) {
            fillDataWithFood();
        }

        addNewSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifySizesContainer();
            }
        });

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

    }

    private void fillDataWithFood() {
        Picasso.get().load(food.getIcon()).into(itemImage);
        itemName.setText(food.getName());
        itemDescription.setText(food.getDescription());

        for (Size size : food.getSizes()) {
            View v = modifySizesContainer();
            AppCompatSpinner sizesSpinner = (AppCompatSpinner) v.findViewById(R.id.size_spinner);
            EditText editText = (EditText) v.findViewById(R.id.item_size);


            int position = Arrays.asList(sizesArray).indexOf(size.getSize());
            sizesSpinner.setSelection(position, true);
            editText.setText(String.valueOf(size.getPrice()));

        }
    }

    private void uploadData() {
        if (checkInputs()) {
            btnProgress.setVisibility(View.VISIBLE);
            submitBtn.setText("");

            Food food = new Food(
                    itemName.getText().toString(),
                    itemDescription.getText().toString(),
                    imageUri,
                    sizes
            );
            String selectedCategory = isNewCategorySelected ? newCategoryName.getText().toString() : itemCategory.getSelectedItem().toString();
            DataService.getInstance().uploadFoodItem(restId, selectedCategory, isNewCategorySelected, categories.size(), food, new DataListeners.DataListener() {
                @Override
                public void onReceiveStatus(boolean status) {
                    if (status) {
                        Toast.makeText(AddEditMenuItemActivity.this, "Item Added Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        btnProgress.setVisibility(View.GONE);
                        submitBtn.setText(R.string.submit);
                    }
                }
            }, new DataListeners.UploadImageListener() {
                @Override
                public void onImageUploadProgress(int progress) {
                    uploadImageProgress.setProgress(progress);
                }

                @Override
                public void onImageUploaded(UploadTask.TaskSnapshot taskSnapshot) {

                }

                @Override
                public void onImageUploadFailed(Exception ex) {
                    btnProgress.setVisibility(View.GONE);
                    submitBtn.setText(R.string.submit);
                }
            });
        }
    }

    private void chooseImage() {
        ImagePicker.build(
                new DialogConfiguration()
                        .setTitle("Choose an Image")
                        .setOptionOrientation(LinearLayoutCompat.HORIZONTAL),
                new ImageResultListener() {
                    @Override
                    public void onImageResult(ImageResult imageResult) {
                        itemImage.setImageBitmap(imageResult.getBitmap());
                        imageUri = imageResult.getUri().toString();
                    }
                }
        ).show(getSupportFragmentManager());
    }

    private void initCategories() {
        DataService.getInstance().getCategoriesForRestaurant(restId, new DataListeners.OnCategoryListener() {
            @Override
            public void onCategoriesRetrieved(final ArrayList<String> categories) {
                AddEditMenuItemActivity.this.categories = categories;
                categories.add(ADD_NEW_CATEGORY);
                categories.add("Select item category");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        AddEditMenuItemActivity.this,
                        android.R.layout.simple_spinner_item,
                        categories
                ) {
                    @Override
                    public int getCount() {
                        return categories.size() - 1;
                    }

                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent).findViewById(android.R.id.text1);
                        textView.setTypeface(typeface);
                        textView.setTextSize(14);
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        if (position == getCount() || position == getCount() - 1) {
                            textView.setTextColor(Color.GRAY);
                        } else {
                            textView.setTextColor(Color.WHITE);
                        }
                        return textView;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemCategory.setAdapter(adapter);
                itemCategory.setSelection(category != null ? categories.indexOf(category) : adapter.getCount());

                itemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == adapter.getCount() - 1) {
                            newCategoryName.setVisibility(View.VISIBLE);
                            isNewCategorySelected = true;
                        } else {
                            newCategoryName.setVisibility(View.GONE);
                            isNewCategorySelected = false;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCategoriesNotFound() {

            }
        });

    }

    private boolean checkInputs() {
        sizes.clear();

        if (itemCategory.getSelectedItemPosition() == itemCategory.getAdapter().getCount()) {
            Snackbar.make(findViewById(android.R.id.content), "Please Select Item Category", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (itemCategory.getSelectedItemPosition() == itemCategory.getAdapter().getCount() - 1) {
            if (TextUtils.isEmpty(newCategoryName.getText())) {
                Snackbar.make(findViewById(android.R.id.content), "Please Enter Item Category Name", Snackbar.LENGTH_LONG).show();
                return false;
            } else if (categories.contains(newCategoryName.getText().toString())) {
                Snackbar.make(findViewById(android.R.id.content), "This Category is already exist", Snackbar.LENGTH_LONG).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(itemName.getText())) {
            itemName.setError("Please Enter the Item Name");
            return false;
        }
        if (itemPricesContainer.getChildCount() == 0) {
            Snackbar.make(findViewById(android.R.id.content), "Please select at least one size", Snackbar.LENGTH_LONG).show();
            return false;
        }

        sizes.clear();

        for (int i = 0; i < itemPricesContainer.getChildCount(); i++) {
            View v = itemPricesContainer.getChildAt(i);
            EditText editText = (EditText) v.findViewById(R.id.item_size);
            AppCompatSpinner spinner = (AppCompatSpinner) v.findViewById(R.id.size_spinner);

            if (spinner.getSelectedItemPosition() == spinner.getAdapter().getCount()) {
                Snackbar.make(findViewById(android.R.id.content), "Please Select a correct size", Snackbar.LENGTH_LONG).show();
                return false;
            } else if (TextUtils.isEmpty(editText.getText())) {
                editText.setError("Please enter a price");
                return false;
            }
            Size size = new Size(spinner.getSelectedItem().toString(), Double.valueOf(editText.getText().toString()));
            sizes.add(size);
        }
        return true;
    }

    private View modifySizesContainer() {
        if (itemPricesContainer.getVisibility() == View.INVISIBLE)
            itemPricesContainer.setVisibility(View.VISIBLE);

        final View newSizeView = LayoutInflater.from(AddEditMenuItemActivity.this).inflate(R.layout.item_size, itemPricesContainer, false);
        AppCompatSpinner sizesSpinner = (AppCompatSpinner) newSizeView.findViewById(R.id.size_spinner);
        final ImageView removeBtn = (ImageView) newSizeView.findViewById(R.id.remove_size);

        ArrayAdapter adapter = new ArrayAdapter<String>(
                AddEditMenuItemActivity.this,
                android.R.layout.simple_spinner_item,
                sizesArray) {
            @Override
            public int getCount() {
                return sizesArray.length - 1;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent).findViewById(android.R.id.text1);
                textView.setTypeface(typeface);
                textView.setTextSize(12);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                if (position == getCount()) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.WHITE);
                }
                return textView;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizesSpinner.setAdapter(adapter);
        sizesSpinner.setSelection(adapter.getCount());
        itemPricesContainer.addView(newSizeView);

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPricesContainer.removeView(newSizeView);
                itemPricesContainer.invalidate();
                if (itemPricesContainer.getChildCount() == 0)
                    itemPricesContainer.setVisibility(View.INVISIBLE);
            }
        });

        return newSizeView;
    }

    private void initView() {
        itemImage = (ImageView) findViewById(R.id.item_image);
        itemCategory = (AppCompatSpinner) findViewById(R.id.item_category);
        itemName = (EditText) findViewById(R.id.item_name);
        itemDescription = (EditText) findViewById(R.id.item_description);
        addNewSizeBtn = (ImageView) findViewById(R.id.add_new_size_btn);
        itemPricesContainer = (LinearLayout) findViewById(R.id.item_prices_container);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        newCategoryName = (EditText) findViewById(R.id.new_category_name);
        uploadImageProgress = (ProgressBar) findViewById(R.id.upload_image_progress);
        btnProgress = (ProgressBar) findViewById(R.id.btn_progress);
    }
}
