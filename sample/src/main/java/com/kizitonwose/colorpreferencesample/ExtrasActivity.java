package com.kizitonwose.colorpreferencesample;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorShape;

import java.util.Arrays;
import java.util.List;

public class ExtrasActivity extends BaseActivity implements ColorDialog.OnColorSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int toolbarColor;
    private int fabColor;
    List<String> colorPrimaryList;
    List<String> colorPrimaryDarkList;
    SharedPreferences preferences;
    private final String TOOLBAR_COLOR_KEY = "toolbar-key";
    private final String FAB_COLOR_KEY = "fab-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Extras sample");

        colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toolbarColor = preferences.getInt(TOOLBAR_COLOR_KEY, ContextCompat.getColor(this, R.color.colorPrimary));
        fabColor = preferences.getInt(FAB_COLOR_KEY, ContextCompat.getColor(this, R.color.color_3));

        toolbar.setBackgroundColor(toolbarColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(fabColor));
        updateStatusBarColor(toolbarColor);

        findViewById(R.id.colorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorDialog(toolbar);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorDialog(view);
            }
        });

    }

    private void showColorDialog(final View view) {
        new ColorDialog.Builder(this)
                .setColorShape(view instanceof Toolbar ? ColorShape.SQUARE : ColorShape.CIRCLE)
                .setColorChoices(R.array.color_choices)
                .setTag(String.valueOf(view instanceof Toolbar ? R.id.toolbar : R.id.fab))
                .setSelectedColor(view instanceof Toolbar ? toolbarColor : fabColor)
                .show();
    }

    private void updateStatusBarColor(int newColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(newColor);
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }


    @Override
    public void onColorSelected(int newColor, String tag) {
        //Find and color the view from the tag we set
        View view = findViewById(Integer.parseInt(tag));
        if (view instanceof Toolbar) {
            toolbar.setBackgroundColor(newColor);
            toolbarColor = newColor;
            preferences.edit().putInt(TOOLBAR_COLOR_KEY, newColor).apply();
            //change the status bar color
            updateStatusBarColor(newColor);
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(newColor));
            fabColor = newColor;
            preferences.edit().putInt(FAB_COLOR_KEY, newColor).apply();
        }

    }
}
