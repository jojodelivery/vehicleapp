package com.pin91.jojovehicleapp.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.utils.JojoUtils;


/**
 * Created by udit on 11/17/2015.
 */
public class DashboardCell extends RelativeLayout {

    TextView cellIcon;
    TextView notificationCircle;
    TextView cellTitle;
    private final String TEXT_FONT_STYLE = "app_icons.ttf";
    private int circlePaddingMultiplier = 20;
    private int textSize = 30;

    public DashboardCell(Context context) {
        super(context);
        initViews();
    }

    public DashboardCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DashboardCell);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DashboardCell_cellTitle:
                    String cellTitle = a.getString(attr);
                    setCellTitle(cellTitle);
                    break;
                case R.styleable.DashboardCell_iconText:
                    String dashboardText = a.getString(attr);
                    setDashboardIdentifierText(dashboardText);
                    break;
                case R.styleable.DashboardCell_iconFontSize:
                    int fontSize = a.getInteger(attr, 80);
                    cellIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    break;
                case R.styleable.DashboardCell_notificationTextSize:
                    int textSize = a.getInteger(attr, this.textSize);
                    notificationCircle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                    break;
                case R.styleable.DashboardCell_cellBackground:
                    JojoUtils.setBackground(this, a.getDrawable(R.styleable.DashboardCell_cellBackground));
                    break;
                case R.styleable.DashboardCell_notificationTextColor:
                    notificationCircle.setTextColor(a.getColor(R.styleable.DashboardCell_notificationTextColor, JojoUtils.getColor(getContext(), R.color.black)));
                    break;
                case R.styleable.DashboardCell_iconColor:
                    cellIcon.setTextColor(a.getColor(R.styleable.DashboardCell_iconColor, Color.parseColor("#ffffff")));
                    break;
            }
        }
        a.recycle();

    }

    private void initViews() {
        cellIcon = new TextView(getContext());
        cellIcon.setTypeface(Typeface.createFromAsset(getContext().getAssets(), TEXT_FONT_STYLE));
        LayoutParams dashboardIdentifierParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dashboardIdentifierParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        dashboardIdentifierParams.addRule(RelativeLayout.CENTER_VERTICAL);
        cellIcon.setLayoutParams(dashboardIdentifierParams);
        setDashboardIdentifierColor(R.color.white);
        int iconId = JojoUtils.generateViewId();
        cellIcon.setId(iconId);
        addView(cellIcon);

        cellTitle = new TextView(getContext());
        cellTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutParams cellTitleParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cellTitleParams.addRule(RelativeLayout.RIGHT_OF, iconId);
        cellTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cellTitle.setTextColor(JojoUtils.getColor(getContext(), R.color.white));
        cellTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cellTitle.setTypeface(null, Typeface.BOLD);
        cellTitle.setLayoutParams(cellTitleParams);
        addView(cellTitle);

        notificationCircle = new TextView(getContext());
        LayoutParams notificationLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        notificationLayoutParams.setMargins(0, 0, -20, -20);

        notificationLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, iconId);
        notificationLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, iconId);
        notificationCircle.setLayoutParams(notificationLayoutParams);
        JojoUtils.setBackground(notificationCircle, R.drawable.rounded_corner_rectangle);
        notificationCircle.setTypeface(null, Typeface.BOLD);
   //     notificationCircle.setTextColor(JojoUtils.getColor(getContext(), R.color.dashboard_cell_circle_boundary));
        setNotificationCircleText(1);
        setNotificationCirclePadding(1);
        addView(notificationCircle);
    }

    public void setNotificationCircleText(int number) {
        notificationCircle.setText(number + "");
    }

    public void setDashboardIdentifierText(String text) {
        cellIcon.setText(text);
    }

    public void setDashboardIdentifierColor(int color) {
        cellIcon.setTextColor(JojoUtils.getColor(getContext(), color));
    }

    public void setNotificationCirclePadding(int number){
        circlePaddingMultiplier = (int)(this.textSize/1.5);
        int digits = JojoUtils.getDigits(number);
        int topPadding = 0;
        int bottomPadding = 0;
        int leftPadding = 0;
        int rightPadding = 0;
        switch (digits){
            case 1:
                leftPadding = rightPadding= this.circlePaddingMultiplier;
                break;
            case 2:
                leftPadding = rightPadding = bottomPadding=topPadding = this.circlePaddingMultiplier;
                break;
            default:
                topPadding = bottomPadding = this.circlePaddingMultiplier*digits;
                rightPadding = leftPadding = this.circlePaddingMultiplier*(digits-2);
                break;
        }
        notificationCircle.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    public void setCellTitle(String cellTitle){
        this.cellTitle.setText(cellTitle);
    }
}
