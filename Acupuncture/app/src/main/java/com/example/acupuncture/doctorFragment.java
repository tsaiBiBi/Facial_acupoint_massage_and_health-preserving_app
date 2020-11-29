package com.example.acupuncture;

import android.bluetooth.le.ScanSettings;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dataclass.Pressed;
import com.example.webservice.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class doctorFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap map;
    MapView mapView;
    public static List<Pressed> recordweek = new ArrayList<>();
    public List<String> disease_tmp = new ArrayList<String>();
    private static String[] fmyp = new String[]{"眼睛保健","臉部美容","緩解頭痛", "改善鼻子不適",
            "改善失眠","緩解牙痛","緩解耳鳴","昏迷急救","改善口腔衛生","緩解顔面神經麻痺"};
    public int[] disease_tmpcount = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private Spinner sp1, sp2;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private String[][] division = new String[][]{{"眼科"}, {"家醫科", "神經科", "精神科"}};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor, container, false);

        //從資料庫去拿所有資料
        if (!User.recordIsGotten) {
            User.pressedCount(getActivity());
        }

        sp1 = (Spinner) v.findViewById(R.id.disease);
        sp2 = (Spinner) v.findViewById(R.id.division);
        sp1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                count_week();
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, disease_tmp);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp1.setAdapter(adapter);

                //測試用
                for(int i = 0;i < disease_tmpcount.length;i++){
                    Log.v("anotherrrrr testtest", String.valueOf(disease_tmpcount[i]));
                }
                return false;
            }
        });
        return v;
    }

    public static void recordWeekMap(int num, int usr, String date, String func, int times) {
        Pressed nowFunc = new Pressed(num, usr, date, func, times);
        recordweek.add(nowFunc);
    }

    public void count_week() {
        //暫存日期用
        List<String> tmp_date = new ArrayList<>();

        //判斷他是哪個病徵
        for (int i = 0; i < fmyp.length; i++) {
            for (int j = 0; j < recordweek.size();j++){
                if (fmyp[i].compareTo(recordweek.get(j).func) == 0){
                    //將按壓相同病徵的日期存入
                    tmp_date.add(recordweek.get(j).date);

                    //為了要判斷沒有同一個日期所以往下跑 function
                    issame(tmp_date,i);
                }
            }
            //清空
            tmp_date.clear();
        }
    }

    public void issame(List<String> tmp_date2,int fymp_index) {
        for (int i =0 ; i<disease_tmpcount.length;i++ ){
            if (i == fymp_index){
                disease_tmpcount[fymp_index] = tmp_date2.size();
            }
            //為了要讓他的長度不要無限增大
            //七天超過四次在這裡判斷的
            if (disease_tmpcount[i] > 1){
                if (disease_tmp.contains(fmyp[i])){
                }else {
                    disease_tmp.add(fmyp[i]);
                }
            }
        }
        return;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;

    }
}


//    private View.OnClickListener qqq  = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            List<String> list = new ArrayList<String>();
//            Log.v("re", String.valueOf(recordweek.size()));
//            for (int i = 0; i < recordweek.size();i++){
//                list.add(recordweek.get(i).func);
//            }
//
//            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp1.setAdapter(adapter);
//        }
//    };
//
//    //第一個下拉類別的監聽
//    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
//        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
//            // 讀取第一個下拉選單是選擇第幾個
//            int pos = sp1.getSelectedItemPosition();
//            // 載入第二個下拉選單
//            adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, division[pos]);
//            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp2.setAdapter(adapter2);
//
//        }
//
//        public void onNothingSelected(AdapterView<?> arg0){
//
//        }
//
//    };

//    @Override
//    public void onResume() {
////        Log.e("DEBUG", "onResume of HomeFragment");
//        super.onResume();
//        User.pressedCount(getActivity());
//    }

