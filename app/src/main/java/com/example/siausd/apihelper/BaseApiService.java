package com.example.siausd.apihelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaseApiService {

    // Fungsi untuk memanggil API http://localhost/sia/Login.php
    @FormUrlEncoded
    @POST("LoginMahasiswa.php")
    Call<ResponseBody> loginMhsRequest(@Field("nim") String nim,
                                    @Field("password") String password);


    // Fungsi untuk memanggil API http://localhost/sia/UbahPassword.php
    @FormUrlEncoded
    @POST("UbahPasswordMhs.php")
    Call<ResponseBody> ubahPassMhsRequest(@Field("nim") String nim,
                                       @Field("passwordlama") String passwordlama,
                                       @Field("passwordbaru") String passwordbaru,
                                       @Field("konfpassword") String konfpassword);

    // Fungsi untuk memanggil API http://localhost/sia/UbahBiodata.php
    @FormUrlEncoded
    @POST("UbahBiodataMhs.php")
    Call<ResponseBody> ubahBiodataRequest(@Field("nim") String nim,
                                          @Field("kodeKabLahir") String kodeKabLahir,
                                          @Field("tempatLahir") String tempatLahir,
                                          @Field("tglLahir") String tglLahir,
                                          @Field("alamatSkr") String alamatSkr,
                                          @Field("kodeKabSkr") String kodeKabSkr,
                                          @Field("kodePosSkr") String kodePosSkr,
                                          @Field("alamatAsal") String alamatAsal,
                                          @Field("kodeKabAsal") String kodeKabAsal,
                                          @Field("kodePosAsal") String kodePosAsal,
                                          @Field("namaAyah") String namaAyah,
                                          @Field("email") String email,
                                          @Field("noHp") String noHp,
                                          @Field("nisn") String nisn,
                                          @Field("nik") String nik,
                                          @Field("tglLahirAyah") String tglLahirAyah,
                                          @Field("namaIbu") String namaIbu,
                                          @Field("tglLahirIbu") String tglLahirIbu,
                                          @Field("nikAyah") String nikAyah,
                                          @Field("nikIbu") String nikIbu);

    // Fungsi untuk memanggil API http://localhost/sia/TampilBiodata.php
    @GET("TampilBiodataMhs.php")
    Call<ResponseBody> tampilBiodataMhsRequest(@Query("nim") String nim);

    // Fungsi untuk memanggil API http://localhost/sia/TampilAllKabupaten.php
    @GET("TampilAllKabupaten.php")
    Call<ResponseBody> getAllKabupaten();

    //-------------------------------- Dosen ----------------------------------------

    // Fungsi untuk memanggil API http://localhost/sia/Login.php
    @FormUrlEncoded
    @POST("LoginDosen.php")
    Call<ResponseBody> loginDosenRequest(@Field("user") String user,
                                    @Field("password") String password);


    // Fungsi untuk memanggil API http://localhost/sia/UbahPassword.php
    @FormUrlEncoded
    @POST("UbahPasswordDosen.php")
    Call<ResponseBody> ubahPassDosenRequest(@Field("user") String user,
                                       @Field("passwordlama") String passwordlama,
                                       @Field("passwordbaru") String passwordbaru,
                                       @Field("konfpassword") String konfpassword);

    // Fungsi untuk memanggil API http://localhost/sia/TampilBiodata.php
    @GET("TampilBiodataDosen.php")
    Call<ResponseBody> tampilBiodataDosenRequest(@Query("kode_pegawai") String kode_pegawai);

    // Fungsi untuk memanggil API http://localhost/sia/TampilAllStatusKeluar.php
    @GET("TampilAllStatusKeluar.php")
    Call<ResponseBody> getAllStatusKeluar();

    // Fungsi untuk memanggil API http://localhost/sia/TampilAllStatusPegawai.php
    @GET("TampilAllStatusPegawai.php")
    Call<ResponseBody> getAllStatusPegawai();

    // Fungsi untuk memanggil API http://localhost/sia/UpdateBiodata.php
    @FormUrlEncoded
    @POST("UbahBiodataDosen.php")
    Call<ResponseBody> ubahBiodataDosenRequest(@Field("kode_pegawai") String kode_pegawai,
                                            @Field("glr_dpn") String glr_dpn,
                                            @Field("glr_blk") String glr_blk,
                                            @Field("nik") String nik,
                                            @Field("npwp") String npwp,
                                            @Field("alamat_skr") String alamat_skr,
                                            @Field("telp_rumah") String telp_rumah,
                                            @Field("no_hp1") String no_hp1,
                                            @Field("email1") String email1,
                                            @Field("tempat_lahir") String tempat_lahir,
                                            @Field("tgl_lahir") String tgl_lahir,
                                            @Field("nama_status_keluar") String nama_status_keluar,
                                            @Field("nama_stats_pegawai") String nama_stats_pegawai,
                                            @Field("nidn") String nidn,
                                            @Field("alamat_ktp") String alamat_ktp,
                                            @Field("email2") String email2,
                                            @Field("no_hp2") String no_hp2);

}
