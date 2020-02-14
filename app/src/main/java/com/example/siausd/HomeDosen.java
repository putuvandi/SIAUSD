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
import android.widget.TextView;
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

public class HomeDosen extends AppCompatActivity implements View.OnClickListener {

    Button btnBiodataDosen, btnUbahPasswordDosen;

    BottomNavigationView btnNavigation;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dosen);

        btnBiodataDosen = (Button) findViewById(R.id.btnBiodataDosen);
        btnUbahPasswordDosen = (Button) findViewById(R.id.btnUbahPasswordDosen);

        btnNavigation = findViewById(R.id.btnNavigation);

        sharedPrefManager = new SharedPrefManager(this);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        btnBiodataDosen.setOnClickListener(this);
        btnUbahPasswordDosen.setOnClickListener(this);

        btnNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        //setFragment(profileFragment);
                        startActivity(new Intent(getApplicationContext(), HomeDosen.class));
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
        if (v == btnBiodataDosen) {
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            requestTampilBiodataDosen();
        }
        if (v == btnUbahPasswordDosen) {
            Intent intent = new Intent(this, UbahPasswordDosen.class);
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
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                        startActivity(new Intent(HomeDosen.this, Login.class)
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

    private void requestTampilBiodataDosen(){
        mApiService.tampilBiodataDosenRequest(sharedPrefManager.getSPKode()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonResult = new JSONObject(response.body().string());
                        if (jsonResult.getString("error").equals("false")){

                            Intent intent = new Intent(mContext, BiodataDosen.class);
                            Bundle extras = new Bundle();

                            String mKodePgw = jsonResult.getJSONObject("dosen").getString("kode_pegawai");
                            extras.putString("kodePgw", mKodePgw);

                            String mNamaPgw = jsonResult.getJSONObject("dosen").getString("nama_pegawai");
                            extras.putString("namaPgw", mNamaPgw);

                            String mGlrDpn = jsonResult.getJSONObject("dosen").getString("glr_dpn");
                            extras.putString("glrDpn", mGlrDpn);

                            String mGlrBlk = jsonResult.getJSONObject("dosen").getString("glr_blk");
                            extras.putString("glrBlk", mGlrBlk);

                            String mNik = jsonResult.getJSONObject("dosen").getString("nik");
                            extras.putString("nik", mNik);

                            String mFileNik = jsonResult.getJSONObject("dosen").getString("file_nik");
                            extras.putString("fileNik", mFileNik);

                            String mNpwp = jsonResult.getJSONObject("dosen").getString("npwp");
                            extras.putString("npwp", mNpwp);

                            String mFileNpwp = jsonResult.getJSONObject("dosen").getString("file_npwp");
                            extras.putString("fileNpwp", mFileNpwp);

                            String mAlamatSkr = jsonResult.getJSONObject("dosen").getString("alamat_skr");
                            extras.putString("alamatSkr", mAlamatSkr);

                            String mTelpRumah = jsonResult.getJSONObject("dosen").getString("telp_rumah");
                            extras.putString("telpRumah", mTelpRumah);

                            String mNoHp1 = jsonResult.getJSONObject("dosen").getString("no_hp1");
                            extras.putString("noHp1", mNoHp1);

                            String mEmail1 = jsonResult.getJSONObject("dosen").getString("email1");
                            extras.putString("email1", mEmail1);

                            String mTempatlahir = jsonResult.getJSONObject("dosen").getString("tempat_lahir");
                            extras.putString("tempatLahir", mTempatlahir);

                            String mTglLahir = jsonResult.getJSONObject("dosen").getString("tgl_lahir");
                            extras.putString("tglLahir", mTglLahir);

                            String mJenisKelamin = jsonResult.getJSONObject("dosen").getString("jenis_kelamin");
                            extras.putString("jenisKelamin", mJenisKelamin);

                            String mStatusKeluar = jsonResult.getJSONObject("dosen").getString("status_keluar");
                            extras.putString("statusKeluar", mStatusKeluar);

                            String mStatusPgw = jsonResult.getJSONObject("dosen").getString("status_pegawai");
                            extras.putString("statusPegawai", mStatusPgw);

                            String mNidn = jsonResult.getJSONObject("dosen").getString("nidn");
                            extras.putString("nidn", mNidn);

                            String mAlamatKtp = jsonResult.getJSONObject("dosen").getString("alamat_ktp");
                            extras.putString("alamatKtp", mAlamatKtp);

                            String mEmail2 = jsonResult.getJSONObject("dosen").getString("email2");
                            extras.putString("email2", mEmail2);

                            String mNoHp2 = jsonResult.getJSONObject("dosen").getString("no_hp2");
                            extras.putString("noHp2", mNoHp2);

                            String pesan = jsonResult.getString("message");
                            Toast.makeText(mContext, pesan, Toast.LENGTH_SHORT).show();

                            intent.putExtras(extras);
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            //finish();
                        }else{
                            String error_msg = jsonResult.getString("error_msg");
                            Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > "+t.toString());
                loading.dismiss();
            }
        });
    }
}
