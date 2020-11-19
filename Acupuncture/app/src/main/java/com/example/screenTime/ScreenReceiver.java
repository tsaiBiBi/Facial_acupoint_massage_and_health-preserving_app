package com.example.screenTime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = true;
        }
    }
}
