package com.tarija.tresdos.safetarija;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.valdesekamdem.library.mdtoast.MDToast;

public class LogoutActivity extends AppCompatActivity {

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private final static String TAG = LogoutActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    //    tipo p=padre h=hijo n=ninguno
    public static final String Password = "passwordKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        mPinLockView = (PinLockView) findViewById(R.id.Lpin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.Lindicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(6);
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                String texto = sharedpreferences.getString(Password,"");
                if (pin.equals(texto)) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Pin Aceptado...", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE","true");
                    setResult(2,intent);
                    finish();
                } else {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Los pin no son iguales", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();;
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE","false");
                    setResult(2,intent);
                    finish();
                }
            }

            @Override
            public void onEmpty() {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Error! no ingresaste un pin", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Pin Actualizado", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
            }
        });

    }
}
