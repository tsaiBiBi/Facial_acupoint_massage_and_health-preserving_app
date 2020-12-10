package com.example.acupuncture;

import com.example.dataclass.Clinic;
import com.example.dataclass.MusicService;
import com.example.dataclass.Pressed;
import com.example.dataclass.SharedPrefManager;
import com.example.webservice.User;
import com.example.webservice.WClinic;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


public class doctorFragment extends Fragment implements OnMapReadyCallback {

    public static List<Pressed> recordweek = new ArrayList<>();
    public List<String> disease_tmp = new ArrayList<String>();
    private static String[] fmyp = new String[]{"眼睛保健", "臉部美容", "緩解頭痛", "改善鼻子不適",
            "改善失眠", "緩解牙痛", "緩解耳鳴", "昏迷急救", "改善口腔衛生", "緩解顔面神經麻痺"};
    public int[] disease_tmpcount = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private Spinner sp1, sp2;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private String itemname[] = null;
    private Double latitude , longitude;

    private static final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION = 100;
    public static List<Clinic> clinics = new ArrayList<>();
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LocationManager mLocationMgr;
    private FusedLocationProviderClient client;
    public final String type = "1";
    private SharedPrefManager sharedprefmanager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor, container, false);

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


        //從資料庫去拿所有資料
        if (!User.recordIsGotten) {
            User.pressedCount(getActivity());
        }

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        checkLocationPermissionAndEnableIt(false);
        mLocationMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        
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
                for (int i = 0; i < disease_tmpcount.length; i++) {
                    Log.v("anotherrrrr testtest", String.valueOf(disease_tmpcount[i]));
                }
                sp1.setOnItemSelectedListener(selectListener);
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
            for (int j = 0; j < recordweek.size(); j++) {
                if (fmyp[i].compareTo(recordweek.get(j).func) == 0) {
                    //將按壓相同病徵的日期存入
                    tmp_date.add(recordweek.get(j).date);

                    //為了要判斷沒有同一個日期所以往下跑 function
                    issame(tmp_date, i);
                }
            }
            //清空
            tmp_date.clear();
        }
    }

    public void issame(List<String> tmp_date2, int fymp_index) {
        for (int i = 0; i < disease_tmpcount.length; i++) {
            if (i == fymp_index) {
                disease_tmpcount[fymp_index] = tmp_date2.size();
                Log.v("check", String.valueOf(fmyp[i]));
                Log.v("check", String.valueOf(tmp_date2));
                Log.v("check", String.valueOf(disease_tmpcount[i]));
                Log.v("check", "----------------------");
            }
            //為了要讓他的長度不要無限增大
            //七天超過四次在這裡判斷的
            if (disease_tmpcount[i] > 3) {
                if (!disease_tmp.contains(fmyp[i])) {
                    disease_tmp.add(fmyp[i]);
                }
            }
        }
        return;
    }

    //第一個下拉類別的監聽
    AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            // 讀取第一個下拉選單是選擇第幾個
            if (parent.getId() == R.id.disease) {
                switch (parent.getSelectedItem().toString()) {
                    case "眼睛保健":
                        itemname = new String[] { "眼科" };
                        break;
                    case "臉部美容":
                        itemname = new String[] { "皮膚科" };
                        break;
                    case "緩解頭痛":
                    case "改善失眠":
                        itemname = new String[] { "家醫科", "神經科", "精神科" };
                        break;
                    case "改善鼻子不適":
                        itemname = new String[] { "家醫科", "耳鼻喉科" };
                        break;
                    case "緩解牙痛":
                        itemname = new String[] { "牙科" };
                        break;
                    case "緩解耳鳴":
                        itemname = new String[] { "耳鼻喉科" };
                        break;
                    case "昏迷急救":
                        itemname = new String[] {};
                        break;
                    case "改善口腔衛生":
                        itemname = new String[] { "家醫科", "耳鼻喉科", "牙科" };
                        break;
                    case "緩解顔面神經麻痺":
                        itemname = new String[] { "家醫科", "神經科"};
                        break;
                }
                if (itemname != null) {
                    setnameitem();
                }
            } else if (parent.getId() == R.id.division) {
                //這裡可以抓到所選取的病徵
                mMap.clear();
                myLocation();
                String clinic_type = sp2.getSelectedItem().toString();
                WClinic.q_clinic(getActivity() , mMap , latitude , longitude , clinic_type);
            }
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    //這裡就是把 division 的東西加入下拉是選單裡
    private void setnameitem() {
        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, itemname);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(selectListener);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //檢查收到的權限要求編號是否和我們送出的相同
        if(requestCode == REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //再檢查一次，就會進入同意的狀態，並且啟動定位
                checkLocationPermissionAndEnableIt(true);
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkLocationPermissionAndEnableIt(boolean on) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //這項功能尚未取得使用者的同意
            //開始執行徵詢使用者的流程
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(getActivity());
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("App需要啟動定位功能。");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //顯示詢問使用者是否同意功能權限的對話盒
                        //使用者答覆後會執行onRequestPermissionsResult()
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                    }
                });
                altDlgBuilder.show();

            } else {
                //顯示使用者是否同意功能權限的對話和
                //使用者答覆後會執行onRequestPermissionsResult()
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
            }
            return;
        }
        //這項功能之前已經取得使用者的同意
        //根據on參數的值，啟動或關閉定位功能
        if (on) {
            //啟動定位功能
        } else {
            //關閉定位功能
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    myLocation();
                }
            }
        });
    }

    public void myLocation() {
        LatLng here = new LatLng(latitude , longitude);
        mMap.addMarker(
                new MarkerOptions()
                        .position(here)
                        .title("我在這裡喔")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(here , 13));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
    }

    @Override
    public void onResume() {
        super.onResume();
        User.pressedCount(getActivity());
    }
}

