package com.example.siausd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class BiodataDosen extends AppCompatActivity {

    EditText textKodePegawai, textNamaPegawai, textGelarDepan, textGelarBelakang,
            textNik, textFileNik, textNpwp, textFileNpwp, textAlamatSkr, textTelpRumah,
            textNoHp1, textEmail1, textTempatLahir, textTanggalLahir, textJenisKelamin,
            textNidn, textAlamatKtp, textEmail2, textNoHp2;

    String mKode, statusKeluar, statusPegawai;
    Spinner textStatusPegawai, textStatusKeluar;

    DatePickerDialog picker;
    Button tombol_ubah;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata_dosen);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        textKodePegawai = (EditText)findViewById(R.id.textKodePegawai);
        textNamaPegawai = (EditText)findViewById(R.id.textNamaPegawai);
        textGelarDepan = (EditText)findViewById(R.id.textGelarDepan);
        textGelarBelakang = (EditText)findViewById(R.id.textGelarBelakang);
        textNik = (EditText)findViewById(R.id.textNik);
        textFileNik = (EditText)findViewById(R.id.textFileNik);
        textNpwp = (EditText)findViewById(R.id.textNpwp);
        textFileNpwp = (EditText)findViewById(R.id.textFileNpwp);
        textAlamatSkr = (EditText)findViewById(R.id.textAlamatSkr);
        textTelpRumah = (EditText)findViewById(R.id.textTelpRumah);
        textNoHp1 = (EditText)findViewById(R.id.textNoHp1);
        textEmail1 = (EditText)findViewById(R.id.textEmail1);
        textTempatLahir = (EditText)findViewById(R.id.textTempatLahir);
        textTanggalLahir = (EditText)findViewById(R.id.textTanggalLahir);
        textJenisKelamin = (EditText)findViewById(R.id.textJenisKelamin);
        textStatusKeluar = (Spinner) findViewById(R.id.textStatusKeluar);
        textStatusPegawai = (Spinner) findViewById(R.id.textStatusPegawai);
        textNidn = (EditText)findViewById(R.id.textNidn);
        textAlamatKtp = (EditText)findViewById(R.id.textAlamatKtp);
        textEmail2 = (EditText)findViewById(R.id.textEmail2);
        textNoHp2 = (EditText)findViewById(R.id.textNoHp2);
        tombol_ubah = (Button)findViewById(R.id.tombol_ubah);

        sharedPrefManager = new SharedPrefManager(this);
        mKode = sharedPrefManager.getSPKode();
        textKodePegawai.setText(mKode);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String mKodePgw = extras.getString("kodePgw");
        textKodePegawai.setText(mKodePgw);
        String mNamaPgw = extras.getString("namaPgw");
        textNamaPegawai.setText(mNamaPgw);
        String mGlrDpn = extras.getString("glrDpn");
        textGelarDepan.setText(mGlrDpn);
        String mGlrBlk = extras.getString("glrBlk");
        textGelarBelakang.setText(mGlrBlk);
        String mNik = extras.getString("nik");
        textNik.setText(mNik);
        String mFileNik = extras.getString("fileNik");
        textFileNik.setText(mFileNik);
        String mNpwp = extras.getString("npwp");
        textNpwp.setText(mNpwp);
        String mFileNpwp = extras.getString("fileNpwp");
        textFileNpwp.setText(mFileNpwp);
        String mAlamatSkr = extras.getString("alamatSkr");
        textAlamatSkr.setText(mAlamatSkr);
        String mTelpRumah = extras.getString("telpRumah");
        textTelpRumah.setText(mTelpRumah);
        String mNoHp1 = extras.getString("noHp1");
        textNoHp1.setText(mNoHp1);
        String mEmail1 = extras.getString("email1");
        textEmail1.setText(mEmail1);
        String mTempatlahir = extras.getString("tempatLahir");
        textTempatLahir.setText(mTempatlahir);
        String mTglLahir = extras.getString("tglLahir");
        textTanggalLahir.setText(mTglLahir);
        textTanggalLahir.setInputType(InputType.TYPE_NULL);
        textTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] tgl = getTanggal(textTanggalLahir.getText().toString());
                int tahun = tgl[0];
                int bulan = tgl[1]-1;
                int hari = tgl[2];
                Log.e("cetak: ", Integer.toString(bulan));
                picker = new DatePickerDialog(BiodataDosen.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textTanggalLahir.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, tahun, bulan, hari);
                picker.show();
            }
        });

        String mJenisKelamin = extras.getString("jenisKelamin");
        textJenisKelamin.setText(mJenisKelamin);
        statusKeluar = extras.getString("statusKeluar");
        initSpinnerStatusKeluar();
        statusPegawai = extras.getString("statusPegawai");
        initSpinnerStatusPegawai();
        String mNidn = extras.getString("nidn");
        textNidn.setText(mNidn);
        String mAlamatKtp = extras.getString("alamatKtp");
        textAlamatKtp.setText(mAlamatKtp);
        String mEmail2 = extras.getString("email2");
        textEmail2.setText(mEmail2);
        String mNoHp2 = extras.getString("noHp2");
        textNoHp2.setText(mNoHp2);

        textStatusKeluar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textStatusPegawai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tombol_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                ubahBiodata();
            }
        });
    }

    private void initSpinnerStatusKeluar(){
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        mApiService.getAllStatusKeluar().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        loading.dismiss();

                        JSONObject jsonResults = new JSONObject(response.body().string());
                        JSONArray result = jsonResults.getJSONArray("result");
                        List<String> str = new ArrayList<String>();
                        for (int i = 0; i<result.length(); i++) {
                            JSONObject jo = result.getJSONObject(i);
                            String status = jo.getString("status_keluar");
                            str.add(status);
                        }

                        String compareValue = statusKeluar;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                                android.R.layout.simple_spinner_item, str);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        textStatusKeluar.setAdapter(adapter);
                        if (compareValue != null) {
                            int spinnerPosition = adapter.getPosition(compareValue);
                            textStatusKeluar.setSelection(spinnerPosition);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data dosen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinnerStatusPegawai(){

        mApiService.getAllStatusPegawai().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        loading.dismiss();

                        JSONObject jsonResults = new JSONObject(response.body().string());
                        JSONArray result = jsonResults.getJSONArray("result");
                        List<String> str = new ArrayList<String>();
                        for (int i = 0; i<result.length(); i++) {
                            JSONObject jo = result.getJSONObject(i);
                            String status = jo.getString("status_pegawai");
                            str.add(status);
                        }

                        String compareValue = statusPegawai;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                                android.R.layout.simple_spinner_item, str);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        textStatusPegawai.setAdapter(adapter);
                        if (compareValue != null) {
                            int spinnerPosition = adapter.getPosition(compareValue);
                            textStatusPegawai.setSelection(spinnerPosition);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data dosen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ubahBiodata(){

        mApiService.ubahBiodataDosenRequest(textKodePegawai.getText().toString(),
                textGelarDepan.getText().toString(), textGelarBelakang.getText().toString(),
                textNik.getText().toString(), textNpwp.getText().toString(),
                textAlamatSkr.getText().toString(), textTelpRumah.getText().toString(),
                textNoHp1.getText().toString(), textEmail1.getText().toString(),
                textTempatLahir.getText().toString(), textTanggalLahir.getText().toString(),
                textStatusKeluar.getSelectedItem().toString(), textStatusPegawai.getSelectedItem().toString(),
                textNidn.getText().toString(), textAlamatKtp.getText().toString(),
                textEmail2.getText().toString(), textNoHp2.getText().toString()
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    try {
                        assert response.body() != null;
                        JSONObject jsonResults = new JSONObject(response.body().string());
                        if (jsonResults.getString("error").equals("false")) {

                            String message = jsonResults.getString("message");
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(mContext, HomeDosen.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            // jika gagal ubah biodata
                            String error_msg = jsonResults.getString("error_msg");
                            Toast.makeText(mContext, error_msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException | IOException e) {
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
}
