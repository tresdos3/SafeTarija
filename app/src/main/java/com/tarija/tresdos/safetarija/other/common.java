package com.tarija.tresdos.safetarija.other;

import com.tarija.tresdos.safetarija.remote.ApiService;
import com.tarija.tresdos.safetarija.remote.retrofitClient;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class common {
    public  static String fcmtoken = "d7qvVeKLs4k:APA91bHvnETCXj-6qvMxo3ArGsQqhW3Faa4W687QOJBpBjYma7H8xaabTj-KaHzDRRpNqm6ZqzVz5ZeGsb7i32LfQ3ID3X3ski-eowG_jKMCOXlHNSEXtu56TukL2CdENT-NJ2dQVPon";
    private static String baseurl = "https://fcm.googleapis.com/";
    public  static ApiService getFCMClient(){
        return retrofitClient.getClient(baseurl).create(ApiService.class);
    }
}
