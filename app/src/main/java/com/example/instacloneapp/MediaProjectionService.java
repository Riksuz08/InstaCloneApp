package com.example.instacloneapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MediaProjectionService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Your foreground service code here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();

            Intent intent1 = new Intent(this, MainActivity.class);
            String channelId = "my_channel";
            NotificationChannel channel = new NotificationChannel(channelId, "My Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
            Notification notification = new NotificationCompat.Builder(this, "Channel1")
                    .setContentText("Instagram is running")
                    .setContentTitle("Foreground Service")
                    .setSmallIcon(R.mipmap.icon)
//                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
        }


        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                "Channel1",
                "Foreground Service",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }
}