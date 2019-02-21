package com.kizitonwose.colorpreferencesample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Kizito Nwose on 10/1/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.github) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_url)));
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
