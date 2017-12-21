package com.tarija.tresdos.safetarija.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
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
import com.jaredrummler.android.processes.AndroidProcesses;
import com.tarija.tresdos.safetarija.R;
import com.tarija.tresdos.safetarija.model.Notification;
import com.tarija.tresdos.safetarija.model.myreponse;
import com.tarija.tresdos.safetarija.model.sender;
import com.tarija.tresdos.safetarija.other.common;
import com.tarija.tresdos.safetarija.remote.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectAppService extends Service {
    private DatabaseReference rootRef,HijosRef;
    private FirebaseAuth auth;
    public String tokenP;
    public String ProcesoR;
    public String Nombre;
    private static final String TAG = DetectAppService.class.getSimpleName();
    SharedPreferences sharedpreferences;
    //    varibles sharedpreferences
    public static final String mypreference = "mypref";
    public static  final String NombreHIJO = "Nombrehijo";
    //    tipo p=padre h=hijo n=ninguno
    public static final String UltimoNotificado2 = "ultimoKey";
    public static final String Huid = "HuidKey";
    TimerTask timerTask;
    ApiService mService;
    List<String> listanegra = new ArrayList<String>(
            Arrays.asList("com.wo.voice", "com.supercell.clashroyale", "org.appspot.apprtc", "com.tarija.tresdos.goutuchofer")
    );
    public DetectAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {

        super.onCreate();
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UltimoNotificado2, "prueba");
        editor.commit();
        mService = common.getFCMClient();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
    }
    String lastAppPN = "";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(getApplicationContext());
                for (int i = 0; i< processes.size();i++){
                    final String proceso = processes.get(i).processName;
                    for (int v = 0; v <listanegra.size(); v++){
                        String Lista = listanegra.get(v);
                        String texto = sharedpreferences.getString(UltimoNotificado2,"");
                        if (proceso.contains(Lista)){
                            if (!proceso.contains(texto)){
                                auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();
                                rootRef = FirebaseDatabase.getInstance().getReference();
                                HijosRef = rootRef.child(user.getUid());
                                ProcesoR= processes.get(i).processName;
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(UltimoNotificado2, processes.get(i).processName);
                                editor.commit();
                                Log.d("Mensaje", "run: Sera Notificada");
                                EnviarNot(processes.get(i).processName);
                            }
                            else{
//                                Log.d("App", "La siguiente app se esta ejecutando "+processes.get(i).processName);
                            Log.d("Mensaje", "run: Fue Notificada");
                            }
                        }
                    }
                }
                processes.clear();
            }
        },2000,3000);
        return START_STICKY;
    }
    public void EnviarNot(final String Nombre){
        final FirebaseUser user = auth.getCurrentUser();
        rootRef.child(user.getUid()).child("tokenP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tokenP = dataSnapshot.getValue(String.class);
                String texto = sharedpreferences.getString(Huid,"");
                rootRef.child(user.getUid()).child("hijos").child(texto).child("nombre").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String NombreH = dataSnapshot.getValue(String.class);
                        Notification notification = new Notification(NombreH+" ha iniciado: "+ Nombre,"Safe Tarija");
                        sender sender = new sender(tokenP, notification);
                        mService.SendNotification(sender)
                                .enqueue(new Callback<myreponse>() {
                                    @Override
                                    public void onResponse(Call<myreponse> call, Response<myreponse> response) {
                                        if (response.body().success == 1){
                                            Log.d("Abrio","asdasd");
                                        }
                                        else{
                                            Log.d("Abrio","asdasd");
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
