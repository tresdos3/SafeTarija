package com.tarija.tresdos.safetarija;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tarija.tresdos.safetarija.other.Hijos;
import com.valdesekamdem.library.mdtoast.MDToast;

public class RegistroActivity extends AppCompatActivity {
    private TextView textoT,textoT2;
    private EditText hint;
    private Button register;
    private DatabaseReference rootRef,HijosRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private MDToast mdToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        textoT = (TextView) findViewById(R.id.textoTitulo);
        textoT2 = (TextView) findViewById(R.id.textoTitulo2);
        hint = (EditText) findViewById(R.id.txtNombreH);
        register = (Button) findViewById(R.id.btnRegistro);
        Typeface CarterOne = Typeface.createFromAsset(getAssets(), "CarterOne.ttf");
        textoT.setTypeface(CarterOne);
        textoT2.setTypeface(CarterOne);
        hint.setTypeface(CarterOne);
        register.setTypeface(CarterOne);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    goLoginScreen();
                }
            }
        };
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hint.getText().toString().trim().length() != 0){
                    Hijos nuevo = new Hijos(hint.getText().toString(), "null","no","no");
                    HijosRef.child("hijos").push().setValue(nuevo);
                    mdToast = MDToast.makeText(RegistroActivity.this, "Registrado correctamente", MDToast.LENGTH_LONG, mdToast.TYPE_SUCCESS);
                    mdToast.show();
                    finish();
                }
                else{
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Campo Vacio", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                }
            }
        });
    }
    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
