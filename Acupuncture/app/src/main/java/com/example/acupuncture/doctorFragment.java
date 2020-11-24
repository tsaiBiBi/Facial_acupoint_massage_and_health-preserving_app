package com.example.acupuncture;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dataclass.Pressed;
import com.example.webservice.User;

import java.util.ArrayList;
import java.util.List;

public class doctorFragment extends Fragment {

//    public static List<String> disease2 = new ArrayList<>();
    public static List<Pressed> recordweek = new ArrayList<>();
    private String[] disease = new String[]{"眼睛保健", "改善失眠"};
    private String[][] division = new String[][]{{"眼科"},{"家醫科","神經科","精神科"}};
    private Spinner sp1, sp2;
    private ArrayAdapter<String> adapter ;
    private ArrayAdapter<String> adapter2;
//    public List<String> disease2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor, container, false);

         //從資料庫去拿所有資料
        if (!User.recordIsGotten) {
        User.pressedCount(getActivity());
        }

        for (int i = 0 ; i < recordweek.size();i++){
           Log.v("aaabbb", recordweek.get(i).func);
        }


        // 載入第一個下拉選單
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, disease);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1 = (Spinner) v.findViewById(R.id.disease);
        sp2 = (Spinner) v.findViewById(R.id.division);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(selectListener);

        return v;
    }
    public static void recordWeekMap(int num, int usr, String date, String func, int times) {
        Pressed nowFunc = new Pressed(num, usr, date, func, times);
        recordweek.add(nowFunc);
    }

     //第一個下拉類別的監聽
    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
            // 讀取第一個下拉選單是選擇第幾個
            int pos = sp1.getSelectedItemPosition();
            // 載入第二個下拉選單
            adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, division[pos]);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp2.setAdapter(adapter2);

            Log.v("aaa----------", String.valueOf(recordweek.size()));
            for (int i = 0 ; i < recordweek.size();i++){
                Log.v("aaa", recordweek.get(i).func);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };

    @Override
    public void onResume() {
//        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
        User.pressedCount(getActivity());
    }

}