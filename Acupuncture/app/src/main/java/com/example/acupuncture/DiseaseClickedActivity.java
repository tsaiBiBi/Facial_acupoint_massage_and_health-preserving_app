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
import com.example.dataclass.pressNumber;
import com.example.webservice.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiseaseClickedActivity extends AppCompatActivity {

//    int[] colorClassArray = new int[]{0xFF616621, 0xFFAC7D87, 0xFF616621,0xFFAC7D87,0x542200
//            ,0x2D1200,0xE7655A,0xF6CE5D,0xBAE6FB,0x542200};  // 542200 2D1200
    private static String[] fmyp = new String[]{"眼睛保健","臉部美容","緩解頭痛", "改善鼻子不適",
            "改善失眠","緩解牙痛","緩解耳鳴","昏迷急救","改善口腔衛生","緩解顔面神經麻痺"};
    private int count = 7;
//    private List<String> times2 = new ArrayList<String>();;
//    String[] funcpress2 = new String[10];
    private float[] staticdata = new float[10];
    public float[] funcpress2 = new float[]{0,0,0,0,0,0,0,0,0,0};
    public static List<Pressed> record = new ArrayList<>();
    public List<String> disease = new ArrayList<>();
    private List<pressNumber> totalPressTime = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseaseclicked);

        // 從資料庫去拿所有資料
        if (!User.recordIsGotten) {
            User.pressedCount(getApplicationContext());
        }

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
        BarDataSet barDataSet = new BarDataSet(dataValue(), "data");
        barDataSet.setColors(getColors());


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

        chart.setDrawMarkers(true);
        chart.setMarker(new pressMarker(this, totalPressTime));
    }

    private ArrayList<BarEntry> dataValue() {

        ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();

        for(int j= -6;j<1;j++) {
            Log.v("qqq", "--------"+String.valueOf(getOldDate(j)));
            Log.v("test", String.valueOf(j));
            everypress(j);
            for(int p = 0; p < 10; p++){
                Log.v("123",p+ String.valueOf(funcpress2[p]));
            }
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

                Log.v("ha", String.valueOf(a));
                dataVals.add(new BarEntry(j+7, new float[]{a,b,c,d,e,f,g,h,i,k}));

                Log.v("qqq", String.valueOf(fmyp[p]));
                Log.v("qqq", String.valueOf(funcpress2[p]));
                pressNumber temp = new pressNumber(fmyp[p], funcpress2[p]);
                totalPressTime.add(temp);
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
        return dft.format(endDate);
    }

    public void everypress(int j){
            for (int i = 0; i <record.size();i++){
                if (getOldDate(j).compareTo(record.get(i).date) == 0) {

                    Log.v("fuck2", "查證用"+getOldDate(j));
                    Log.v("fuck2","查證用"+record.get(i).date);
                    Log.v("fuck2","查證用"+record.get(i).times);
                    Log.v("fuck2","查證用"+record.get(i).func);

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
        Log.v("fuck2","查證用------");
        for (int j = 0; j < fmyp.length;j++){
            if ((fmyp[j]).equals(func)) {
                funcpress2[j] = time2 +funcpress2[j];
//                Log.v("fuck2", "查證用"+getOldDate(j));
                Log.v("fuck2","查證用"+record.get(j).date);
                Log.v("fuck2","查證用"+record.get(j).times);
                Log.v("fuck2","查證用---"+record.get(j).func);
                Log.v("fuck2","查證用000000"+fmyp[j]);
            }
        }
//        for(int p = 0; p < 10; p++){
//            Log.v("woo",p+ String.valueOf(funcpress2[p]));
//        }
        return;
    }

    public void statistics(float[] tmp_funpress){
        Log.v("yyy","---------");
        for (int j = 0; j<fmyp.length; j++){
            float tmp;
//            float[] staticdata = new float[]{0,0,0,0,0,0,0,0,0,0};
//            String[] disease = new String[]{};

            tmp = tmp_funpress[j];
            staticdata[j] = staticdata[j]+tmp;

            //判斷按壓次數
            if (staticdata[j] > 3){
                if (disease.contains(fmyp[j])){
                    //為了不加入重複的病徵
                } else {
                    disease.add(fmyp[j]);
                }
            }
        }
        Log.v("oomg", String.valueOf(disease));
        Fragment doctorFragment = new Fragment();
        Bundle bundle = new Bundle();
//        bundle.putStringArray("Disease", disease);//這裡的values就是我們要傳的值
        doctorFragment.setArguments(bundle);
        return;
    }

    private int[] getColors() {
        int[] colors = new int[10];
        colors[0] = getResources().getColor(R.color.colorAccent);
        colors[1] = getResources().getColor(R.color.design_default_color_primary);
        colors[2] = getResources().getColor(R.color.colorPrimaryDark);
        colors[3] = getResources().getColor(R.color.design_default_color_primary);
        colors[4] = getResources().getColor(R.color.common_google_signin_btn_text_light);
        colors[5] = getResources().getColor(R.color.common_google_signin_btn_text_light_disabled);
        colors[6] = getResources().getColor(R.color.common_google_signin_btn_text_light_pressed);
        colors[7] = getResources().getColor(R.color.common_google_signin_btn_tint);
        colors[8] = getResources().getColor(R.color.common_google_signin_btn_text_light_default);
        colors[9] = getResources().getColor(R.color.colorAccent);
        return colors;
    }
}



class pressMarker extends MarkerView {

    private TextView contentView;
    private List<pressNumber> totalPress;

    public pressMarker(Context context, List<pressNumber> totalPress) {
        // 设置布局文件
        super(context, R.layout.view_marker);
        this.totalPress = totalPress;
        contentView = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        pressNumber item = totalPress.get((int) e.getX());
        contentView.setText(item.getName());
        super.refreshContent(e, highlight);
    }

    // 设置偏移量
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(float) (getWidth() / 2), -getHeight() - 10);
    }
}