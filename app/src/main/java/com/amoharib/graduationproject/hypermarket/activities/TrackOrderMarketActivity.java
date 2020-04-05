package com.amoharib.graduationproject.hypermarket.activities;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.activities.MainActivity;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.HyperMarket;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.OrderStatus;
import com.amoharib.graduationproject.utils.VectorDrawableUtils;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.services.DataService;
import com.amoharib.graduationproject.utils.OrderStatus;
import com.amoharib.graduationproject.utils.VectorDrawableUtils;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TrackOrderMarketActivity extends AppCompatActivity implements DataListeners.OrderStatusListener {


    private TextView restName;
    private TimelineView sentTimeLine;
    private TimelineView seenTimeLine;
    private TimelineView onTheWayTimeLine;
    private CircleProgressBar timer;
    private TextView timerText;
    private String restaurantId;
    private String orderId;
    private HyperMarket rest;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);


        restaurantId = getIntent().getStringExtra("hypermarket");
        orderId = getIntent().getStringExtra("orderMarketId");

        initView();

        DataService.getInstance().getMarket(restaurantId, new DataListeners.HyperMarketListener() {
            @Override
            public void onHyperMarketRetrieved(HyperMarket restaurant) {
                if (restaurant != null) {
                    rest = restaurant;
                    initCustomViews();

                    new CountDownTimer(45 * 60000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            timerText.setText(timeFormat.format(new Date(millisUntilFinished)));
                            Integer timeLeft = 100 - ((int) ((double) millisUntilFinished / ((double) 45 * 60000) * 100));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timer.setProgress(timeLeft, true);
                            } else {
                                timer.setProgress(timeLeft);
                            }
                        }

                        @Override
                        public void onFinish() {
                            timerText.setText(timeFormat.format(new Date(0)));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timer.setProgress(100, true);
                            } else {
                                timer.setProgress(100);
                            }

                        }
                    }.start();
                }
            }
        });
    }

    private void initCustomViews() {
        restName.setText(rest.getTitle());
        DataService.getInstance().addOnOrderMarketStatusChanged(orderId, this);
    }

    private void initView() {
        restName = (TextView) findViewById(R.id.restName);
        sentTimeLine = (TimelineView) findViewById(R.id.sentTimeLine);
        seenTimeLine = (TimelineView) findViewById(R.id.seenTimeLine);
        onTheWayTimeLine = (TimelineView) findViewById(R.id.onTheWayTimeLine);
        timer = (CircleProgressBar) findViewById(R.id.timer);
        timerText = (TextView) findViewById(R.id.timerText);


        sentTimeLine.initLine(TimelineView.getTimeLineViewType(0, 3));
        onTheWayTimeLine.initLine(TimelineView.getTimeLineViewType(2, 3));

    }

    @Override
    public void onStatusChanged(OrderStatus status) {
        changeStatus(status);
    }

    private void changeStatus(OrderStatus status) {
        switch (status) {
            case SENT:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_dotted_circle));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_empty_circle));
                break;
            case SEEN:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_dotted_circle));
                break;
            case OUT:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                break;
        }

        refreshStatusView();
    }

    private void refreshStatusView() {
        sentTimeLine.invalidate();
        seenTimeLine.invalidate();
        onTheWayTimeLine.invalidate();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HyperMarketActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
        finish();
    }
}
