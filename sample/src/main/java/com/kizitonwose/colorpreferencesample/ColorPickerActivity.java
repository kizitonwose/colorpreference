package com.kizitonwose.colorpreferencesample;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorShape;

public class ColorPickerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int toolbarColor;
    private int fabColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        fabColor = ContextCompat.getColor(this, R.color.color_3);

        findViewById(R.id.colorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorDialog(toolbar);
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorDialog(view);
            }
        });

    }

    private void showColorDialog(final View view) {
        new ColorDialog.Builder(this)
                .setColorShape(ColorShape.CIRCLE)
                .setColorChoices(R.array.color_choices)
                .setSelectedColor(view instanceof Toolbar ? toolbarColor : fabColor)
                .setColorSelectedListener(new ColorDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int newColor) {
                        view.setBackgroundColor(newColor);
                        if (view instanceof Toolbar) {
                            toolbar.setBackgroundColor(newColor);
                            toolbarColor = newColor;
                        } else {
                            ((FloatingActionButton) view).setBackgroundTintList(ColorStateList.valueOf(newColor));
                            fabColor = newColor;
                        }
                    }
                }).show();
    }


}
