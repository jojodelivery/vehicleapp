package com.pin91.jojovehicleapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by udit on 11/17/2015.
 */
public class JojoUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static void setBackground(View view, int drawable){
        if(Build.VERSION.SDK_INT >= 21){
            view.setBackground(view.getContext().getResources().getDrawable(drawable, null));
        } else {
            view.setBackgroundDrawable(view.getContext().getResources().getDrawable(drawable));
        }
    }

    public static void setBackground(View view, Drawable drawable){
        if(Build.VERSION.SDK_INT >= 21){
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static int getColor(Context context, int color){
        if(Build.VERSION.SDK_INT >= 21){
            return context.getResources().getColor(color, null);
        } else {
            return context.getResources().getColor(color);
        }
    }

    public static int getDigits(int number){
        int digits = 1;
        if(number/10 > 0){
            number/=10;
            digits++;
        }
        return digits;
    }



    /**
     * Generate a value suitable for use in View.setId
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
