package com.tarija.tresdos.safetarija;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karan.churi.PermissionManager.PermissionManager;
import com.tarija.tresdos.safetarija.other.PolicyManager;
import com.tarija.tresdos.safetarija.service.BrowserService;
import com.tarija.tresdos.safetarija.service.ContactsService;
import com.tarija.tresdos.safetarija.service.DetectAppService;
import com.tarija.tresdos.safetarija.service.EmergencyService;
import com.tarija.tresdos.safetarija.service.LocationService;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DashboardHActivity extends AppCompatActivity {


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private LocationManager lm;
    private View photoheader;
    private FloatingActionButton btnPattner;
    private ImageView btnLogout;
    private CircleImageView imageuser;
    private TextView nombreuser, emailuser, hijouser, keyuser;
    private FirebaseAuth auth;
    private DatabaseReference rootRef,HijosRef, HijoEstado;
    SharedPreferences sharedpreferences;
    private String Mensaje;
    private PolicyManager policyManager;
    String device_unique_id,IMEI;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public  static final int RequestPermissionCode  = 1 ;
    public static final String mypreference = "mypref";
    public static  final String NombreHIJO = "Nombrehijo";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";
    public static final String Huid = "HuidKey";
    public static final String BtnVisible = "btnKey";
    private PermissionManager permissionManager;
    private final int REQUEST_LOCATION = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_h);
        policyManager = new PolicyManager(this);

        auth = FirebaseAuth.getInstance();
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        photoheader = (View) findViewById(R.id.photoHeader);
        imageuser = (CircleImageView) findViewById(R.id.ImageUser);
        nombreuser = (TextView) findViewById(R.id.NombreUser);
        emailuser = (TextView) findViewById(R.id.EmailUser);
        hijouser = (TextView) findViewById(R.id.tvEducation);
        keyuser = (TextView) findViewById(R.id.tvAddress);
        btnLogout = (ImageView) findViewById(R.id.btnlogut);
        btnPattner = (FloatingActionButton) findViewById(R.id.buttomPatner);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoheader.setTranslationZ(6);
            photoheader.invalidate();
        }
//        if (!activarGps()){
//            mensaje();
//        }
//        else{
//            permissions.add(ACCESS_FINE_LOCATION);
//            permissions.add(ACCESS_COARSE_LOCATION);
//            permissionsToRequest = findUnAskedPermissions(permissions);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//
//                if (permissionsToRequest.size() > 0)
//                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
//            }
//            Intent intentGeo = new Intent(DashboardHActivity.this, LocationService.class);
//            startService(intentGeo);
//        }
        rootRef = FirebaseDatabase.getInstance().getReference();


        if (auth.getCurrentUser() != null){
            FirebaseUser user = auth.getCurrentUser();
            ViewData(user);
        }
        else{
            goPreguntaScreen();
        }
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);
//        permisos();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardHActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION},10);
        }


        if (sharedpreferences.contains(BtnVisible)) {
            String texto = sharedpreferences.getString(BtnVisible,"");
            if (texto.equals("true")){
                btnPattner.setVisibility(View.INVISIBLE);
            }
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });
        btnPattner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarServicio();
                loadIMEI();
                if (sharedpreferences.contains(NombreHIJO)) {
                    String t = sharedpreferences.getString(NombreHIJO, "");
                }
                Intent i = new Intent(DashboardHActivity.this, PinActivity.class);
