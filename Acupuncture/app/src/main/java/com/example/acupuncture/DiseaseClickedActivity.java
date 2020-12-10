package com.example.acupuncture;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dataclass.Pressed;
import com.example.webservice.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiseaseClickedActivity extends AppCompatActivity {

    private static String[] fmyp = new String[]{"眼睛保健","臉部美容","緩解頭痛", "改善鼻子不適",
            "改善失眠","緩解牙痛","緩解耳鳴","昏迷急救","改善口腔衛生","緩解顔面神經麻痺"};
    public float[] funcpress2 = new float[]{0,0,0,0,0,0,0,0,0,0};
    public static List<Pressed> record = new ArrayList<>();
    public List<String> disease = new ArrayList<>();
    public static int[] week = new int[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseaseclicked);


        //接收傳過來的值
        Intent intent = getIntent();
        record = (List<Pressed>) intent.getSerializableExtra("Record");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 設置返回鍵
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BarChart chart = (BarChart) findViewById(R.id.clickedChart);

        // 設置數據
        BarDataSet barDataSet = new BarDataSet(dataValue(), "");
        barDataSet.setColors(getColors());
        barDataSet.setStackLabels(fmyp);

        BarData barData = new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();  // refresh

        // 去除右下角的 Description
        chart.getDescription().setEnabled(false);
        // 去除左下圖例
//        chart.getLegend().setEnabled(false);
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

        Legend l = chart.getLegend();
        l.setTextSize(20f);
        l.setTextColor(Color.WHITE);
        l.setWordWrapEnabled(true);
        l.setXEntrySpace(12f);

        String[] aWeek = getWeek();
        pressMarker myMarkerView = new pressMarker(this, aWeek);
        myMarkerView.setChartView(chart);
        chart.setMarker(myMarkerView);
    }

    public String[] getWeek(){
        String[] day_of_week = new String[7];
        for(int i = 0; i < 7; i++){
            switch (week[i]) {
                case 1:
                    day_of_week[i] = "星期日";
                    break;
                case 2:
                    day_of_week[i] = "星期一";
                    break;
                case 3:
                    day_of_week[i] = "星期二";
                    break;
                case 4:
                    day_of_week[i] = "星期三";
                    break;
                case 5:
                    day_of_week[i] = "星期四";
                    break;
                case 6:
                    day_of_week[i] = "星期五";
                    break;
                case 7:
                    day_of_week[i] = "星期六";
            }
        }
        return day_of_week;
    }
    private ArrayList<BarEntry> dataValue() {

        ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();

        for(int j= -6;j<1;j++) {

            everypress(j);

            for(int p = 0; p < 10; p++){
                float a = funcpress2[0];
                float b = funcpress2[1];
                float c = funcpress2[2];
                float d = funcpress2[3];
                float e = funcpress2[4];
                float f = funcpress2[5];
                float g = funcpress2[6];
                float h = funcpress2[7];
                float i = funcpress2[8];
                float k = funcpress2[9];
                 dataVals.add(new BarEntry(j+7, new float[]{a,b,c,d,e,f,g,h,i,k}));

                Log.v("qqq", fmyp[p]);
                Log.v("qqq", String.valueOf(funcpress2[p]));
//                pressNumber temp = new pressNumber(p, fmyp[p]);
//                totalPressTime.add(temp);
            }

            //統計有沒有數據超標用的
//            statistics(funcpress2);

            //清空數值不然會影響到下一個
            for(int count = 0; count < 10; count++){
                funcpress2[count]=0;
            }
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
    //取得前七天的日期
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // distanceDay 為 -6~0，week 陣列從 distanceDay+6 (0~6)
        week[distanceDay+6] = date.get(Calendar.DAY_OF_WEEK);
        return dft.format(endDate);
    }

    public void everypress(int j){
            for (int i = 0; i <record.size();i++){
                if (getOldDate(j).compareTo(record.get(i).date) == 0) {
                    Log.v("fuck2", "查證用"+getOldDate(j));
                    Log.v("fuck2","查證用"+record.get(i).date);
                    Log.v("fuck2","查證用"+record.get(i).times);
                    Log.v("fuck2","查證用"+record.get(i).func);
                    Log.v("fuck2","------------------------");
                    cate(record.get(i).date, record.get(i).func, record.get(i).times);
                } else {
                    cate2(record.get(i).date, record.get(i).func);
                }
            }

        return;
    }
    public void cate2( String date , String func) {
        for (int j = 0; j < fmyp.length; j++) {
            if ((fmyp[j]).equals(func)) {
                funcpress2[j] = 0+ funcpress2[j];
            }
        }
    }

    public void cate( String date , String func, int time2){
        for (int j = 0; j < fmyp.length;j++){
            if ((fmyp[j]).equals(func)) {
                funcpress2[j] = time2 +funcpress2[j];
//                Log.v("fuck2","查證用"+record.get(j).date);
//                Log.v("fuck2","查證用"+record.get(j).times);
//                Log.v("fuck2","查證用---"+record.get(j).func);
//                Log.v("fuck2","查證用000000"+fmyp[j]);
            }
        }
        return;
    }

//    public void statistics(float[] tmp_funpress){
//        for (int j = 0; j<fmyp.length; j++){
//            if (tmp_funpress[j] == 0){
//                staticdata[j] = 0 + staticdata[j];
//            } else {
//                staticdata[j] = 1 + staticdata[j];
//            }
//            if (staticdata[j] > 1 ){
//                if (disease.contains(fmyp[j])){
//                } else {
//                    disease.add(fmyp[j]);
//                    doctorFragment.disease2.add(fmyp[j]);
//                }
//            }
//            Log.v("oomg!!!!!!!!!!!", String.valueOf(disease));
//        }
//        return;
//    }

    private int[] getColors() {
        int[] colors = new int[10];
        colors[0] = getResources().getColor(R.color.nose);
        colors[1] = getResources().getColor(R.color.head);
        colors[2] = getResources().getColor(R.color.sleep);
        colors[3] = getResources().getColor(R.color.teeth);
        colors[4] = getResources().getColor(R.color.pretty);
        colors[5] = getResources().getColor(R.color.eye);
        colors[6] = getResources().getColor(R.color.ear);
        colors[7] = getResources().getColor(R.color.stun);
        colors[8] = getResources().getColor(R.color.mouth);
        colors[9] = getResources().getColor(R.color.face);
        return colors;
    }
}

class pressMarker extends MarkerView {

    private final TextView tvContent;
    private String[] week;

    public pressMarker(Context context, String[] week) {
        super(context, R.layout.view_marker);
        this.week = week;

        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(week[(int) e.getX()-1] + "\n" + Utils.formatNumber(ce.getHigh(), 0, false));
        } else {
            tvContent.setText(week[(int) e.getX()-1] + "\n" + Utils.formatNumber(((BarEntry) e).getYVals()[highlight.getStackIndex()], 0, false)  + " 次");
        }

        super.refreshContent(e, highlight);
    }

    private MPPointF mpPointF;

    @Override
    public MPPointF getOffset() {

        if(mpPointF == null){
            mpPointF = new MPPointF(-(getWidth() / 2), -getHeight());
        }
        return mpPointF;
    }
}