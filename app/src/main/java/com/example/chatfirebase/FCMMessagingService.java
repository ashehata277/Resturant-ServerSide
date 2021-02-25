package com.example.chatfirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMMessagingService extends FirebaseMessagingService
{
    NotificationManager noti;
    String ChannelID = "Firebase_Channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.i("Before","Before");
        String title =remoteMessage.getNotification().getTitle();
        String Body  =remoteMessage.getNotification().getBody();
        Log.i("after",title);
        noti =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.sm);
        createNotificationChannel(ChannelID,"FirebaseTest","Example New Channel");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,ChannelID)
                .setContentTitle(title)
                .setContentText(Body)
                .setSmallIcon(R.drawable.sm)
                .setLargeIcon(bmp)
                .setAutoCancel(true)
                .setNumber(1);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setVibrate(new long[] {500,1000,500,1000,500});
        noti.notify(100,builder.build());

    }
    protected void createNotificationChannel(String id, String name,String description)
    {

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel =
                new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(
                new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        noti.createNotificationChannel(channel);
    }
}
