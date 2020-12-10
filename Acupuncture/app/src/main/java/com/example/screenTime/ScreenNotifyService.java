package com.example.screenTime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.dataclass.AppUsageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenNotifyService extends Service {
    public ScreenNotifyService() {
    }

    public static long totalUsageTime = 0;

    boolean notifiedYet = false;   // 提醒過了沒，提醒過了就不要再提醒了
    int restrict; // 使用者自行輸入的時間
    boolean timeIsSet = false;  // 初始時間設置過了沒
    long start_time, end_time;
    BroadcastReceiver mReceiver = new ScreenReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static final String TAG = "ScreenTimeService";

    @Override
    public void onCreate() {
        super.onCreate();

        // 要偵測螢幕是否已關閉來停止使用，就要從註冊廣播並接收資訊
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 確認時間是否設置過
                if (!timeIsSet) {
                    start_time = System.currentTimeMillis(); // 目前時間
                    end_time = start_time + 1000 * 60 * 60;  // 一小時內使用情況
                    timeIsSet = true;
                    Log.e("timeIsSet", "??????????????????????? " + start_time + "...." + end_time);
                } else {
                    Log.e("timeIsSet", "-----------------------  " + start_time + "...." + end_time);
                }

                // Activity 傳來的 restrict time
                try {
                    if (intent.hasExtra("restrict_time")) {
                        restrict = intent.getIntExtra("restrict_time", 60);
                        Log.e("限制時間查看", "restrict time isnt null:" + restrict);
                    } else {
                        Log.e("限制時間查看", "restrict time is null:");
                    }
                } catch (NumberFormatException e) {
                    Log.e("限制時間查看", "NumberFormatException");
                } catch (NullPointerException a) {
                    Log.e("限制時間查看", "NullPointerException");
                }

                if (getUsageStatisticsOver(start_time, end_time, restrict)) {  // 如果目前使用時間超過的話
                    // 就進行一個提醒的動作
                    if (!notifiedYet) {  // 時間內還沒提醒過
                        Intent intent = new Intent(getApplicationContext(), NotificationPublisher.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), restrict*60*1000, pendingIntent);
                        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), restrict*60*1000, pendingIntent);
                        Log.i("提醒出來", "notification!");
                        notifiedYet = true;
                    } else {  // 時間內已經提醒過了
                        // 如果有停止使用 (關閉螢幕)  // 去確認使用者是否有停止使用手機
                        if (!ScreenReceiver.wasScreenOn) {
                            Log.i(TAG, "screen off!");
                            notifiedYet = false;
                            timeIsSet = false;
                            totalUsageTime = 0;
                        }
                    }
                }else{
                    Log.i("限制時間", "沒有超過時間!");
                }
            }
        }).start();  // 要 start 才會執行 thread 唷

        AlarmManager alarmManage = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime;  // 此為任務觸發時間，elapseRealtime 可以獲取到系統開機至今所經歷時間的毫秒數
        // 如果有休息過！
        triggerAtTime = SystemClock.elapsedRealtime() + 1 * 60 * 1000;   // 五分鐘後再去偵測是否有超時的現象
        Intent i = new Intent(this, ScreenNotifyService.class);
        // PendingIntent 就是一個 Intent 的描述，我們可以把這個描述交給別的程式，別的程式根據這個描述在後面的別的時間做你安排做的事情
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        alarmManage.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    boolean getUsageStatisticsOver(long start_time, long end_time, long restrict) {
        long restrict_Time = 1000 * 60 * restrict; // Restrict Time In Milliseconds
        totalUsageTime = 0;
        UsageEvents.Event currentEvent;
        //  List<UsageEvents.Event> allEvents = new ArrayList<>();
        HashMap<String, AppUsageInfo> map = new HashMap<>();
        HashMap<String, List<UsageEvents.Event>> sameEvents = new HashMap<>(); // 前面是 package 名稱，後面是 usageEvents

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        if (mUsageStatsManager != null) {
            // Get all apps data from starting time to end time
            // 所以 UsageEvents 儲存的資料就是時間範圍內所有的 app 資料
            UsageEvents usageEvents = mUsageStatsManager.queryEvents(start_time, end_time);

            // Put these data into the map
            while (usageEvents.hasNextEvent()) {
                currentEvent = new UsageEvents.Event();
                usageEvents.getNextEvent(currentEvent);
                if (currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND ||
                        currentEvent.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    //  allEvents.add(currentEvent);
                    String key = currentEvent.getPackageName();  // map 的 key 是這個事件應用程式的名字
                    try {
                        PackageManager pm = getApplicationContext().getPackageManager();
                        ApplicationInfo applicationInfo = pm.getApplicationInfo(key, PackageManager.GET_META_DATA);
                        String appName = pm.getApplicationLabel(applicationInfo).toString();
                        if (map.get(key) == null) {  // 如果這個 key 尚未存在在 map 裡，就放進 map 跟 sameEvents 裡
                            map.put(key, new AppUsageInfo(key, appName));
                            sameEvents.put(key, new ArrayList<UsageEvents.Event>());
                        }
                        sameEvents.get(key).add(currentEvent); // sameEvents 裡就會有同一個 key(packageName) 的所有事件
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e("Screen Time PackageManager", "......");
                    }
                }
            }

            // Traverse through each app data which is grouped together and count launch, calculate duration
            // 利用HashMap的entrySet()方法取得所有資料
            for (Map.Entry<String, List<UsageEvents.Event>> entry : sameEvents.entrySet()) {
                int totalEvents = entry.getValue().size();
                if (totalEvents > 1) {
                    for (int i = 0; i < totalEvents - 1; i++) {
                        UsageEvents.Event E0 = entry.getValue().get(i);
                        UsageEvents.Event E1 = entry.getValue().get(i + 1);

                        // 這個應用程式結束的時間減掉開始時間，就是使用時間 ACTIVITY_PAUSED = 2
                        if (E0.getEventType() == 1 && E1.getEventType() == 2) {
                            long diff = E1.getTimeStamp() - E0.getTimeStamp();
                            map.get(E0.getPackageName()).timeInForeground += diff;
                        }
                    }
                }

                // If First eventtype is ACTIVITY_PAUSED then added the difference of start_time and Event occuring time because the application is already running.
                // 第一個
                if (entry.getValue().get(0).getEventType() == 2) {
                    long diff = entry.getValue().get(0).getTimeStamp() - start_time;
                    map.get(entry.getValue().get(0).getPackageName()).timeInForeground += diff;
                }

                // If Last eventtype is ACTIVITY_RESUMED then added the difference of end_time and Event occuring time because the application is still running .
                // 最後一個
                if (entry.getValue().get(totalEvents - 1).getEventType() == 1) {
                    long diff = System.currentTimeMillis() - entry.getValue().get(totalEvents - 1).getTimeStamp();
                    map.get(entry.getValue().get(totalEvents - 1).getPackageName()).timeInForeground += diff;
                }
            }

            ArrayList<AppUsageInfo> smallInfoList = new ArrayList<>(map.values());

            // Concatenating data to show in a text view. You may do according to your requirement
            for (AppUsageInfo appUsageInfo : smallInfoList) {
                // Do according to your requirement
                totalUsageTime += appUsageInfo.timeInForeground;
            }
        } else {
            Toast.makeText(this, "Sorry...", Toast.LENGTH_SHORT).show();
        }
        Log.e("總時間查看！！！", ".:" + totalUsageTime + " restrict:" + restrict_Time);

        return totalUsageTime >= restrict_Time;  // 如果超過時間就回傳 true
    }

}
