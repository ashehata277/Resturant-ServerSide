package com.example.chatfirebase;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FCMinstanceIdservice extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        String RecentlyToken= FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("FCM_Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCM_Token",RecentlyToken);
        editor.apply();
        Log.d("REGISTRATION_TOKEN",RecentlyToken);
    }
}
