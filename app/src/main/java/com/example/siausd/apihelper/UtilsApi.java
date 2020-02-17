package com.example.siausd.apihelper;

public class UtilsApi {

    //public static final String BASE_URL_API = "http://172.23.3.10/cobasia/";
    public static final String BASE_URL_API = "http://172.23.2.57/cobasia/";

    public static BaseApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

}
