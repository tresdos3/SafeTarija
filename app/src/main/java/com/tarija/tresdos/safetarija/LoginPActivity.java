package com.tarija.tresdos.safetarija;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tarija.tresdos.safetarija.other.myaplication;
import com.tarija.tresdos.safetarija.receiver.NetworkReceiver;
import com.valdesekamdem.library.mdtoast.MDToast;

public class LoginPActivity extends AppCompatActivity  implements NetworkReceiver.ConnectivityReceiverListener, GoogleApiClient.OnConnectionFailedListener{

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";
    private TextView chooseText, titleText;
    private ImageView img;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int SING_IN_CODE = 777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_p);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        Typeface CarterOne = Typeface.createFromAsset(getAssets(), "CarterOne.ttf");
        chooseText = (TextView)findViewById(R.id.newchoose);
        titleText = (TextView) findViewById(R.id.titleText);
        img = (ImageView) findViewById(R.id.imageView);
        signInButton = (SignInButton) findViewById(R.id.btn_signin);
        chooseText.setTypeface(CarterOne);
        titleText.setTypeface(CarterOne);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        signInButton.setSize(SignInButton.SIZE_WIDE);

        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SING_IN_CODE);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    goMainScreen();
                }
            }
        };
        checkConnection();
        chooseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Tipo, "n");
                editor.commit();
                Intent i = new Intent(LoginPActivity.this, ChooseActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myaplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SING_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthGoogle(result.getSignInAccount());
        } else {
            MDToast mdToast = MDToast.makeText(getApplicationContext(), "No se pudo iniciar sesion", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
            mdToast.show();
        }
    }
    private void firebaseAuthGoogle(GoogleSignInAccount signInAccount) {

        progressBar.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);
        chooseText.setVisibility(View.GONE);
        titleText.setVisibility(View.GONE);
        img.setVisibility(View.GONE);

        AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                chooseText.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);

                if (!task.isSuccessful()){
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Error al autentificar", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                }
            }
        });
    }
    private void goMainScreen() {
        Intent intent = new Intent(this, DashboardPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void showSnack(boolean isConnected) {
        if (isConnected) {
            signInButton.setEnabled(true);
            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Bien! Ahora estas conectado a internet", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
            mdToast.show();
        } else {
            signInButton.setEnabled(false);
            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Alerta! No estas conectado a internet", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
            mdToast.show();
        }
    }
}
