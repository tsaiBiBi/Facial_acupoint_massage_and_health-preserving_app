package com.example.acupuncture;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class DiseaseClickedActivity extends AppCompatActivity {

    int[] colorClassArray = new int[]{0xFF554499, 0xFFabcd44, 0xFF789542};
    private int count = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseaseclicked);

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
        xAxis.setTextColor(Color.parseColor("#00251a"));
        // 去除 x 軸
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(12);
        yAxis.setTextColor(Color.parseColor("#00251a"));
    }

    private ArrayList<BarEntry> dataValue() {
        ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();
        for(int i = 1; i <= count; i++) {
            float y = (float) Math.random() * 5;
            dataVals.add(new BarEntry(i, new float[]{y, y, y}));
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