package com.tarija.tresdos.safetarija.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    List<String> listanegra = new ArrayList<String>(
            Arrays.asList("com.wo.voice", "com.supercell.clashroyale", "com.facebook.mlite", "org.appspot.apprtc",
                    "com.koushikdutta.vysor", "com.tarija.tresdos.goutuchofer")
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

//                    Log.d(TAG, "run: "+ processes.get(i).processName + " PID: " + processes.get(i).pid);
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
                                Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_LONG).show();
//                                Log.d("App", "La siguiente app se esta ejecutando "+processes.get(i).processName);
//                            Log.d("Mensaje", "run: Desea Cerrar esta aplicacion");
                            }
                        }
                    }
                }
                processes.clear();
            }
        },2000,3000);
        return START_STICKY;
    }
    public void EnviarNot(String TokenPadre , String App, String Nombre){
    }
}
