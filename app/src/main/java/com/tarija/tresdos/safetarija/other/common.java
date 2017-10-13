package com.tarija.tresdos.safetarija.other;

import com.tarija.tresdos.safetarija.remote.ApiService;
import com.tarija.tresdos.safetarija.remote.retrofitClient;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class common {
    public  static String fcmtoken = "ceWqHb67xmg:APA91bFKEfJE9cngRIodPrBesY3-0Or06fIn4Q9-Rb2BZMPPhSWeaKCeO3px-aHeRLhRIc5Xpzslh7t8k5mlxFiMQL9WRJKFTmFkQsY6wU_CoAgriD_96GrWE2KlEZjH93fkb-feld22";
    private static String baseurl = "https://fcm.googleapis.com/";
    public  static ApiService getFCMClient(){
        return retrofitClient.getClient(baseurl).create(ApiService.class);
    }
}
