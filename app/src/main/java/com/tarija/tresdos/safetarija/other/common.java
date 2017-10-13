package com.tarija.tresdos.safetarija.other;

import com.tarija.tresdos.safetarija.remote.ApiService;
import com.tarija.tresdos.safetarija.remote.retrofitClient;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class common {
    private static String baseurl = "https://fcm.googleapis.com/";
    public  static ApiService getFCMClient(){
        return retrofitClient.getClient(baseurl).create(ApiService.class);
    }
}
