package com.amoharib.graduationproject.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.amoharib.graduationproject.models.Address;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    private StringUtils() {
    }

    public static Spanned getFormattedAddress(Address address) {
        String compressedText = String.format("Address Name: <b>%s</b><br>District: <b>%s</b><br>Street/Number:   <b>%s</b><br>House/Building: <b>%s</b><br>Apartment/Office Name: <b>%s</b><br>Phone: <b>%s</b><br>Delivery Instructions: <b>%s</b>",
                address.getAddressName(),
                address.getDistrictName(),
                address.getStreetNumber(),
                address.getHouseBuilding(),
                address.getApartmentOffice(),
                address.getPhone(),
                address.getDeliveryInstructions());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(compressedText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(compressedText);
        }
    }

    public static String getFormattedStatus(OrderStatus orderStatus) {
        String status = "";

        switch (orderStatus) {
            case SENT:
                status = "Sent to the Restaurant";
                break;
            case SEEN:
                status = "Seen by the Restaurant";
                break;
            case OUT:
                status = "On Your Way";
                break;
        }

        return status;
    }

    public static String getUsernameFromEmail(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    public static String getFormattedDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy MM:HH");
        return simpleDateFormat.format(new Date(timestamp));
    }

//    public static String getUserIdFromUrl(String url) {
//        return url.substring(url.indexOf('=') + 1, url.indexOf('&'));
//    }

    public static String getImageUriForUser(String imageUri) {
        return imageUri + "?type=large";
    }
}
