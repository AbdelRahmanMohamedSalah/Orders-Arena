package com.amoharib.graduationproject.seller.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.RestControlStatus;
import com.amoharib.graduationproject.utils.StringUtils;
import com.daimajia.swipe.SwipeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;

public class SellerMenuItemViewHolder extends RecyclerView.ViewHolder {

    private OnDataChangeListener onDataChangeListener;

    private SwipeLayout swipeLayout;
    private ImageView editBtn, deleteBtn;
    private SimpleDraweeView itemImage;
    private TextView itemName, itemDescription, itemPrice, multiSize;

    private String restId = StringUtils.getUsernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    public SellerMenuItemViewHolder(View itemView) {
        super(itemView);
        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.container);
        editBtn = (ImageView) itemView.findViewById(R.id.edit_item);
        deleteBtn = (ImageView) itemView.findViewById(R.id.delete_item);
        itemImage = (SimpleDraweeView) itemView.findViewById(R.id.foodIcon);
        itemName = (TextView) itemView.findViewById(R.id.foodName);
        itemDescription = (TextView) itemView.findViewById(R.id.foodDescription);
        itemPrice = (TextView) itemView.findViewById(R.id.foodPrice);
        multiSize = (TextView) itemView.findViewById(R.id.multi_sizes);
        configSwipeLayout();
    }

    private void configSwipeLayout() {
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
    }

    public SellerMenuItemViewHolder updateUI(final Food food, final String category) {
        itemImage.setImageURI(food.getIcon());
        itemName.setText(food.getName());
        itemDescription.setText(food.getDescription());
        itemPrice.setText(String.format("%sLE", food.getSizes().get(0).getPrice()));
        if (food.getSizes().size() > 1) multiSize.setVisibility(View.VISIBLE);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataChangeListener.onDataChanged(RestControlStatus.EDITED);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataService.getInstance().deleteMenuItemForRestaurant(restId, category, food.getId(), new DataListeners.DataListener() {
                    @Override
                    public void onReceiveStatus(boolean status) {
                        onDataChangeListener.onDataChanged(RestControlStatus.DELETED);
                    }
                });
            }
        });


        return this;
    }

    public void addOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {
        public void onDataChanged(RestControlStatus restControlStatus);
    }
}
