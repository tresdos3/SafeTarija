package com.tarija.tresdos.safetarija;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tarija.tresdos.safetarija.receiver.NetworkReceiver;
import com.valdesekamdem.library.mdtoast.MDToast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChooseActivity extends AppCompatActivity{

    SharedPreferences sharedpreferences;
    private Button btnPadre, btnHijo;

    //    tipo p=padre h=hijo n=ninguno
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        btnPadre = (Button) findViewById(R.id.btnpadre);
        btnHijo = (Button) findViewById(R.id.btnhijo);

        Typeface CarterOne = Typeface.createFromAsset(getAssets(), "CarterOne.ttf");
        btnPadre.setTypeface(CarterOne);
        btnHijo.setTypeface(CarterOne);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Tipo)) {
            String texto = sharedpreferences.getString(Tipo,"");
            if (texto.equals("p")){
                Intent i = new Intent(ChooseActivity.this, LoginPActivity.class);
                finish();
                startActivity(i);
            }
            if (texto.equals("h")){
                if (sharedpreferences.contains(Session)){
                    String texto1 = sharedpreferences.getString(Session,"");
                    if (texto1.equals("si")){
                        Intent i = new Intent(ChooseActivity.this, DashboardHActivity.class);
                        finish();
                        startActivity(i);
                    }
                }
            }
        }

        btnPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Realizado!...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Tipo, "p");
                editor.commit();
                Intent i = new Intent(ChooseActivity.this, LoginPActivity.class);
                finish();
                startActivity(i);
            }
        });
        btnHijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Realizado!...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Tipo, "h");
                editor.commit();
                Intent i = new Intent(ChooseActivity.this, LoginHActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
}
