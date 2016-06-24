package com.takahidesato.android.promatchandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
public class AboutActivity extends AppCompatActivity implements OnMapReadyCallback {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 100;

    @Bind(R.id.txv_phone)
    TextView mPhoneTextView;
    @Bind(R.id.txv_email)
    TextView mEmailTextView;
    @OnClick(R.id.txv_phone)
    @TargetApi(Build.VERSION_CODES.M)
    public void onPhoneClick(View v) {
        checkPermission();
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
        setContentView(R.layout.activity_about);
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

    /***
     * Reference: https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
     */
    private void checkPermission() {
        int hasCallPhonePermission = ContextCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.CALL_PHONE);
        if (hasCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(AboutActivity.this, Manifest.permission.CALL_PHONE)) {
                showMessageOKCancel("You need to allow access to Phone",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AboutActivity.this,
                                        new String[] {Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(AboutActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        /*** making a phone call after permission is granted ***/
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.promatch_phone)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AboutActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
