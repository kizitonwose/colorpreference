package com.kizitonwose.colorpreferencesample;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kizitonwose.colorpreferencecompat.ColorPreferenceCompat;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

public class PreferenceCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_compat);
        setTitle("ColorPreferenceCompat sample");

    }

    public static class MyPreferenceFragmentCompat extends PreferenceFragmentCompat {

        private final String CUSTOM_PICKER_PREF_KEY = "color_pref_lobster_compat";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_compat);

            findPreference(CUSTOM_PICKER_PREF_KEY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showColorDialog(preference);
                    return true;
                }
            });
        }

        private void showColorDialog(final Preference preference) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View colorView = inflater.inflate(R.layout.dialog_color, null);

            int color = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(CUSTOM_PICKER_PREF_KEY, Color.YELLOW);
            final LobsterPicker lobsterPicker = (LobsterPicker) colorView.findViewById(R.id.lobsterPicker);
            LobsterShadeSlider shadeSlider = (LobsterShadeSlider) colorView.findViewById(R.id.shadeSlider);

            lobsterPicker.addDecorator(shadeSlider);
            lobsterPicker.setColorHistoryEnabled(true);
            lobsterPicker.setHistory(color);
            lobsterPicker.setColor(color);

            new AlertDialog.Builder(getActivity())
                    .setView(colorView)
                    .setTitle("Choose Color")
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((ColorPreferenceCompat) preference).setValue(lobsterPicker.getColor());
                        }
                    })
                    .setNegativeButton("CLOSE", null)
                    .show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.github) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kizitonwose/colorpreference"));
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
