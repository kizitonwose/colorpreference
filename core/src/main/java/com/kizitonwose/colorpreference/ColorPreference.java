package com.kizitonwose.colorpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class ColorPreference extends Preference implements ColorDialog.OnColorSelectedListener {
    private int[] colorChoices = {};
    private int value = 0;
    private int itemLayoutId = R.layout.pref_color_layout;
    private int itemLayoutLargeId = R.layout.pref_color_layout_large;
    private int numColumns = 5;
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
            numColumns = a.getInteger(R.styleable.ColorPreference_numColumns, numColumns);
            colorShape = ColorShape.getShape(a.getInteger(R.styleable.ColorPreference_colorShape, 1));
            previewSize = PreviewSize.getSize(a.getInteger(R.styleable.ColorPreference_viewSize, 1));
            showDialog = a.getBoolean(R.styleable.ColorPreference_showDialog, true);
            int choicesResId = a.getResourceId(R.styleable.ColorPreference_colorChoices,
                    R.array.default_color_choice_values);
            colorChoices = ColorUtils.extractColorArray(choicesResId, getContext());

        } finally {
            a.recycle();
        }

        setWidgetLayoutResource(previewSize == PreviewSize.NORMAL ? itemLayoutId : itemLayoutLargeId);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        ImageView previewView = (ImageView) view.findViewById(R.id.color_view);
        ColorUtils.setColorViewValue(previewView, value, false, colorShape);
    }

    public void setValue(int value) {
        if (callChangeListener(value)) {
            this.value = value;
            persistInt(value);
            notifyChanged();
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (showDialog) {
            ColorUtils.showDialog(getContext(), this, getFragmentTag(),
                    numColumns, colorShape, colorChoices, getValue());
        }
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();
        //helps during activity re-creation
        if (showDialog) {
            ColorUtils.attach(getContext(), this, getFragmentTag());
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
        return value;
    }

    @Override
    public void onColorSelected(int newColor, String tag) {
        setValue(newColor);
    }
}
