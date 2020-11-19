package com.example.screenTime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static com.example.screenTime.AppNotify.CHANNEL_Screen_ID;

import com.example.acupuncture.MainActivity;
import com.example.acupuncture.R;

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Build a PendingIntent with a back stack
        // Create an Intent for the activity you want to start
        Intent toFragment= new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("toValue", "faceFragment");
        PendingIntent pend = PendingIntent.getActivity(context, 0, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);

        // ------------------+
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // do notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_Screen_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // 設置圖標
                .setContentTitle("來自穴經驗的關懷") // 設置 notify title
                .setContentText("您使用超時了唷！建議您休息一下:3～") // 設置 notify msg
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.panda)
                .setContentIntent(pend)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
}
