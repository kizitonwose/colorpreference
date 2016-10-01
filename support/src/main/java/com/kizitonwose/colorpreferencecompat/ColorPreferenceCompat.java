package com.kizitonwose.colorpreferencecompat;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;

import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorUtils;

/**
 * Created by Kizito Nwose on 9/26/2016.
 */
public class ColorPreferenceCompat extends Preference implements ColorDialog.OnColorSelectedListener {
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
            ColorDialog fragment = ColorDialog.newInstance(mNumColumns, mColorShape, mColorChoices, getValue());
            fragment.setOnColorSelectedListener(this);

            ContextWrapper context = (ContextWrapper) getContext();
            Activity activity = (Activity) context.getBaseContext();

            activity.getFragmentManager().beginTransaction()
                    .add(fragment, getFragmentTag())
                    .commit();
        }
    }

    @Override
    public void onAttached() {
        super.onAttached();

        //helps during activity re-creation
        if (showDialog) {
            ContextWrapper context = (ContextWrapper) getContext();
            Activity activity = (Activity) context.getBaseContext();

            ColorDialog fragment = (ColorDialog) activity
                    .getFragmentManager().findFragmentByTag(getFragmentTag());
            if (fragment != null) {
                // re-bind preference to fragment
                fragment.setOnColorSelectedListener(this);
            }
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

    @Override
    public void onColorSelected(int newColor) {
        setValue(newColor);
    }
}
