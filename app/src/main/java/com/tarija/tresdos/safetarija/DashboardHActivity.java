package com.tarija.tresdos.safetarija;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;

public class DashboardHActivity extends AppCompatActivity {

    private View photoheader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_h);
        photoheader = (View) findViewById(R.id.photoHeader);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoheader.setTranslationZ(6);
            photoheader.invalidate();
        }
    }
}
