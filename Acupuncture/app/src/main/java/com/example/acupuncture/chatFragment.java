package com.example.acupuncture;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dataclass.Pressed;
import com.example.webservice.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class chatFragment extends Fragment {
    public static List<Pressed> record = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        if (!User.recordIsGotten) {
            User.pressedCount(getActivity());
        }

        CardView screentimeView = (CardView) v.findViewById(R.id.screentimeView);
        CardView diseaseclickedView = (CardView) v.findViewById(R.id.diseaseclickedView);

        screentimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ScreenTimeActivity.class);
                startActivity(intent);
            }
        });

        diseaseclickedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //傳值以及跳轉頁面
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Record", (Serializable)record);
                intent.setClass(getContext(), DiseaseClickedActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return v;
    }
    public static void recordMap(int num, int usr, String date, String func, int times) {
        Pressed nowFunc = new Pressed(num, usr, date, func, times);
        record.add(nowFunc);

        for (int i = 0; i <record.size();i++) {
            //如果相等就把他加入 times2 的 list
            Log.v("fuck22222222", String.valueOf(i));
            Log.v("fuck22222222", "查證用" + record.get(i).date);
            Log.v("fuck22222222", "查證用" + record.get(i).times);
            Log.v("fuck22222222", "查證用" + record.get(i).func);
        }

    }
    @Override
    public void onResume() {
//        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
        User.pressedCount(getActivity());
    }

}