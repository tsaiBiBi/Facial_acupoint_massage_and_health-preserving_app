package com.example.acupuncture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dataclass.Urls;
import com.example.webservice.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class faceFragment extends Fragment {
    Button btnNose, btnCar, btnSleep, btnCold, btnPretty, btnEye;
    Button btnOpenCam;
    ImageView ivAcup;
    TextView txtvAcupTitle, txtvAcup;
    public static List<Acup> acupunctures = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face, container, false);
        // 從資料庫去拿所有穴道的資料
        User.acup(getActivity());

        btnNose = (Button) v.findViewById(R.id.nose);
        btnCar = (Button) v.findViewById(R.id.car);
        btnSleep = (Button) v.findViewById(R.id.sleep);
        btnCold = (Button) v.findViewById(R.id.cold);
        btnPretty = (Button) v.findViewById(R.id.pretty);
        btnEye = (Button) v.findViewById(R.id.eye);
        btnOpenCam = (Button) v.findViewById(R.id.openCamera);
        txtvAcup = (TextView) v.findViewById(R.id.txtvAcup);
        txtvAcupTitle = (TextView) v.findViewById(R.id.txtvAcupTitle);
        ivAcup = (ImageView) v.findViewById(R.id.imageAcup);

        btnNose.setOnClickListener(btnAcupList);
        btnCar.setOnClickListener(btnAcupList);
        btnSleep.setOnClickListener(btnAcupList);
        btnCold.setOnClickListener(btnAcupList);
        btnPretty.setOnClickListener(btnAcupList);
        btnEye.setOnClickListener(btnAcupList);

        btnOpenCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(); //呼叫照相機
                intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
                startActivity(intent);
            }
        });
        return v;
    }
    public static void acupMap(int num, String name, String part, String position, String times, String func, String detail, String img){
        // 將資料以穴道為單位分類
        Acup nowAcup = new Acup(num, name, part, position, times, func, detail, img);
        acupunctures.add(nowAcup);
    }

    private View.OnClickListener btnAcupList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 建立一個 AlertDialog.Builder 物件
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(getContext());
            // 取得使用者所按按鈕病徵
            String fSymp = "";
            //判斷id
            switch (view.getId()) {
                //執行方法
                case R.id.nose:
                    fSymp = "鼻子不適";
                    break;
                case R.id.car:
                    fSymp = "暈車";
                    break;
                case R.id.sleep:
                    fSymp = "失眠";
                    break;
                case R.id.cold:
                    fSymp = "奇效";
                    break;
                case R.id.pretty:
                    fSymp = "臉部美容";
                    break;
                case R.id.eye:
                    fSymp = "眼睛保健";
                    break;
            }
            String[] matchAcups = new String[acupunctures.size()]; // 符合病徵的穴道
            int[] matchNumAcups = new int[acupunctures.size()]; // 記錄這個穴道同時符合病徵的特定編號
            int j = 0;  // 第幾個符合病徵的穴道
            // 將所有acupuncture中的func符合fSymp的穴道名稱加入陣列
            for(int i = 0; i < acupunctures.size(); i++){
                if (acupunctures.get(i).func.equals(fSymp)){
                    matchAcups[j] = acupunctures.get(i).name;
                    matchNumAcups[j] = acupunctures.get(i).num;
                    j++;
                }
            }
            // 必須先設定好選單長度 顯示List時才不會有多餘的選項
            int leng = 0;
            for(int i = 0; i < matchAcups.length; i++){
                if(matchAcups[i] == null){
                    leng = i;
                    break;
                }
            }
            final String[] acupList = new String[leng];
            final int[] acupNumList = new int[leng];
            for(int i = 0; i < leng; i++){
                acupList[i] = matchAcups[i];
                acupNumList[i] = matchNumAcups[i];
            }
            dialog_list.setTitle(fSymp); // 設定 Dialog 的標題為病徵名稱
            dialog_list.setItems(acupList, new DialogInterface.OnClickListener() {  // 將穴道放入 dialog 中
                @Override
                //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                public void onClick(DialogInterface dialog, int which) {
                    for(int i = 0; i < acupunctures.size(); i++){
                        if(acupunctures.get(i).name.equals(acupList[which]) && acupunctures.get(i).num == acupNumList[which]){
                            String detail = acupunctures.get(i).detail;
                            txtvAcupTitle.setText(acupList[which]);
                            txtvAcup.setText(detail);
                            String urlImage = Urls.img_url + acupunctures.get(i).img;
                            Picasso.with(getActivity()).load(urlImage).into(ivAcup);
                            break;
                        }
                    }
                }
            });
            // 最後顯示陣列
            dialog_list.show();
        }
    };
}