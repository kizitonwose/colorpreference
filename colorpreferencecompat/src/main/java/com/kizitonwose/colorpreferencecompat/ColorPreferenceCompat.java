package com.kizitonwose.colorpreferencecompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kizitonwose.colorpreference.ColorUtils;

/**
 * Created by Kizito Nwose on 9/26/2016.
 */
public class ColorPreferenceCompat extends Preference {
    private int[] mColorChoices = {};
    private int mValue = 0;
    private int mItemLayoutId = R.layout.pref_color_layout;
    private int mItemLayoutLargeId = R.layout.pref_color_layout_large;
    private int mNumColumns = 5;
    private View mPreviewView;
    private int mColorShape = 1;
    private boolean showDialog = true;
    private int previewSize = 1;

    public ColorPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
    }

    public ColorPreferenceCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, 0);
    }

    public ColorPreferenceCompat(Context context) {
        super(context);
        initAttrs(null, 0);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.ColorPreferenceCompat, defStyle, defStyle);

        try {
            mNumColumns = a.getInteger(R.styleable.ColorPreferenceCompat_numColumns, mNumColumns);
            mColorShape = a.getInteger(R.styleable.ColorPreferenceCompat_colorShape, 1);
            previewSize = a.getInteger(R.styleable.ColorPreferenceCompat_viewSize, 1);
            showDialog = a.getBoolean(R.styleable.ColorPreferenceCompat_showDialog, true);
            int choicesResId = a.getResourceId(R.styleable.ColorPreferenceCompat_colorChoices,
                    R.array.default_color_choice_values);
            if (choicesResId > 0) {
                String[] choices = a.getResources().getStringArray(choicesResId);

                mColorChoices = new int[choices.length];
                for (int i = 0; i < choices.length; i++) {
                    mColorChoices[i] = Color.parseColor(choices[i]);
                }
            }

        } finally {
            a.recycle();
        }
        setWidgetLayoutResource(previewSize == 1 ? mItemLayoutId : mItemLayoutLargeId);

    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mPreviewView = holder.findViewById(R.id.color_view);
        ColorUtils.setColorViewValue(mPreviewView, mValue, false, mColorShape);
    }

    public void setValue(int value) {
        if (callChangeListener(value)) {
            mValue = value;
            persistInt(value);
            notifyChanged();
        }
    }


    @Override
    protected void onClick() {
        super.onClick();

        if (showDialog) {
            ColorDialogFragment fragment = ColorDialogFragment.newInstance();
            fragment.setPreference(this);

            ContextWrapper context = (ContextWrapper) getContext();
            Activity activity = (Activity) context.getBaseContext();

            activity.getFragmentManager().beginTransaction()
                    .add(fragment, getFragmentTag())
                    .commit();
        }
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(0) : (Integer) defaultValue);
    }

    public String getFragmentTag() {
        return "color_" + getKey();
    }

    public int getValue() {
        return mValue;
    }

    public static class ColorDialogFragment extends DialogFragment {
        private ColorPreferenceCompat mPreference;
        private GridLayout mColorGrid;

        public ColorDialogFragment() {
        }

        public static ColorDialogFragment newInstance() {
            return new ColorDialogFragment();
        }

        public void setPreference(ColorPreferenceCompat preference) {
            mPreference = preference;
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

            mColorGrid = (GridLayout) rootView.findViewById(R.id.color_grid);
            mColorGrid.setColumnCount(mPreference.mNumColumns);
            repopulateItems();

            return new AlertDialog.Builder(getActivity())
                    .setView(rootView)
                    .create();
        }

        private void repopulateItems() {
            if (mPreference == null || mColorGrid == null) {
                return;
            }

            Context context = mColorGrid.getContext();
            mColorGrid.removeAllViews();
            for (final int color : mPreference.mColorChoices) {
                View itemView = LayoutInflater.from(context)
                        .inflate(R.layout.grid_item_color, mColorGrid, false);

                ColorUtils.setColorViewValue(itemView.findViewById(R.id.color_view), color,
                        color == mPreference.getValue(), mPreference.mColorShape);
                itemView.setClickable(true);
                itemView.setFocusable(true);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPreference.setValue(color);
                        dismiss();
                    }
                });

                mColorGrid.addView(itemView);
            }

            sizeDialog();
        }

        @Override
        public void onStart() {
            super.onStart();
            sizeDialog();
        }

        private void sizeDialog() {
            if (mPreference == null || mColorGrid == null) {
                return;
            }

            Dialog dialog = getDialog();
            if (dialog == null) {
                return;
            }

            final Resources res = mColorGrid.getContext().getResources();
            DisplayMetrics dm = res.getDisplayMetrics();

            // Can't use Integer.MAX_VALUE here (weird issue observed otherwise on 4.2)
            mColorGrid.measure(
                    View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.AT_MOST));
            int width = mColorGrid.getMeasuredWidth();
            int height = mColorGrid.getMeasuredHeight();

            int extraPadding = res.getDimensionPixelSize(R.dimen.color_grid_extra_padding);

            width += extraPadding;
            height += extraPadding;

            dialog.getWindow().setLayout(width, height);
        }
    }



}
