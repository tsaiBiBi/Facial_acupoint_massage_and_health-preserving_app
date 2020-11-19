package com.example.screenTime;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppNotify extends Application {
    public static final String CHANNEL_Screen_ID = "screenControl";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // PRIORITY_MAX、PRIORITY_HIGHT、PRIORITY_LOW、PRIORITY_MIN、PRIORITY_DEFAULT
            NotificationChannel channel_Screen = new NotificationChannel(
                    CHANNEL_Screen_ID,
                    "Channel Screen",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_Screen.setDescription("This is Channel Screen");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel_Screen);
        }
    }
}