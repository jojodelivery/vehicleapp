package com.pin91.jojovehicleapp.views.action;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;

/**
 * Created by udit on 1/3/2016.
 */
public abstract class SelectionPanel<T extends Object> {
    TextView acceptBtn;
    TextView rejectBtn;
    private final String TEXT_FONT_STYLE = "app_icons.ttf";
    T object;
    public abstract void onAcceptClick(T object);
    public abstract void onRejectClick(T object);

    public SelectionPanel(View rootView, T object){
        acceptBtn = (TextView)rootView.findViewById(R.id.acceptBtn);
        rejectBtn = (TextView)rootView.findViewById(R.id.rejectBtn);
        Typeface tf = Typeface.createFromAsset(rootView.getContext().getAssets(), TEXT_FONT_STYLE);
        acceptBtn.setTypeface(tf);
        rejectBtn.setTypeface(tf);
        this.object = object;
        setClickListeners();
    }


    private void setClickListeners(){
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onAcceptClick(object);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRejectClick(object);
            }
        });
    }

    public void disablePanel(){
        acceptBtn.setEnabled(false);
        rejectBtn.setEnabled(false);
    }
}
