package com.kizitonwose.colorpreference;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kizito Nwose on 9/28/2016.
 */
public class ColorUtils {

    public static void setColorViewValue(View view, int color, boolean selected, ColorShape shape) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Resources res = imageView.getContext().getResources();

            Drawable currentDrawable = imageView.getDrawable();
            GradientDrawable colorChoiceDrawable;
            if (currentDrawable instanceof GradientDrawable) {
                // Reuse drawable
                colorChoiceDrawable = (GradientDrawable) currentDrawable;
            } else {
                colorChoiceDrawable = new GradientDrawable();
                colorChoiceDrawable.setShape(shape == ColorShape.SQUARE ? GradientDrawable.RECTANGLE : GradientDrawable.OVAL);
            }

            // Set stroke to dark version of color
            int darkenedColor = Color.rgb(
                    Color.red(color) * 192 / 256,
                    Color.green(color) * 192 / 256,
                    Color.blue(color) * 192 / 256);

            colorChoiceDrawable.setColor(color);
            colorChoiceDrawable.setStroke((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 2, res.getDisplayMetrics()), darkenedColor);

            Drawable drawable = colorChoiceDrawable;
            if (selected) {
                BitmapDrawable checkmark = (BitmapDrawable) res.getDrawable(isColorDark(color)
                        ? R.drawable.checkmark_white
                        : R.drawable.checkmark_black);
                checkmark.setGravity(Gravity.CENTER);
                drawable = new LayerDrawable(new Drawable[]{
                        colorChoiceDrawable,
                        checkmark});
            }

            imageView.setImageDrawable(drawable);

        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    private static final int BRIGHTNESS_THRESHOLD = 150;

    private static boolean isColorDark(int color) {
        return ((30 * Color.red(color) +
                59 * Color.green(color) +
                11 * Color.blue(color)) / 100) <= BRIGHTNESS_THRESHOLD;
    }


}
