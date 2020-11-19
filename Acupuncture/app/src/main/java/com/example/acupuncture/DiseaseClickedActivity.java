package com.example.acupuncture;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.dataclass.Acup;
import com.example.dataclass.Pressed;
import com.example.dataclass.Que;
import com.example.webservice.Acupuncture;
import com.example.webservice.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class DiseaseClickedActivity extends AppCompatActivity {

    int[] colorClassArray = new int[]{0xFF616621, 0xFFAC7D87, 0xFFE5CC53};  // 542200 2D1200
    private int count = 7;
    private String q;
    public static List<Pressed> record = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseaseclicked);

        if (!User.recordIsGotten) {
            User.pressedCount(getApplicationContext());
        }

        //接收傳過來的值
        Intent intent = getIntent();
        List<Pressed> record = (List<Pressed>) intent.getSerializableExtra("Record");
        Log.v("la;a", "recordtimes3 is"+record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 設置返回鍵
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BarChart chart = (BarChart) findViewById(R.id.clickedChart);

        // 設置數據
        BarDataSet barDataSet = new BarDataSet(dataValue(), "data");
        barDataSet.setColors(colorClassArray);

        BarData barData = new BarData(barDataSet);
        chart.setData(barData);
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
        barDataSet.setDrawValues(false);

        XAxis xAxis = chart.getXAxis();
        // x 軸顯示在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));
        // 去除 x 軸
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(12);
        yAxis.setTextColor(Color.parseColor("#FFFFFF"));
        yAxis.setAxisMinimum(0f);

        chart.setScaleEnabled(false);  // 圖表禁止縮放
        Log.v("la;a", "444444recordtimes is"+record);
    }

    private ArrayList<BarEntry> dataValue() {
        ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();
        for(int i = 1; i <= count; i++) {
            float y = (float) Math.random() * 5;
            dataVals.add(new BarEntry(i, new float[]{y, y, y}));
//            Log.v("la;a", "recordtimes1 is"+record);
        }
        return dataVals;
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