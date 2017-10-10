package com.tarija.tresdos.safetarija;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class RastreoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference rootRef,HijosRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView result;
    private String Key;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    public double Lat, Long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rastreo);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        result = (TextView) findViewById(R.id.result);
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
        Key = getIntent().getExtras().getString("Key_Hijo");
        HijosRef.child("hijos").child(Key).child("alerta").setValue("no");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        HijosRef.child("hijos").child(Key).child("ubicacion").child("latitud").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lat = dataSnapshot.getValue(Double.class);
                HijosRef.child("hijos").child(Key).child("ubicacion").child("longitud").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long = dataSnapshot.getValue(Double.class);
                        LatLng sydney = new LatLng(Lat,Long);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.wifidata))
                                .anchor(0.0f, 1.0f)
                                .title("Estoy aki")
                                .snippet("Tu hijo se encuentra aki...")
                                .position(sydney));
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
                        LatLng latLng = new LatLng(Lat, Long);
                        Geocoder geocoder = new Geocoder(RastreoActivity.this);

                        try {

                            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 15);
                            if (addressList != null && addressList.size() > 0) {
                                String locality = addressList.get(0).getAddressLine(0);
                                String country = addressList.get(0).getLocality();
                                if (!locality.isEmpty() && !country.isEmpty()) {
                                    result.setText(locality + " - " + country);
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
    private void configureCameraIdle(final double lati, final double logi) {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = new LatLng(lati, logi);
                Geocoder geocoder = new Geocoder(RastreoActivity.this);

                try {

                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 15);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getLocality();
                        if (!locality.isEmpty() && !country.isEmpty()) {
                            result.setText(locality + " - " + country);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
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
