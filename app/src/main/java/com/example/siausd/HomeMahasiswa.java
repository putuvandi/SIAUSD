package com.example.siausd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.siausd.apihelper.BaseApiService;
import com.example.siausd.apihelper.UtilsApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeMahasiswa extends AppCompatActivity implements View.OnClickListener {

    Button btnBiodataMhs, btnUbahPasswordMhs;

    BottomNavigationView btnNavigation;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;
    ProgressDialog loading;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_mahasiswa);

        btnBiodataMhs = (Button) findViewById(R.id.btnBiodataMhs);
        btnUbahPasswordMhs = (Button) findViewById(R.id.btnUbahPasswordMhs);

        btnNavigation = findViewById(R.id.btnNavigation);

        sharedPrefManager = new SharedPrefManager(this);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        extras = new Bundle();

        btnBiodataMhs.setOnClickListener(this);
        btnUbahPasswordMhs.setOnClickListener(this);

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
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnBiodataMhs) {
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            requestTampilBiodata();
        }
        if (v == btnUbahPasswordMhs) {
            Intent intent = new Intent(this, UbahPasswordMahasiswa.class);
            startActivity(intent);
        }
    }

    private void konfirmasiLogout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Apakah yakin ingin keluar?");

        alertBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIM, "");
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                        startActivity(new Intent(HomeMahasiswa.this, Login.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });

        alertBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tidak memproses apa pun
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void requestTampilBiodata() {
        mApiService.tampilBiodataMhsRequest(sharedPrefManager.getSPNim()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    try {
                        JSONObject jsonResults = new JSONObject(response.body().string());
                        if (jsonResults.getString("error").equals("false")) {

                            Intent intent = new Intent(mContext, BiodataMahasiswa.class);

                            String mNamaMhs = jsonResults.getJSONObject("mahasiswa").getString("nama_mahasiswa");
                            extras.putString("namaMhs", mNamaMhs);

                            String mKodeKabLahir = jsonResults.getJSONObject("mahasiswa").getString("kode_kabupaten_lahir");
                            extras.putString("kodeKabLahir", mKodeKabLahir);

                            String mTempatLahir = jsonResults.getJSONObject("mahasiswa").getString("tempat_lahir");
                            extras.putString("tempatLahir", mTempatLahir);

                            String mTglLahir = jsonResults.getJSONObject("mahasiswa").getString("tgl_lahir");
                            extras.putString("tglLahir", mTglLahir);

                            String mSex = jsonResults.getJSONObject("mahasiswa").getString("jenis_kelamin");
                            extras.putString("sex", mSex);

                            String mAlamatSkr = jsonResults.getJSONObject("mahasiswa").getString("alamat_skr");
                            extras.putString("alamatSkr", mAlamatSkr);

                            String mKodeKabSkr = jsonResults.getJSONObject("mahasiswa").getString("kode_kabupaten_skr");
                            extras.putString("kodeKabSkr", mKodeKabSkr);

                            String mKodePosSkr = jsonResults.getJSONObject("mahasiswa").getString("kode_pos_skr");
                            extras.putString("kodePosSkr", mKodePosSkr);

                            String mAlamatAsal = jsonResults.getJSONObject("mahasiswa").getString("alamat_asal");
                            extras.putString("alamatAsal", mAlamatAsal);

                            String mKodeKabAsal = jsonResults.getJSONObject("mahasiswa").getString("kode_kabupaten_asal");
                            extras.putString("kodeKabAsal", mKodeKabAsal);

                            String mKodePosAsal = jsonResults.getJSONObject("mahasiswa").getString("kode_pos_asal");
                            extras.putString("kodePosAsal", mKodePosAsal);

                            String mNamaAyah = jsonResults.getJSONObject("mahasiswa").getString("nama_ayah");
                            extras.putString("namaAyah", mNamaAyah);

                            String mEmail = jsonResults.getJSONObject("mahasiswa").getString("email");
                            extras.putString("email", mEmail);

                            String mNoHp = jsonResults.getJSONObject("mahasiswa").getString("no_hp");
                            extras.putString("noHp", mNoHp);

                            String mNisn = jsonResults.getJSONObject("mahasiswa").getString("nisn");
                            extras.putString("nisn", mNisn);

                            String mNik = jsonResults.getJSONObject("mahasiswa").getString("nik");
                            extras.putString("nik", mNik);

                            String mTglLahirAyah = jsonResults.getJSONObject("mahasiswa").getString("tgl_lahir_ayah");
                            extras.putString("tglLahirAyah", mTglLahirAyah);

                            String mNamaIbuKandung = jsonResults.getJSONObject("mahasiswa").getString("nama_ibu_kandung");
                            extras.putString("namaIbuKandung", mNamaIbuKandung);

                            String mTglLahirIbuKandung = jsonResults.getJSONObject("mahasiswa").getString("tgl_lahir_ibu_kandung");
                            extras.putString("tglLahirIbuKandung", mTglLahirIbuKandung);

                            String mNikAyah = jsonResults.getJSONObject("mahasiswa").getString("nik_ayah");
                            extras.putString("nikAyah", mNikAyah);

                            String mNikIbuKandung = jsonResults.getJSONObject("mahasiswa").getString("nik_ibu_kandung");
                            extras.putString("nikIbuKandung", mNikIbuKandung);

                            intent.putExtras(extras);
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            //finish();
                        } else {
                            // jika gagal
                            String error_msg = jsonResults.getString("error_msg");
                            Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                loading.dismiss();
            }
        });
    }
}
