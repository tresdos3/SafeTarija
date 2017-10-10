package com.tarija.tresdos.safetarija;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.valdesekamdem.library.mdtoast.MDToast;

public class IntroHActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_h);
        addSlide(AppIntroFragment.newInstance("Rapida...",
                "Selecciona a unos de tus hijos",
                R.drawable.selecciona,
                Color.parseColor("#f39c12")));
        addSlide(AppIntroFragment.newInstance("Seguro...",
                "Notificaciones en tiempo real",
                R.drawable.seguro,
                Color.parseColor("#2980b9")));
        addSlide(AppIntroFragment.newInstance("Impida...",
                "que su hijo no quite la aplicacion...",
                R.drawable.hey,
                Color.parseColor("#7f8c8d")));
        addSlide(AppIntroFragment.newInstance("Cuida...",
                "a tus seres queridos",
                R.drawable.fami,
                Color.parseColor("#ffffff")));
        showStatusBar(false);
        setBarColor(Color.parseColor("#f39c12"));
        setSeparatorColor(Color.parseColor("#f39c12"));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        Intent i = new Intent(IntroHActivity.this, SelecHijoActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        MDToast mdToast = MDToast.makeText(getApplicationContext(), "Finalizado...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    @Override
    public void onSlideChanged() {
        MDToast mdToast = MDToast.makeText(getApplicationContext(), "Siguiente...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }
}
