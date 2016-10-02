package com.kizitonwose.colorpreference;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;


public class ColorPreference extends Preference implements ColorDialog.OnColorSelectedListener {
    private int[] mColorChoices = {};
    private int mValue = 0;
    private int mItemLayoutId = R.layout.pref_color_layout;
    private int mItemLayoutLargeId = R.layout.pref_color_layout_large;
    private int mNumColumns = 5;
    private View mPreviewView;
    private ColorShape colorShape = ColorShape.CIRCLE;
    private boolean showDialog = true;

    public ColorPreference(Context context) {
        super(context);
        initAttrs(null, 0);
    }

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, 0);
    }

    public ColorPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs, defStyle);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.ColorPreference, defStyle, defStyle);

        PreviewSize previewSize = PreviewSize.NORMAL;
        try {
            //mItemLayoutId = a.getResourceId(R.styleable.ColorPreference_itemLayout, mItemLayoutId);
            mNumColumns = a.getInteger(R.styleable.ColorPreference_numColumns, mNumColumns);
            colorShape = ColorShape.getShape(a.getInteger(R.styleable.ColorPreference_colorShape, 1));
            previewSize = PreviewSize.getSize(a.getInteger(R.styleable.ColorPreference_viewSize, 1));
            showDialog = a.getBoolean(R.styleable.ColorPreference_showDialog, true);
            int choicesResId = a.getResourceId(R.styleable.ColorPreference_colorChoices,
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

        setWidgetLayoutResource(previewSize == PreviewSize.NORMAL ? mItemLayoutId : mItemLayoutLargeId);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mPreviewView = view.findViewById(R.id.color_view);
        ColorUtils.setColorViewValue(mPreviewView, mValue, false, colorShape);
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
            ColorDialog fragment = ColorDialog.newInstance(mNumColumns, colorShape, mColorChoices, getValue());
            fragment.setOnColorSelectedListener(this);

            Activity activity = (Activity) getContext();
            activity.getFragmentManager().beginTransaction()
                    .add(fragment, getFragmentTag())
                    .commit();
        }
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        //helps during activity re-creation
        if (showDialog) {
            Activity activity = (Activity) getContext();
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
    public void onColorSelected(int newColor, String tag) {
        setValue(newColor);
    }
}
