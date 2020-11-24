package com.example.acupuncture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dataclass.AppUsageInfo;
import com.example.dataclass.usageTime;
import com.example.screenTime.ScreenNotifyService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenTimeActivity extends AppCompatActivity {

    final int COUNT = 7;
    private BarDataSet bar;
    private View overtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_time);


        // 時間存取
        long[] totalUsageTime = new long[7];

        List<usageTime> totalTimeFormat = new ArrayList<>(7);

        try {
            for (int i = 6; i >= 0; i--) {
                Calendar[] startEnd = getPastDate(i);
                long start_time = startEnd[0].getTimeInMillis();
                long end_time = startEnd[1].getTimeInMillis();
                int week = startEnd[0].get(Calendar.DAY_OF_WEEK);
                if (i == 0) {
                    totalUsageTime[6 - i] = getUsageStatistics(start_time, System.currentTimeMillis());
                } else {
                    totalUsageTime[6 - i] = getUsageStatistics(start_time, end_time);
                }
                String tmp = converLongToTimeChar(totalUsageTime[6 - i]);

                usageTime temp = new usageTime(week, tmp);
                totalTimeFormat.add(temp);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // ----------------
        final TextView textTime = findViewById(R.id.textTime);
        final AlertDialog.Builder dialog_time = new AlertDialog.Builder(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 設置返回鍵
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BarChart chart = (BarChart) findViewById(R.id.barChart);
        bar = new BarDataSet(getPoints(totalUsageTime), "Label 1");
        /*bar.setBarBorderColor(Color.parseColor("#ffab40"));
        bar.setBarBorderWidth(1);*/
        bar.setColor(Color.parseColor("#ffab40"));  // bar 顏色
        bar.setHighLightAlpha(37);  // 點選後的透明度
        // 圖表背景色
        /*chart.setDrawGridBackground(true);
        chart.setGridBackgroundColor(Color.parseColor("#654321"));*/

        List<IBarDataSet> bars = new ArrayList<>();
        bars.add(bar);
        // 設置數據
        chart.setData(new BarData(bars));
        chart.invalidate();  // refresh

        // 去除右下角的 Description
        chart.getDescription().setEnabled(false);
        // 去除左下圖例
        chart.getLegend().setEnabled(false);
        // 去除網格線
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        // 去除左邊線
        chart.getAxisLeft().setDrawAxisLine(false);
        // 去除右側 y 軸
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        // 去除上方數值
        bar.setDrawValues(false);

        // 設定 x 軸
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // x 軸顯示在下方
        xAxis.setTextSize(12);  // x 軸文字大小
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));  // x 軸文字顏色
        // 去除 x 軸
        xAxis.setDrawAxisLine(false);

        // 設定左側 y 軸
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(12);
        yAxis.setTextColor(Color.parseColor("#FFFFFF"));
        yAxis.setAxisMinimum(0f);
        chart.setScaleEnabled(false);  // 圖表禁止縮放

        final Intent intent = new Intent(this, ScreenNotifyService.class);

        // 超時設定(NumberPicker)
        overtime = findViewById(R.id.overtime);
        overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View vi = inflater.inflate(R.layout.activity_time_set, null);
                final NumberPicker picker = (NumberPicker) vi.findViewById(R.id.numberPicker);

                picker.setMaxValue(60);
                picker.setMinValue(1);
                picker.setValue(40);
                picker.setWrapSelectorWheel(false);
                // 數字變化的監聽事件
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {  // newVal 當前值
                        // ......
                    }
                });
                // 將 NumberPicker 設置成 AlertDialog
                dialog_time.setView(picker);
                dialog_time.setTitle("請設置時間")
                        .setView(vi)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 點選確定後
                                // Toast.makeText(this, " " + which, Toast.LENGTH_SHORT).show();
                                int restrict_time = picker.getValue();
                                intent.putExtra("restrict_time", restrict_time);
                                startService(intent);
                            }
                        }).show();
            }
        });

        // 啟用 Marker View
        chart.setDrawMarkers(true);
        // 設置 Marker View
        chart.setMarker(new usageTimeMarker(this, totalTimeFormat));
    }
    // x, y 軸資料
    private List<BarEntry> getPoints(long[] totalUsageEventTime) {
        List<BarEntry> points = new ArrayList<>();

        for (int i = 1; i <= COUNT; i++) {
//            float y = (float) Math.random() * 20;
            float y = (float) (Math.round(totalUsageEventTime[i-1]));
            points.add(new BarEntry(i, y));
        }
        return points;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 點擊返回鍵，關閉當前活動
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Calendar[] getPastDate(int past) {
        // past 是指要過去幾天
        Calendar[] pastDays = new Calendar[2];
        pastDays[0] = Calendar.getInstance();
        pastDays[0].set(Calendar.DAY_OF_YEAR, pastDays[0].get(Calendar.DAY_OF_YEAR) - past);
        pastDays[0].set(Calendar.HOUR_OF_DAY, 0);
        pastDays[0].set(Calendar.SECOND, 0);
        pastDays[0].set(Calendar.MINUTE, 0);
        pastDays[0].set(Calendar.MILLISECOND, 0);

        pastDays[1] = Calendar.getInstance();
        pastDays[1].set(Calendar.DAY_OF_YEAR, pastDays[1].get(Calendar.DAY_OF_YEAR) - (past-1));
        pastDays[1].set(Calendar.HOUR_OF_DAY, 0);
        pastDays[1].set(Calendar.SECOND, 0);
        pastDays[1].set(Calendar.MINUTE, 0);
        pastDays[1].set(Calendar.MILLISECOND, 0);
        return pastDays;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    long getUsageStatistics(long start_time, long end_time) throws PackageManager.NameNotFoundException {
        long sum = 0;
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
                    PackageManager pm = getApplicationContext().getPackageManager();
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(key, PackageManager.GET_META_DATA);
                    String appName = (String) pm.getApplicationLabel(applicationInfo);
                    if (map.get(key) == null) {  // 如果這個 key 尚未存在在 map 裡，就放進 map 跟 sameEvents 裡
                        map.put(key, new AppUsageInfo(key, appName));
                        sameEvents.put(key, new ArrayList<UsageEvents.Event>());
                    }
                    sameEvents.get(key).add(currentEvent); // sameEvents 裡就會有同一個 key(packageName) 的所有事件
                }
            }

            // 由於 start_time 是星期一，所以他會從星期一來算，但還是要避免他星期一都沒用手機第一筆就是星期二
            // 先建立一個星期幾的數字
            // Traverse through each app data which is grouped together and count launch, calculate duration
            // 利用HashMap的entrySet()方法取得所有資料
            for (Map.Entry<String, List<UsageEvents.Event>> entry : sameEvents.entrySet()) {
                int totalEvents = entry.getValue().size();
                if (totalEvents > 1) {
                    for (int i = 0; i < totalEvents - 1; i++) {
                        // 某事件跟這個事件的下一個事件
                        UsageEvents.Event E0 = entry.getValue().get(i);
                        UsageEvents.Event E1 = entry.getValue().get(i + 1);

                        // 這個應用程式結束的時間減掉開始時間，就是使用時間 MOVE_TO_BACKGROUND = 2
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
                    long diff = end_time - entry.getValue().get(totalEvents - 1).getTimeStamp();
                    map.get(entry.getValue().get(totalEvents - 1).getPackageName()).timeInForeground += diff;
                }
            }

            ArrayList<AppUsageInfo> smallInfoList = new ArrayList<>(map.values());

            String strMsg = "";
            // Concatenating data to show in a text view. You may do according to your requirement
            for (AppUsageInfo appUsageInfo : smallInfoList) {
                // Do according to your requirement
                strMsg = strMsg.concat(appUsageInfo.appName + " : " + converLongToTimeChar(appUsageInfo.timeInForeground) + "\n\n");
                sum += appUsageInfo.timeInForeground;
            }

            Log.i("UsageEvent", converLongToTimeChar(sum));
        } else {
            Toast.makeText(this, "Sorry...", Toast.LENGTH_SHORT).show();
        }
        return sum;
    }

    public String converLongToTimeChar(long usedTime) {
        String hour = "", min = "", sec = "";

        int h = (int) (usedTime / 1000 / 60 / 60);
        if (h != 0)
            hour = h + "h ";

        int m = (int) ((usedTime / 1000 / 60) % 60);
        if (m != 0)
            min = m + "m ";
        return hour + min;
    }
}

class usageTimeMarker extends MarkerView {

    private TextView contentView;
    private List<usageTime> weekUsage;

    public usageTimeMarker(Context context, List<usageTime> weekUsage) {
        // 設置布局文件
        super(context, R.layout.view_marker);
        this.weekUsage = weekUsage;
        contentView = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        // 不減一的話，最後一天無法點擊框框
        usageTime item = weekUsage.get((int) e.getX()-1);
        contentView.setText(item.getWeek() + "\n" + item.getTotalTime());
        super.refreshContent(e, highlight);
    }

    // 設置偏移量
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(float) (getWidth() / 2), -getHeight() - 10);
    }
}
