package com.example.acupuncture;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ScreenTimeActivity extends AppCompatActivity {

    final int COUNT = 7;
    private BarDataSet bar;
    private View overtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 設置返回鍵
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BarChart chart = (BarChart) findViewById(R.id.barChart);
        bar = new BarDataSet(getPoints(), "Label 1");
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
        // 去除上方數值
        bar.setDrawValues(false);

        // 設定 x 軸
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // x 軸顯示在下方
        xAxis.setTextSize(12);  // x 軸文字大小
        xAxis.setTextColor(Color.parseColor("#00251a"));  // x 軸文字顏色
        // 去除 x 軸
        xAxis.setDrawAxisLine(false);

        // 設定左側 y 軸
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(12);
        yAxis.setTextColor(Color.parseColor("#00251a"));
        yAxis.setAxisMinimum(0f);

        chart.setScaleEnabled(false);  // 圖表禁止縮放

        // 超時設定(NumberPicker)
        overtime = findViewById(R.id.overtime);
        overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View vi = inflater.inflate(R.layout.activity_time_set, null);
                NumberPicker picker = (NumberPicker) vi.findViewById(R.id.numberPicker);
                picker.setMaxValue(60);
                picker.setMinValue(20);
                picker.setValue(40);
                // 數字變化的監聽事件
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {  // newVal 當前值
                        // ......
                    }
                });

                // 將 NumberPicker 設置成 AlertDialog
                new AlertDialog.Builder(v.getContext())
                        .setTitle("請設置時間")
                        .setView(vi)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 點選確定後......
                            }
                        })
                        .show();
            }
        });
    }
    // x, y 軸資料
    private List<BarEntry> getPoints() {
        List<BarEntry> points = new ArrayList<>();
        for(int i = 1; i <= COUNT; i++) {
            float y = (float) Math.random() * 20;
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
}