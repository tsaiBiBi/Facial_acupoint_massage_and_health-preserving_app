package com.example.acupuncture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dataclass.Acup;
import com.example.dataclass.Acup_pos;
import com.example.dataclass.Urls;
import com.example.webservice.Acupuncture;
import com.example.webservice.User;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class faceFragment extends Fragment {
    private Button btnNose, btnHead, btnSleep, btnTeeth, btnPretty, btnEye, btnEar, btnStun, btnMouth, btnFace;
    private Button btnOpenCam;
    private Button btnNext, btnLast;
    private Button btnNowSymp, btnLastSymp;
    private boolean btnLastIsSetted;
    private ImageView ivAcup;
    private TextView txtvAcupTitle, txtvAcup;
    private Urls urls;

    private static int showingAcup = -1;
    private boolean lastCanBePressed, nextCanBePressed;

    public static List<Acup> acupunctures = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face, container, false);
        // 從資料庫去拿所有穴道的資料
        if (!Acupuncture.acupIsGotten) {
            Acupuncture.acup(getActivity());
        }
        showingAcup = -1;

        btnNose = v.findViewById(R.id.nose);
        btnHead = v.findViewById(R.id.head);
        btnSleep = v.findViewById(R.id.sleep);
        btnTeeth = v.findViewById(R.id.teeth);
        btnPretty = v.findViewById(R.id.pretty);
        btnEye = v.findViewById(R.id.eye);
        btnEar = v.findViewById(R.id.ear);
        btnStun = v.findViewById(R.id.stun);
        btnMouth = v.findViewById(R.id.mouth);
        btnFace = v.findViewById(R.id.face);

        btnOpenCam = v.findViewById(R.id.openCamera);

        txtvAcup = v.findViewById(R.id.txtvAcup);
        txtvAcupTitle = v.findViewById(R.id.txtvAcupTitle);

        ivAcup = v.findViewById(R.id.imageAcup);

        btnNext = v.findViewById(R.id.next);
        btnLast = v.findViewById(R.id.last);

        btnNext.setOnClickListener(btnNextAcup);
        btnLast.setOnClickListener(btnLastAcup);

        btnNose.setOnClickListener(btnAcupList);
        btnHead.setOnClickListener(btnAcupList);
        btnSleep.setOnClickListener(btnAcupList);
        btnTeeth.setOnClickListener(btnAcupList);
        btnPretty.setOnClickListener(btnAcupList);
        btnEye.setOnClickListener(btnAcupList);
        btnEar.setOnClickListener(btnAcupList);
        btnStun.setOnClickListener(btnAcupList);
        btnMouth.setOnClickListener(btnAcupList);
        btnFace.setOnClickListener(btnAcupList);

        return v;
    }

    public static void acupMap(int num, String name, String part, String position, String times, String func, String detail, String img, List<Acup_pos> pos) {
        // 將資料以穴道為單位分類
        Acup nowAcup = new Acup(num, name, part, position, times, func, detail, img, pos);
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
                    fSymp = "改善鼻子不適";
                    btnNowSymp = btnNose;
                    break;
                case R.id.head:
                    fSymp = "緩解頭痛";
                    btnNowSymp = btnHead;
                    break;
                case R.id.sleep:
                    fSymp = "改善失眠";
                    btnNowSymp = btnSleep;
                    break;
                case R.id.teeth:
                    fSymp = "緩解牙痛";
                    btnNowSymp = btnTeeth;
                    break;
                case R.id.pretty:
                    fSymp = "臉部美容";
                    btnNowSymp = btnPretty;
                    break;
                case R.id.eye:
                    fSymp = "眼睛保健";
                    btnNowSymp = btnEye;
                    break;
                case R.id.ear:
                    fSymp = "緩解耳鳴";
                    btnNowSymp = btnEar;
                    break;
                case R.id.stun:
                    fSymp = "昏迷急救";
                    btnNowSymp = btnStun;
                    break;
                case R.id.mouth:
                    fSymp = "改善口腔衛生";
                    btnNowSymp = btnMouth;
                    break;
                case R.id.face:
                    fSymp = "緩解顏面神經麻痺";
                    btnNowSymp = btnFace;
                    break;
            }
            String[] matchAcups = new String[acupunctures.size()]; // 符合病徵的穴道
            int[] matchNumAcups = new int[acupunctures.size()]; // 記錄這個穴道同時符合病徵的特定編號
            int j = 0;  // 第幾個符合病徵的穴道
            // 將所有acupuncture中的func符合fSymp的穴道名稱加入陣列
            for (int i = 0; i < acupunctures.size(); i++) {
                if (acupunctures.get(i).func.equals(fSymp)) {
                    matchAcups[j] = acupunctures.get(i).name;
                    matchNumAcups[j] = acupunctures.get(i).num;
                    j++;
                }
            }
            // 必須先設定好選單長度 顯示List時才不會有多餘的選項
            int leng = 0;
            for (int i = 0; i < matchAcups.length; i++) {
                if (matchAcups[i] == null) {
                    leng = i;
                    break;
                }
            }
            final String[] acupList = new String[leng];
            final int[] acupNumList = new int[leng];
            for (int i = 0; i < leng; i++) {
                acupList[i] = matchAcups[i];
                acupNumList[i] = matchNumAcups[i];
            }
            dialog_list.setTitle(fSymp); // 設定 Dialog 的標題為病徵名稱
            dialog_list.setItems(acupList, new DialogInterface.OnClickListener() {  // 將穴道放入 dialog 中
                @Override
                //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < acupunctures.size(); i++) {
                        if (acupunctures.get(i).name.equals(acupList[which]) && acupunctures.get(i).num == acupNumList[which]) {
                            String detail = acupunctures.get(i).detail;
                            List<Acup_pos> pos = acupunctures.get(i).pos;
                            String posInfo = acupunctures.get(i).position;
                            txtvAcupTitle.setText(acupList[which]);

                            txtvAcup.setText(detail);
                            String urlImage = urls.acup_img_url + acupunctures.get(i).img;
                            Picasso.with(getActivity()).load(urlImage).into(ivAcup);

                            // 設置左排按鈕顏色
                            if (!btnLastIsSetted) {
                                btnLastSymp = btnNowSymp;
                                btnLastIsSetted = true;
                            }
                            btnLastSymp.setBackgroundResource(R.drawable.button_shape);
                            btnNowSymp.setBackgroundResource(R.drawable.button_shape_selected);
                            btnLastSymp = btnNowSymp;

                            // 設置穴道兩旁按鈕顏色
                            showingAcup = acupunctures.get(i).num;
                            // 左還有資料就可亮
                            lastBtnControl();
                            // 右還有資料就可亮
                            nextBtnControl();

                            btnOpenCam.setTextColor(Color.WHITE);
                            btnOpenCam.setBackgroundResource(R.drawable.button_shape);

                            btnOpenCam.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showingAcup >= 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("是否要記錄此次按壓，並作為就醫推薦的參考");
                                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                int acupID = acupunctures.get(showingAcup).num + 1;
                                                Log.v("testtest", "" +acupID);
                                                User.pressedRec(getActivity(), acupID);
                                                Intent intent = new Intent();
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("pos", (Serializable) pos);
                                                bundle.putString("posInfo", posInfo);
                                                intent.putExtras(bundle);
                                                //呼叫照相機
                                                intent.setClass(getActivity(), CameraXLivePreviewActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent();
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("pos", (Serializable) pos);
                                                bundle.putString("posInfo", posInfo);
                                                intent.putExtras(bundle);
                                                //呼叫照相機
                                                intent.setClass(getActivity(), CameraXLivePreviewActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            });
                            break;
                        }
                    }
                }
            });
            // 最後顯示陣列
            dialog_list.show();
        }
    };

    public void lastBtnControl() {
        // 如果此時TextView中的穴道號碼-1也等於現在的穴道名稱
        // 左還有資料就可亮
        if(showingAcup - 1 >= 0) {
            if (acupunctures.get(showingAcup - 1).num >= 0 && acupunctures.get(showingAcup - 1).name.equals(acupunctures.get(showingAcup).name)) {
                btnLast.setBackgroundResource(R.drawable.lastok);
                lastCanBePressed = true;
            } else {
                btnLast.setBackgroundResource(R.drawable.last);
                lastCanBePressed = false;
            }
        }else if(showingAcup == 0){
            btnLast.setBackgroundResource(R.drawable.last);
            lastCanBePressed = false;
        }
    }

    public void nextBtnControl() {
        // 如果此時TextView中的穴道號碼+1也等於現在的穴道名稱
        // 右還有資料就可亮
        if(showingAcup + 1 > acupunctures.size()) {
            if (acupunctures.get(showingAcup + 1).num < acupunctures.size() && acupunctures.get(showingAcup + 1).name.equals(acupunctures.get(showingAcup).name)) {
                btnNext.setBackgroundResource(R.drawable.nextok);
                nextCanBePressed = true;
            } else {
                btnNext.setBackgroundResource(R.drawable.next);
                nextCanBePressed = false;
            }
        }else if(showingAcup == acupunctures.size() - 1){
            btnNext.setBackgroundResource(R.drawable.next);
            nextCanBePressed = false;
        }
    }

    private View.OnClickListener btnLastAcup = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (showingAcup > 0 && lastCanBePressed) {
                // 更改現狀
                showingAcup = showingAcup - 1;
                txtvAcup.setText(acupunctures.get(showingAcup).detail);

                // 上一個按鈕控制
                lastBtnControl();
                // 既然都可以回到last了那next勢必是亮的
                btnNext.setBackgroundResource(R.drawable.nextok);
                nextCanBePressed = true;

                // 更改左排按鈕
                leftButtonControl(acupunctures.get(showingAcup).func);
            }
        }
    };
    private View.OnClickListener btnNextAcup = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(showingAcup >= 0 && nextCanBePressed) {
                // 更改現狀
                showingAcup = showingAcup + 1;
                txtvAcup.setText(acupunctures.get(showingAcup).detail);

                // 上一個按鈕控制
                nextBtnControl();
                // 既然都可以去到next了那last勢必是亮的
                btnLast.setBackgroundResource(R.drawable.lastok);
                lastCanBePressed = true;

                // 更改左排按鈕
                leftButtonControl(acupunctures.get(showingAcup).func);
            }
        }
    };

    // 更改左排按鈕
    public void leftButtonControl(String function) {
        // 先改變btnNowSymp
        switch (function){
            case "改善鼻子不適":
                btnNowSymp = btnNose;
                break;
            case "緩解頭痛":
                btnNowSymp = btnHead;
                break;
            case "改善失眠":
                btnNowSymp = btnSleep;
                break;
            case "緩解牙痛":
                btnNowSymp = btnTeeth;
                break;
            case "臉部美容":
                btnNowSymp = btnPretty;
                break;
            case "眼睛保健":
                btnNowSymp = btnEye;
                break;
            case "緩解耳鳴":
                btnNowSymp = btnEar;
                break;
            case "昏迷急救":
                btnNowSymp = btnStun;
                break;
            case "改善口腔衛生":
                btnNowSymp = btnMouth;
                break;
            case "緩解顏面神經麻痺":
                btnNowSymp = btnFace;
                break;
        }
        btnLastSymp.setBackgroundResource(R.drawable.button_shape);
        btnNowSymp.setBackgroundResource(R.drawable.button_shape_selected);
        btnLastSymp = btnNowSymp;
    }
}