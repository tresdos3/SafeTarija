package com.tarija.tresdos.safetarija;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardHActivity extends AppCompatActivity {

    private View photoheader;
    private ImageView btnLogout;
    private CircleImageView imageuser;
    private TextView nombreuser, emailuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_h);
        photoheader = (View) findViewById(R.id.photoHeader);
        imageuser = (CircleImageView) findViewById(R.id.ImageUser);
        nombreuser = (TextView) findViewById(R.id.NombreUser);
        emailuser = (TextView) findViewById(R.id.EmailUser);
        btnLogout = (ImageView) findViewById(R.id.btnlogut);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoheader.setTranslationZ(6);
            photoheader.invalidate();
        }
    }
}
