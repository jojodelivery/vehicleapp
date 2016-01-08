package com.pin91.jojovehicleapp.views.action;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;

import java.util.List;

/**
 * Created by udit on 1/2/2016.
 */
abstract public class Pager<T extends Object> {

    private TextView leftAction;
    private TextView rightAction;
    private TextView currentPosition;
    List<T> models;
    int currentCount=0;
    private final String TEXT_FONT_STYLE = "app_icons.ttf";

    public abstract void onLeftActionHandler(T object);
    public abstract void onRightActionHandler(T object);
    public abstract void initialView(T object);

    public Pager(View rootView, List<T> models){
        leftAction = (TextView)rootView.findViewById(R.id.previous_arrow);
        rightAction = (TextView)rootView.findViewById(R.id.next_arrow);
        currentPosition = (TextView)rootView.findViewById(R.id.current_position);
        Typeface tf = Typeface.createFromAsset(rootView.getContext().getAssets(), TEXT_FONT_STYLE);
        leftAction.setTypeface(tf);
        rightAction.setTypeface(tf);
        this.models = models;
        if(models.size() > 0){
            initialView(models.get(currentCount));
            displayCounter();
        }
        setActionListeners();
    }

    private void displayCounter(){
        int maxSize = models.size();
        String displayText = (currentCount+1)+"/"+maxSize;
        currentPosition.setText(displayText);
    }
    private void setActionListeners(){
        leftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentCount>0){
                    currentCount--;
                    onLeftActionHandler(models.get(currentCount));
                    displayCounter();
                }
            }
        });

        rightAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentCount < models.size()-1){
                    currentCount++;
                    onRightActionHandler(models.get(currentCount));
                    displayCounter();
                }
            }
        });
    }
}
