package com.example.siausd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.siausd.apihelper.BaseApiService;
import com.example.siausd.apihelper.UtilsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiodataMahasiswa extends AppCompatActivity {

    EditText txtBioNim, txtBioNamaMhs, txtBioTempatLahir, txtBioTglLahir,
            txtBioJenisKelamin, txtBioAlamatSkr, txtBioKodePosSkr,
            txtBioAlamatAsal, txtBioKodePosAsal, txtBioNamaAyah, txtBioEmail,
            txtBioNoHp, txtBioNISN, txtBioNIK, txtBioTglLahirAyah, txtBioNamaIbu,
            txtBioTglLahirIbu, txtBioNIKAyah, txtBioNIKIbu;

    AutoCompleteTextView txtBioKabupatenLahir, txtBioKabupatenSkr, txtBioKabupatenAsal;

    DatePickerDialog picker;

    Button btnUbahBiodata;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata_mahasiswa);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        getSemuaKabupaten();

        txtBioNim = (EditText) findViewById(R.id.txtBioNim);
        txtBioNamaMhs = (EditText) findViewById(R.id.txtBioNamaMhs);
        txtBioKabupatenLahir = (AutoCompleteTextView) findViewById(R.id.txtBioKabupatenLahir);
        txtBioTempatLahir = (EditText) findViewById(R.id.txtBioTempatLahir);
        txtBioTglLahir = (EditText) findViewById(R.id.txtBioTglLahir);
        txtBioJenisKelamin = (EditText) findViewById(R.id.txtBioJenisKelamin);
        txtBioAlamatSkr = (EditText) findViewById(R.id.txtBioAlamatSkr);
        txtBioKabupatenSkr = (AutoCompleteTextView) findViewById(R.id.txtBioKabupatenSkr);
        txtBioKodePosSkr = (EditText) findViewById(R.id.txtBioKodePosSkr);
        txtBioAlamatAsal = (EditText) findViewById(R.id.txtBioAlamatAsal);
        txtBioKabupatenAsal = (AutoCompleteTextView) findViewById(R.id.txtBioKabupatenAsal);
        txtBioKodePosAsal = (EditText) findViewById(R.id.txtBioKodePosAsal);
        txtBioNamaAyah = (EditText) findViewById(R.id.txtBioNamaAyah);
        txtBioEmail = (EditText) findViewById(R.id.txtBioEmail);
        txtBioNoHp = (EditText) findViewById(R.id.txtBioNoHp);
        txtBioNISN = (EditText) findViewById(R.id.txtBioNISN);
        txtBioNIK = (EditText) findViewById(R.id.txtBioNIK);
        txtBioTglLahirAyah = (EditText) findViewById(R.id.txtBioTglLahirAyah);
        txtBioNamaIbu = (EditText) findViewById(R.id.txtBioNamaIbu);
        txtBioTglLahirIbu = (EditText) findViewById(R.id.txtBioTglLahirIbu);
        txtBioNIKAyah = (EditText) findViewById(R.id.txtBioNIKAyah);
        txtBioNIKIbu = (EditText) findViewById(R.id.txtBioNIKIbu);

        btnUbahBiodata = (Button) findViewById(R.id.btnUbahBiodata);

        sharedPrefManager = new SharedPrefManager(this);
        txtBioNim.setText(sharedPrefManager.getSPNim());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String mNamaMhs = extras.getString("namaMhs");
        txtBioNamaMhs.setText(mNamaMhs);

        String mKodeKabLahir = extras.getString("kodeKabLahir");
        txtBioKabupatenLahir.setText(mKodeKabLahir);

        String mTempatLahir = extras.getString("tempatLahir");
        txtBioTempatLahir.setText(mTempatLahir);

        String mTglLahir = extras.getString("tglLahir");
        txtBioTglLahir.setText(mTglLahir);
        txtBioTglLahir.setInputType(InputType.TYPE_NULL);
        txtBioTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] tgl = getTanggal(txtBioTglLahir.getText().toString());
                int tahun = tgl[0];
                int bulan = tgl[1]-1;
                int hari = tgl[2];
                Log.e("cetak: ", Integer.toString(bulan));
                picker = new DatePickerDialog(BiodataMahasiswa.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtBioTglLahir.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, tahun, bulan, hari);
                picker.show();
            }
        });

        String mSex = extras.getString("sex");
        txtBioJenisKelamin.setText(mSex);

        String mAlamatSkr = extras.getString("alamatSkr");
        txtBioAlamatSkr.setText(mAlamatSkr);

        String mKodeKabSkr = extras.getString("kodeKabSkr");
        txtBioKabupatenSkr.setText(mKodeKabSkr);

        String mKodePosSkr = extras.getString("kodePosSkr");
        txtBioKodePosSkr.setText(mKodePosSkr);

        String mAlamatAsal = extras.getString("alamatAsal");
        txtBioAlamatAsal.setText(mAlamatAsal);

        String mKodeKabAsal = extras.getString("kodeKabAsal");
        txtBioKabupatenAsal.setText(mKodeKabAsal);

        String mKodePosAsal = extras.getString("kodePosAsal");
        txtBioKodePosAsal.setText(mKodePosAsal);

        String mNamaAyah = extras.getString("namaAyah");
        txtBioNamaAyah.setText(mNamaAyah);

        String mEmail = extras.getString("email");
        txtBioEmail.setText(mEmail);

        String mNoHp = extras.getString("noHp");
        txtBioNoHp.setText(mNoHp);

        String mNisn = extras.getString("nisn");
        txtBioNISN.setText(mNisn);

        String mNik = extras.getString("nik");
        txtBioNIK.setText(mNik);

        String mTglLahirAyah = extras.getString("tglLahirAyah");
        txtBioTglLahirAyah.setText(mTglLahirAyah);
        txtBioTglLahirAyah.setInputType(InputType.TYPE_NULL);
        txtBioTglLahirAyah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] tgl = getTanggal(txtBioTglLahirAyah.getText().toString());
                int tahun = tgl[0];
                int bulan = tgl[1]-1;
                int hari = tgl[2];
                Log.e("cetak: ", Integer.toString(bulan));
                picker = new DatePickerDialog(BiodataMahasiswa.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtBioTglLahirAyah.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, tahun, bulan, hari);
                picker.show();
            }
        });

        String mNamaIbuKandung = extras.getString("namaIbuKandung");
        txtBioNamaIbu.setText(mNamaIbuKandung);

        String mTglLahirIbuKandung = extras.getString("tglLahirIbuKandung");
        txtBioTglLahirIbu.setText(mTglLahirIbuKandung);
        txtBioTglLahirIbu.setInputType(InputType.TYPE_NULL);
        txtBioTglLahirIbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] tgl = getTanggal(txtBioTglLahirIbu.getText().toString());
                int tahun = tgl[0];
                int bulan = tgl[1]-1;
                int hari = tgl[2];
                Log.e("cetak: ", Integer.toString(bulan));
                picker = new DatePickerDialog(BiodataMahasiswa.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtBioTglLahirIbu.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, tahun, bulan, hari);
                picker.show();
            }
        });

        String mNikAyah = extras.getString("nikAyah");
        txtBioNIKAyah.setText(mNikAyah);

        String mNikIbuKandung = extras.getString("nikIbuKandung");
        txtBioNIKIbu.setText(mNikIbuKandung);

        btnUbahBiodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtBioNIK.getText().toString()) || TextUtils.isEmpty(txtBioNamaIbu.getText().toString())) {
                    Toast.makeText(mContext, "NIK dan Nama Ibu Kandung harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    ubahBiodataMhs(sharedPrefManager.getSPNim(), txtBioKabupatenLahir.getText().toString(),
                            txtBioTempatLahir.getText().toString(), txtBioTglLahir.getText().toString(),
                            txtBioAlamatSkr.getText().toString(), txtBioKabupatenSkr.getText().toString(),
                            txtBioKodePosSkr.getText().toString(), txtBioAlamatAsal.getText().toString(),
                            txtBioKabupatenAsal.getText().toString(), txtBioKodePosAsal.getText().toString(),
                            txtBioNamaAyah.getText().toString(), txtBioEmail.getText().toString(),
                            txtBioNoHp.getText().toString(), txtBioNISN.getText().toString(),
                            txtBioNIK.getText().toString(), txtBioTglLahirAyah.getText().toString(),
                            txtBioNamaIbu.getText().toString(), txtBioTglLahirIbu.getText().toString(),
                            txtBioNIKAyah.getText().toString(), txtBioNIKIbu.getText().toString());
                }
            }
        });
    }

    private void ubahBiodataMhs(String nim, String kodeKabLahir, String tempatLahir,
                             String tglLahir, String alamatSkr, String kodeKabSkr,
                             String kodePosSkr, String alamatAsal, String kodeKabAsal,
                             String kodePosAsal, String namaAyah, String email,
                             String noHp, String nisn, String nik, String tglLahirAyah,
                             String namaIbu, String tglLahirIbu, String nikAyah, String nikIbu) {
        mApiService.ubahBiodataRequest(nim, kodeKabLahir, tempatLahir, tglLahir,alamatSkr,kodeKabSkr,
                kodePosSkr, alamatAsal, kodeKabAsal, kodePosAsal, namaAyah, email, noHp, nisn, nik,
                tglLahirAyah, namaIbu, tglLahirIbu, nikAyah, nikIbu).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    try {
                        JSONObject jsonResults = new JSONObject(response.body().string());
                        if (jsonResults.getString("error").equals("false")) {
                            String message = jsonResults.getString("message");
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, HomeMahasiswa.class);// New activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
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

    private int[] getTanggal(String tgl) {
        StringTokenizer st = new StringTokenizer(tgl, "-");
        int[] tanggal = new int[3];
        tanggal[0] = Integer.parseInt(st.nextToken());
        tanggal[1] = Integer.parseInt(st.nextToken());
        tanggal[2] = Integer.parseInt(st.nextToken());
        return tanggal;
    }

    private void getSemuaKabupaten() {
        mApiService.getAllKabupaten().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("Async Data RemoteData",
                                "Got REMOTE DATA ");
                        JSONObject jsonResults = new JSONObject(response.body().string());
                        JSONArray result = jsonResults.getJSONArray("result");
                        List<String> str = new ArrayList<String>();
                        for (int i = 0; i<result.length(); i++) {
                            JSONObject jo = result.getJSONObject(i);
                            String kabupaten = jo.getString("kabupaten");
                            str.add(kabupaten);
                        }
                        AutoCompleteTextView autoTV1 =
                                (AutoCompleteTextView)((Activity)mContext).findViewById(R.id.txtBioKabupatenLahir);
                        AutoCompleteTextView autoTV2 =
                                (AutoCompleteTextView)((Activity)mContext).findViewById(R.id.txtBioKabupatenSkr);
                        AutoCompleteTextView autoTV3 =
                                (AutoCompleteTextView)((Activity)mContext).findViewById(R.id.txtBioKabupatenAsal);

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext,
                                android.R.layout.simple_dropdown_item_1line, str.toArray(new String[0]));
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext,
                                android.R.layout.simple_dropdown_item_1line, str.toArray(new String[0]));
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext,
                                android.R.layout.simple_dropdown_item_1line, str.toArray(new String[0]));
                        autoTV1.setAdapter(adapter1);
                        autoTV2.setAdapter(adapter2);
                        autoTV3.setAdapter(adapter3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Async Data RemoteData",
                        "error in getting remote data");
            }
        });
    }
}
