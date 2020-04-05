package com.amoharib.graduationproject.buyer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.StringUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserOrderViewHolder extends RecyclerView.ViewHolder {
    private View restNamePlaceHolder, addressPlaceHolder;
    private ShimmerFrameLayout restNameShimmer, addressShimmer;
    private TextView restName, userAddress;
    private TextView orderDate, orderStatus;
    public Button viewDetailsBtn;

    public UserOrderViewHolder(View itemView) {
        super(itemView);
        restNamePlaceHolder = (View) itemView.findViewById(R.id.restNamePlaceHolder);
        addressPlaceHolder = (View) itemView.findViewById(R.id.addressPlaceHolder);
        restNameShimmer = (ShimmerFrameLayout) itemView.findViewById(R.id.restNameShimmer);
        addressShimmer = (ShimmerFrameLayout) itemView.findViewById(R.id.addressShimmer);
        restName = (TextView) itemView.findViewById(R.id.orderRestaurantName);
        orderDate = (TextView) itemView.findViewById(R.id.orderDateTimeText);
        orderStatus = (TextView) itemView.findViewById(R.id.orderStatusText);
        userAddress = (TextView) itemView.findViewById(R.id.orderAddressText);
        viewDetailsBtn = (Button) itemView.findViewById(R.id.viewDetailsBtn);
    }

    public void updateView(Order order) {

        restNameShimmer.startShimmerAnimation();
        addressShimmer.startShimmerAnimation();

        DataService.getInstance().getRestaurant(order.getRestId(), new DataListeners.RestaurantListener() {
            @Override
            public void onRestaurantRetrieved(Restaurant restaurant) {
                String rest = restaurant.getTitle();
                restName.setText(rest);
                restNamePlaceHolder.setVisibility(View.GONE);
                restNameShimmer.stopShimmerAnimation();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String time = String.format("Your Order has been made on : %s", dateFormat.format(new Date(order.getTimestamp())));
        orderDate.setText(time);

        String status = String.format("Your Order is : %s", StringUtils.getFormattedStatus(order.getStatus()));
        orderStatus.setText(status);

        DataService.getInstance().getAddressByUid(order.getUserId(), order.getUserAddressId(), new DataListeners.UserAddressListener() {
            @Override
            public void onDataReceived(Address address) {
                Spanned text = StringUtils.getFormattedAddress(address);
                userAddress.setText(text);
                addressPlaceHolder.setVisibility(View.GONE);
                addressShimmer.stopShimmerAnimation();
            }
        });

    }
}
