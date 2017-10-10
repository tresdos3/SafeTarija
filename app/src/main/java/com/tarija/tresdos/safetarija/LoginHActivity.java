package com.tarija.tresdos.safetarija;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.valdesekamdem.library.mdtoast.MDToast;

public class LoginHActivity extends AppCompatActivity {

    private EditText txtemail, txtpassword;
    private Button btniniciar;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private RelativeLayout contenedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_h);
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        btniniciar = (Button) findViewById(R.id.btnIniciar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        contenedor = (RelativeLayout) findViewById(R.id.contenedor);
        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null){
//            Intent i = new Intent(LoginHActivity.this, ChooseActivity.class);
//            startActivity(i);
//        }
        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contenedor.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (txtemail.getText().toString().equals("") || txtpassword.getText().toString().equals("")){
                    contenedor.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Existe campo vacio", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                }
                else {
                    LoginUsuario(txtemail.getText().toString(), txtpassword.getText().toString());
                }
            }
        });
    }
    private void LoginUsuario(final String email, final String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                                contenedor.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                MDToast mdToast = MDToast.makeText(getApplicationContext(), task.getException().toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                                mdToast.show();
                        }
                        else{
                            contenedor.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Intent i = new Intent(LoginHActivity.this, SelecHijoActivity.class);
                            finish();
                            startActivity(i);
                        }
                    }
                });
    }
}
