package com.tarija.tresdos.safetarija.service;

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
import com.tarija.tresdos.safetarija.model.Notification;
import com.tarija.tresdos.safetarija.model.myreponse;
import com.tarija.tresdos.safetarija.model.sender;
import com.tarija.tresdos.safetarija.other.browser;
import com.tarija.tresdos.safetarija.other.common;
import com.tarija.tresdos.safetarija.remote.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.everything.providers.android.browser.Bookmark;
import me.everything.providers.android.browser.BrowserProvider;
import me.everything.providers.android.browser.Search;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowserService extends Service {
    private BrowserProvider browserProvider;
    private List<Bookmark> bookmarks;
    private List<Search> searches;
    private DatabaseReference rootRef,HijosRef;
    private FirebaseAuth auth;
    public String tokenP;
    List<String> listanegra = new ArrayList<String>(Arrays.asList("xxx", "pornografia", "desnudas", "putas", "asesinatos", "drogas"));
    SharedPreferences sharedpreferences;
    //    varibles sharedpreferences
    public static final String mypreference = "mypref";
    //    tipo p=padre h=hijo n=ninguno
    public static final String UltimoNotificado = "ultimoKey";
    public static final String Huid = "HuidKey";
    ApiService mService;

    public BrowserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        browserProvider = new BrowserProvider(this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mService = common.getFCMClient();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UltimoNotificado, "prueba");
        editor.commit();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int sdk = Build.VERSION.SDK_INT;
                if ( sdk < 23){
                    bookmarks = browserProvider.getBookmarks().getList();
                    searches = browserProvider.getSearches().getList();
//                for (int i = 0; i < bookmarks.size(); i++ ){
                    final String ultimo = bookmarks.get(bookmarks.size() - 1).title;
                    final int visitas = bookmarks.get(bookmarks.size() - 1).visits;
                    final String url = bookmarks.get(bookmarks.size() - 1).url;
                    final Long creado = bookmarks.get(bookmarks.size() - 1).created;
                    for (int v = 0; v < listanegra.size(); v++){
                        String Valores = listanegra.get(v);
                        String texto = sharedpreferences.getString(UltimoNotificado,"");
                        if (ultimo.contains(Valores)){
                            if (!ultimo.contains(texto)){
                                auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();
                                rootRef = FirebaseDatabase.getInstance().getReference();
                                HijosRef = rootRef.child(user.getUid());
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(UltimoNotificado, ultimo);
                                editor.commit();
//                                Log.d("SinNotificar", "Su hijo ha abierto una pagina peligrosa "+ ultimo);
                                rootRef.child(user.getUid()).child("tokenP").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (sharedpreferences.contains(Huid)) {
                                            String t =sharedpreferences.getString(Huid, "");
//                                            bookmarks.get(v).date;
                                            Log.d("Token Hijo", "onDataChange: " + t);
                                            Log.d("DatosSitio", "Titulo: "+ ultimo + " Url: "+ url + " Cantidad Visitas: "+ visitas + " Creado: " + creado);
                                            browser nuevo = new browser(ultimo,url, visitas, creado, new Date().toString());
                                            HijosRef.child("hijos").child(t).child("historial").push().setValue(nuevo);
                                        }
                                        else{
                                            Log.d("Token Hijo", "No Existe Token");
                                        }
                                        tokenP = dataSnapshot.getValue(String.class);
                                        EnviarNot(tokenP);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }

            }
        },2000, 2000);
        return START_STICKY;
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

                                }
                                else{

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
}
