package com.example.acupuncture;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.webservice.Weather;

import org.json.JSONObject;

import com.example.dataclass.MusicService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class homeFragment extends Fragment {

    ConstraintLayout cl;
    ImageView gender_image;
    public static JSONObject weatherInfo;
    private FusedLocationProviderClient fusedLocationClient;

    public homeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Intent stopIntent = new Intent(getContext(), MusicService.class);
        getActivity().stopService(stopIntent);

//        while (weatherInfo == null){
//            Log.e("B8-null", String.valueOf(weatherInfo));
//        }
        gender_image = (ImageView) view.findViewById(R.id.image);
        set_gender_img();
        cl = view.findViewById(R.id.cl);
        TextView tv_wPre = view.findViewById(R.id.tv_wPre);
        TextView tv_wSug = view.findViewById(R.id.tv_wSug);
        TextView tv_wKnow = view.findViewById(R.id.tv_wKnow);
//        TextView tv_weatherInfo = view.findViewById(R.id.tv_weatherInfo);

        /*ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);
        cs.setHorizontalBias(gender_image.getId(), 0.9f);
        cs.applyTo(cl);*/

        // check permission
        while (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            // ask permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        Weather.get_weather_suggestion(getActivity(), tv_wPre, tv_wSug, tv_wKnow);

        return view;
    }

    public void set_gender_img() {
        MainActivity activity = (MainActivity) getActivity();
        String vgender = activity.set_gender_img();
        if(vgender.equals("1")) {
            gender_image.setImageResource(R.drawable.man);
        }
        else {
            gender_image.setImageResource(R.drawable.girl);
        }
    }

    public void onResume() {
        super.onResume();
        set_gender_img();
    }
}
