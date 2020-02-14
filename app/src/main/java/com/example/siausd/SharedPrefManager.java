package com.example.siausd;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_SIA_USD_APP = "spSIAUSDApp";

    public static final String SP_NIM = "spNim";

    public static final String SP_KODE = "spKode";

    public static final String SP_USER = "spUser";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_SIA_USD_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNim(){
        return sp.getString(SP_NIM, "");
    }

    public String getSPKode(){
        return sp.getString(SP_KODE, "");
    }

    public String getSpUser() {return sp.getString(SP_USER, "");}

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

}
