package com.example.acupuncture;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dataclass.MusicService;
import com.example.dataclass.Pressed;
import com.example.dataclass.SharedPrefManager;
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

    private ProgressBar progressBar;

    public static List<Pressed> record = new ArrayList<>();
    private boolean isclicked = true;
    private SharedPrefManager sharedprefmanager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        Intent stopIntent = new Intent(getContext(), MusicService.class);
        getActivity().stopService(stopIntent);


        // 訪客控制------------------------
        sharedprefmanager = new SharedPrefManager(getContext());
        // 還沒登入的話
        if(!sharedprefmanager.chk_login()){
            Fragment fragment = new guestFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.commit();
        }
        //------------------------

        if (!User.recordIsGotten) {
            User.pressedCount(getActivity());
        }

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

        CardView screentimeView = (CardView) v.findViewById(R.id.screentimeView);
        CardView diseaseclickedView = (CardView) v.findViewById(R.id.diseaseclickedView);

        screentimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclicked = false;
                if(isclicked == false){
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    intent.setClass(getContext(), ScreenTimeActivity.class);
                    startActivity(intent);
                    isclicked = true;
                }

                //progressBar.setVisibility(View.GONE);
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

    }
    @Override
    public void onResume() {
//        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
        User.pressedCount(getActivity());
        if (isclicked == true){
            progressBar.setVisibility(View.GONE);
        }
    }

}