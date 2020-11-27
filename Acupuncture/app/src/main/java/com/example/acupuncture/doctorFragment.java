package com.example.acupuncture;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

public class doctorFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    MapView mapView;

    private String[] disease = new String[]{"眼睛保健", "改善失眠"};
    private String[][] division = new String[][]{{"眼科"},{"家醫科","神經科","精神科"}};
    private Spinner sp1, sp2;
    private ArrayAdapter<String> adapter ;
    private ArrayAdapter<String> adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor, container, false);

        // 載入第一個下拉選單
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, disease);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1 = (Spinner) v.findViewById(R.id.disease);
        sp2 = (Spinner) v.findViewById(R.id.division);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(selectListener);

        return v;
    }
    // 第一個下拉類別的監聽
    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
            // 讀取第一個下拉選單是選擇第幾個
            int pos = sp1.getSelectedItemPosition();
            // 載入第二個下拉選單
            adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, division[pos]);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp2.setAdapter(adapter2);
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };

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