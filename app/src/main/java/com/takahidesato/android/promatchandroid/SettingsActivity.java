package com.takahidesato.android.promatchandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tsato on 5/23/16.
 */
public class SettingsActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.txv_phone)
    TextView mPhoneTextView;
    @Bind(R.id.txv_email)
    TextView mEmailTextView;
    @OnClick(R.id.txv_phone)
    @TargetApi(Build.VERSION_CODES.M)
    public void onPhoneClick(View v) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < 23) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.promatch_phone)));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.promatch_phone)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
    @OnClick(R.id.txv_email)
    public void onEmailClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.promatch_email));
        startActivity(intent);
    }

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(37.372462, -122.038209);
        mMap.addMarker(new MarkerOptions().position(sydney).title(getResources().getString(R.string.promatch_sunnyvale)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

}
