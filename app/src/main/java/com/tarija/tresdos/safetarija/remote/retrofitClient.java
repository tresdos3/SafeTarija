package com.tarija.tresdos.safetarija.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class retrofitClient {
    private static Retrofit retrofit = null;
    public  static Retrofit getClient(String Baseurl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Baseurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
