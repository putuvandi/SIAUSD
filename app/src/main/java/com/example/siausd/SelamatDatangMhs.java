package com.example.siausd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.siausd.apihelper.BaseApiService;
import com.example.siausd.apihelper.UtilsApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SelamatDatangMhs extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    BottomNavigationView btnNavigation;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selamat_datang_mhs);

        sharedPrefManager = new SharedPrefManager(this);
        mApiService = UtilsApi.getAPIService();
        btnNavigation = findViewById(R.id.btnNavigation);

        btnNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        //setFragment(profileFragment);
                        startActivity(new Intent(getApplicationContext(), HomeMahasiswa.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_logout:
                        konfirmasiLogout();
                        return true;

                }// default:
                return false;
            }



            //private void setFragment(Fragment fragment) {
            //  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // fragmentTransaction.replace(R.id.main_frame,fragment);
            // fragmentTransaction.commit();
            // }
        });
    }
    private void konfirmasiLogout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Apakah yakin ingin keluar?");

        alertBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                        startActivity(new Intent(SelamatDatangMhs.this, Login.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });

        alertBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
