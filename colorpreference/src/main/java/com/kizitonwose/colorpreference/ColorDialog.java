package com.kizitonwose.colorpreference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import java.io.Serializable;

/**
 * Created by Kizito Nwose on 9/28/2016.
 */
public class ColorDialog extends DialogFragment {
    private GridLayout colorGrid;
    private OnColorSelectedListener colorSelectedListener;
    private int numColumns;
    private int[] colorChoices;
    private int colorShape;

    //the color to be checked
    private int selectedColorValue;

    private static final String NUM_COLUMNS_KEY = "num_columns";
    private static final String COLOR_SHAPE_KEY = "color_shape";
    private static final String COLOR_CHOICES_KEY = "color_choices";
    private static final String SELECTED_COLOR_KEY = "selected_color";

    public ColorDialog() {
    }


    public static ColorDialog newInstance(int numColumns, int colorShape, int[] colorChoices,  int selectedColorValue) {
        Bundle args = new Bundle();
        args.putInt(NUM_COLUMNS_KEY, numColumns);
        args.putInt(COLOR_SHAPE_KEY, colorShape);
        args.putIntArray(COLOR_CHOICES_KEY, colorChoices);
        args.putInt(SELECTED_COLOR_KEY, selectedColorValue);

        ColorDialog dialog = new ColorDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        numColumns = args.getInt(NUM_COLUMNS_KEY);
        colorShape = args.getInt(COLOR_SHAPE_KEY);
        colorChoices = args.getIntArray(COLOR_CHOICES_KEY);
        selectedColorValue = args.getInt(SELECTED_COLOR_KEY);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener colorSelectedListener) {
        this.colorSelectedListener = colorSelectedListener;
        repopulateItems();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        repopulateItems();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View rootView = layoutInflater.inflate(R.layout.dialog_colors, null);

        colorGrid = (GridLayout) rootView.findViewById(R.id.color_grid);
        colorGrid.setColumnCount(numColumns);
        repopulateItems();

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    private void repopulateItems() {
        if (colorSelectedListener == null || colorGrid == null) {
            return;
        }

        Context context = colorGrid.getContext();
        colorGrid.removeAllViews();
        for (final int color : colorChoices) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.grid_item_color, colorGrid, false);

            ColorUtils.setColorViewValue(itemView.findViewById(R.id.color_view), color,
                    color == selectedColorValue, colorShape);

            itemView.setClickable(true);
            itemView.setFocusable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (colorSelectedListener!=null){
                        colorSelectedListener.onColorSelected(color);
                    }
                    dismiss();
                }
            });

            colorGrid.addView(itemView);
        }

        sizeDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        sizeDialog();
    }

    private void sizeDialog() {
        if (colorSelectedListener == null || colorGrid == null) {
            return;
        }

        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }

        final Resources res = colorGrid.getContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();

        // Can't use Integer.MAX_VALUE here (weird issue observed otherwise on 4.2)
        colorGrid.measure(
                View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.AT_MOST));
        int width = colorGrid.getMeasuredWidth();
        int height = colorGrid.getMeasuredHeight();

        int extraPadding = res.getDimensionPixelSize(R.dimen.color_grid_extra_padding);

        width += extraPadding;
        height += extraPadding;

        dialog.getWindow().setLayout(width, height);
    }

    public interface OnColorSelectedListener extends Serializable {
        void onColorSelected(int newColor);
    }
}