//                finish();
//                startActivity(i);
                startActivityForResult(i,4);

            }
        });

    }
    private void mensaje(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Atencion...")
                .setContentText("Necesitas activar el Gps")
                .setConfirmText("Activar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setCancelText("Cancelar")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        System.exit(0);
                    }
                })
                .show();
    }
    private boolean activarGps(){
        boolean bandera;
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            bandera = false;
        }
        else{
            bandera = true;
        }
        return bandera;
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private  void AdminDevice(){
//
        if (!policyManager.isAdminActive()) {
            Intent activateDeviceAdmin = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    policyManager.getAdminComponent());
            activateDeviceAdmin
                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Despues de activar esta funcion, la aplicacion no podra ser disinstalada");
            startActivityForResult(activateDeviceAdmin,
                    PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
        }
    }
    private void goPreguntaScreen() {
        Intent i = new Intent(DashboardHActivity.this, ChooseActivity.class);
        finish();
        startActivity(i);
    }
    private void ViewData(FirebaseUser user) {
        nombreuser.setText(user.getDisplayName());
        emailuser.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(imageuser);
        if (sharedpreferences.contains(Tipo)) {
            String texto = sharedpreferences.getString(Huid,"");
            cargarDatos(texto, user.getUid());
        }
    }
    private void cargarDatos(final String texto, String uid) {
        HijosRef = rootRef.child(uid).child("hijos").child(texto);
        HijosRef.addValueEventListener(new ValueEventListener() {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map)dataSnapshot.getValue();
                hijouser.setText(map.get("nombre"));
                keyuser.setText(texto);
                editor.putString(NombreHIJO, map.get("nombre"));
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadIMEI() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {

            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = mngr.getDeviceId();
            device_unique_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Toast.makeText(this,"Alredy granted",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        Log.d("asd", "onRequestPermissionsResult: ");
        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(DashboardHActivity.this,"Okey", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(DashboardHActivity.this,"Okey Algo Salio Mal", Toast.LENGTH_LONG).show();

                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:

                if (PResult.length == 1 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    IMEI = mngr.getDeviceId();
                    device_unique_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
                } else {
                    Toast.makeText(this,"ehgehfg",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void iniciarServicio(){
        Intent intentGeo = new Intent(this, LocationService.class);
        startService(intentGeo);
        Intent intentBrowser = new Intent(this, BrowserService.class);
        startService(intentBrowser);
        Intent intentEmer = new Intent(this, EmergencyService.class);
        startService(intentEmer);
//        Intent intent = new Intent(this, PermisosService.class);
//        startService(intent);
        Intent intentApps = new Intent(this, DetectAppService.class);
        startService(intentApps);
        Intent intentInternet = new Intent(this, ContactsService.class);
        startService(intentInternet);
    }
    private void cerrarServicio(){
        Intent intentGeo = new Intent(this, LocationService.class);
        this.stopService(intentGeo);
        Intent intentEmer = new Intent(this, EmergencyService.class);
        this.stopService(intentEmer);
        Intent intentInternet = new Intent(this, ContactsService.class);
        this.stopService(intentInternet);
//        Intent intent = new Intent(this, PermisosService.class);
//        this.stopService(intent);
        Intent intentApps = new Intent(this, DetectAppService.class);
        this.stopService(intentApps);
        Intent intentBrowser = new Intent(this, BrowserService.class);
        this.stopService(intentBrowser);
    }
//    private void Alerta(){
//        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Atencion...")
//                .setContentText("Que deseas hacer en este momento?...")
//                .setConfirmText("Cerrar Sesion")
//                .setCancelText("Quitar App")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        Intent i = new Intent(DashboardHActivity.this, LogoutActivity.class);
//                        startActivityForResult(i,2);
//                    }
//                })
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        Intent i = new Intent(DashboardHActivity.this, LogoutActivity.class);
//                        startActivityForResult(i,3);
//                    }
//                })
//                .show();
//    }
public void open(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Estas seguro, Tomaste una desicion?");
            alertDialogBuilder.setPositiveButton("Cerrar Sesion",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
//                            Toast.makeText(DashboardHActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(DashboardHActivity.this, LogoutActivity.class);
                            startActivityForResult(i,2);
                        }
                    });

    alertDialogBuilder.setNegativeButton("Desinstalar",new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent i = new Intent(DashboardHActivity.this, LogoutActivity.class);
            startActivityForResult(i,3);
        }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseUser user = auth.getCurrentUser();
        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
            Mensaje = message;
            if (Mensaje.equals("true")){
                cerrarServicio();
                auth.signOut();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Tipo, "n");
                editor.putString(Session,"no");
                editor.commit();
                HijoEstado = rootRef.child(user.getUid()).child("hijos").child(keyuser.getText().toString()).child("estado");
                HijoEstado.setValue("no");
                goPreguntaScreen();
            }
            else {
                Toast.makeText(DashboardHActivity.this, "Error: vuelve a intertarlo", Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode == Activity.RESULT_OK
                && requestCode == PolicyManager.DPM_ACTIVATION_REQUEST_CODE) {
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode==3)
        {
            String message=data.getStringExtra("MESSAGE");
            Mensaje = message;
            if (Mensaje.equals("true")){
                cerrarServicio();
                auth.signOut();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Tipo, "n");
                editor.putString(Session,"no");
                editor.commit();
                HijoEstado = rootRef.child(user.getUid()).child("hijos").child(keyuser.getText().toString()).child("estado");
                HijoEstado.setValue("no");
                policyManager.disableAdmin();
                finish();
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:com.tarija.tresdos.safetarija"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Tus padres seran notificados", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
            }
        }
        if(requestCode==4) {
            AdminDevice();
            String message = data.getStringExtra("MESSAGE");
            Mensaje = message;
            if (Mensaje.equals("true")) {
                btnPattner.setVisibility(View.INVISIBLE);
            }
        }
    }
}
