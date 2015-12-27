package com.pin91.jojovehicleapp.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;

/**
 * Created by udit on 12/27/2015.
 */
public class OrderPacketDetailActivity extends Activity {

    private final String TEXT_FONT_STYLE = "app_icons.ttf";
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.order_packet_selection_view);
        initializeViews();
    }

    private void initializeViews(){
        TextView nextIcon = (TextView)findViewById(R.id.next_arrow);
        TextView previousIcon = (TextView)findViewById(R.id.previous_arrow);
        TextView acceptBtn = (TextView)findViewById(R.id.acceptBtn);
        TextView rejectBtn = (TextView)findViewById(R.id.rejectBtn);
        Typeface tf = Typeface.createFromAsset(getAssets(), TEXT_FONT_STYLE);
        nextIcon.setTypeface(tf);
        previousIcon.setTypeface(tf);
        acceptBtn.setTypeface(tf);
        rejectBtn.setTypeface(tf);
    }
}
