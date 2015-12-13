package com.pin91.jojovehicleapp.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.utils.JojoUtils;


/**
 * Created by udit on 11/17/2015.
 */
public class DashboardCell extends RelativeLayout {

    TextView dashboardIdentifier;
    TextView notificationCircle;
    private final String TEXT_FONT_STYLE = "dashboard_items.ttf";
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
                case R.styleable.DashboardCell_dashboardText:
                    String dashboardText = a.getString(attr);
                    setDashboardIdentifierText(dashboardText);
                    break;
                case R.styleable.DashboardCell_dashboardFontSize:
                    int fontSize = a.getInteger(attr, 80);
                    dashboardIdentifier.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
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
                case R.styleable.DashboardCell_dashboardIconColor:
                    dashboardIdentifier.setTextColor(a.getColor(R.styleable.DashboardCell_dashboardIconColor, JojoUtils.getColor(getContext(), R.color.black)));
                    break;
            }
        }
        a.recycle();

    }

    private void initViews() {
        dashboardIdentifier = new TextView(getContext());
        dashboardIdentifier.setTypeface(Typeface.createFromAsset(getContext().getAssets(), TEXT_FONT_STYLE));
        LayoutParams dashboardIdentifierParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dashboardIdentifierParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        dashboardIdentifier.setLayoutParams(dashboardIdentifierParams);
        setDashboardIdentifierColor(R.color.black);
        int dashboardIdentifierId = JojoUtils.generateViewId();
        dashboardIdentifier.setId(dashboardIdentifierId);
        addView(dashboardIdentifier);

        notificationCircle = new TextView(getContext());
        LayoutParams notificationLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        notificationLayoutParams.setMargins(0, 20, 20, 0);

        notificationLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, dashboardIdentifierId);
        notificationLayoutParams.addRule(RelativeLayout.ALIGN_END, dashboardIdentifierId);
        notificationCircle.setLayoutParams(notificationLayoutParams);
      //  JojoUtils.setBackground(notificationCircle, R.drawable.circular_text_view);
        notificationCircle.setTypeface(null, Typeface.BOLD);
        notificationCircle.setTextColor(JojoUtils.getColor(getContext(), R.color.dashboard_cell_circle_boundary));
        setNotificationCircleText(1);
        setNotificationCirclePadding(1);
        addView(notificationCircle);
    }

    public void setNotificationCircleText(int number) {
        notificationCircle.setText(number + "");
    }

    public void setDashboardIdentifierText(String text) {
        dashboardIdentifier.setText(text);
    }

    public void setDashboardIdentifierColor(int color) {
        dashboardIdentifier.setTextColor(JojoUtils.getColor(getContext(), color));
    }

    public void setOnClickListener(OnClickListener listener) {
        setOnClickListener(listener);
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
}
