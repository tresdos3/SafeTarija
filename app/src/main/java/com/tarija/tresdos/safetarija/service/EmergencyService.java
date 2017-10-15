package com.tarija.tresdos.safetarija.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.safetarija.R;
import com.tarija.tresdos.safetarija.model.myreponse;
import com.tarija.tresdos.safetarija.model.sender;
import com.tarija.tresdos.safetarija.other.common;
import com.tarija.tresdos.safetarija.remote.ApiService;
import com.tarija.tresdos.safetarija.model.Notification;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyService extends Service implements SensorEventListener, LocationListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private DatabaseReference rootRef,HijosRef;
    private FirebaseAuth auth;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Huid = "HuidKey";
    public String tokenP;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;
    ApiService mService;

    public EmergencyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mService = common.getFCMClient();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        return START_STICKY;
    }

//region [Sensores]
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > 15) {
            guardarE();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void showNotification(String Text) {
        final NotificationManager mgr = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(this);
        note.setContentTitle("Safe Tarija");
        note.setContentText(Text);
        note.setContentInfo("Enviando alerta...");
        note.setAutoCancel(true);
        note.setSmallIcon(R.mipmap.login);
        mgr.notify(101, note.build());
    }
    private void guardarE(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        String texto = sharedpreferences.getString(Huid,"");
//        maps nuevo = new maps(location.getLatitude(),location.getLongitude());
        HijosRef.child("hijos").child(texto).child("emergencia").child("latitud").setValue("");
        HijosRef.child("hijos").child(texto).child("emergencia").child("longitud").setValue("");
        HijosRef.child("hijos").child(texto).child("alerta").setValue("no");
        HijosRef.child("hijos").child(texto).child("fecha").setValue("");
        rootRef.child(user.getUid()).child("tokenP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tokenP = dataSnapshot.getValue(String.class);
                EnviarNot(tokenP);
//                fn_getlocation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onDestroy() {
        Process.killProcess(Process.myPid());
    }
    public void EnviarNot(final String TokenPadre){
        String texto = sharedpreferences.getString(Huid,"");
        FirebaseUser user = auth.getCurrentUser();
        rootRef.child(user.getUid()).child("hijos").child(texto).child("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String NombreH = dataSnapshot.getValue(String.class);
                Notification notification = new Notification("Safe Tarija",NombreH +" ha activado una alerta");
                sender sender = new sender(TokenPadre, notification);
                mService.SendNotification(sender)
                        .enqueue(new Callback<myreponse>() {
                            @Override
                            public void onResponse(Call<myreponse> call, Response<myreponse> response) {
                                if (response.body().success == 1){
                                    showNotification("Tus familiares seran notificados");
                                }
                                else{
                                    showNotification("Error...");
                                }
                            }

                            @Override
                            public void onFailure(Call<myreponse> call, Throwable t) {

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    endregion

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
