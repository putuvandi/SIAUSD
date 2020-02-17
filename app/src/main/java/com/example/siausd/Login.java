package com.example.siausd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.siausd.apihelper.BaseApiService;
import com.example.siausd.apihelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText txtUser, txtPassword;
    Spinner loginSebagai;
    Button btnLogin;
    ProgressDialog loading;

    List<String> listLogin;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper

        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        loginSebagai = (Spinner) findViewById(R.id.loginSebagai);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        listLogin = new ArrayList<>();
        listLogin.add("-- Pilih login sebagai --");
        listLogin.add("Mahasiswa");
        listLogin.add("Dosen");

        sharedPrefManager = new SharedPrefManager(this);
        // skip login jika user sudah login
        if (sharedPrefManager.getSPSudahLogin()){
            if (!sharedPrefManager.getSPNim().equals("")) {
                startActivity(new Intent(Login.this, SelamatDatangMhs.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            } else if (!sharedPrefManager.getSPKode().equals("") &&
                    !sharedPrefManager.getSpUser().equals("")) {
                startActivity(new Intent(Login.this, SelamatDatangMhs.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listLogin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loginSebagai.setAdapter(adapter);

        loginSebagai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUser.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()) {
                    txtUser.setError("Masukkan NIM atau username");
                } else if (!txtUser.getText().toString().isEmpty() && txtPassword.getText().toString().isEmpty()) {
                    txtPassword.setError("Masukkan password");
                } else if (txtUser.getText().toString().isEmpty() && txtPassword.getText().toString().isEmpty()) {
                    txtUser.setError("Masukkan NIM atau username");
                    txtPassword.setError("Masukkan password");
                } else {
                    if (loginSebagai.getSelectedItem().equals("Mahasiswa")) {
                        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                        requestLoginMhs();
                    } else if (loginSebagai.getSelectedItem().equals("Dosen")) {
                        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                        requestLoginDosen();
                    } else {
                        Toast.makeText(mContext, "Silahkan pilih peran login", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent (Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void requestLoginMhs() {
        mApiService.loginMhsRequest(txtUser.getText().toString(), txtPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                if (jsonResults.getString("error").equals("false")) {
                                    // jika login berhasil maka data nim yang ada di response API
                                    // akan diparsing ke activity selanjutnya
                                    Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                    String mNim = jsonResults.getJSONObject("user").getString("nim");

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NIM, mNim);
                                    // Shared Pref berfungsi untuk menjadi trigger session login
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                                    startActivity(new Intent(mContext, SelamatDatangMhs.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                } else {
                                    // jika login gagal
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

    private void requestLoginDosen() {
        mApiService.loginDosenRequest(txtUser.getText().toString(), txtPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonResults = new JSONObject(response.body().string());
                                if (jsonResults.getString("error").equals("false")) {
                                    // jika login berhasil maka data nim yang ada di response API
                                    // akan diparsing ke activity selanjutnya
                                    Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                    String mKode = jsonResults.getJSONObject("dosen").getString("kode_pegawai");
                                    String mUser = jsonResults.getJSONObject("dosen").getString("user");

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_KODE, mKode);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USER, mUser);
                                    // Shared Pref berfungsi untuk menjadi trigger session login
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                                    startActivity(new Intent(mContext, SelamatDatangDosen.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();

                                } else {
                                    // jika login gagal
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
