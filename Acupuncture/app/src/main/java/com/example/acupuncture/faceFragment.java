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
import com.example.dataclass.MusicService;
import com.example.dataclass.SharedPrefManager;
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
    private TextView txtvAcupTitle, txtvAcupEngTitle, txtvAcup;
    private Urls urls;

    private static int showingAcup = -1;
    private boolean lastCanBePressed, nextCanBePressed;

    private String[] acupList2;
    private int[] acupNumList2;
    private int tempWhich;

    public static List<Acup> acupunctures = new ArrayList<>();
    private SharedPrefManager sharedprefmanager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face, container, false);

        Intent stopIntent = new Intent(getContext(), MusicService.class);
        getActivity().stopService(stopIntent);

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
        txtvAcupEngTitle = v.findViewById(R.id.txtvAcupEngTitle);

        ivAcup = v.findViewById(R.id.imageAcup);

        btnNext = v.findViewById(R.id.next);
        btnLast = v.findViewById(R.id.last);

        btnLast.setOnClickListener(btnLastAcup);
        btnNext.setOnClickListener(btnNextAcup);

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

        sharedprefmanager = new SharedPrefManager(getContext());

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
                    fSymp = "緩解顔面神經麻痺";
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
            acupList2 = new String[leng];
            acupNumList2 = new int[leng];
            for (int i = 0; i < leng; i++) {
                acupList[i] = matchAcups[i];
                acupList2[i] = matchAcups[i];
                acupNumList[i] = matchNumAcups[i];
                acupNumList2[i] = matchNumAcups[i];
            }
            dialog_list.setTitle(fSymp); // 設定 Dialog 的標題為病徵名稱
            dialog_list.setItems(acupList, new DialogInterface.OnClickListener() {  // 將穴道放入 dialog 中
                @Override
                //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                public void onClick(DialogInterface dialog, int which) {
                    showingAcup = acupNumList[which];
                    String detail = acupunctures.get(showingAcup).detail;
                    String acup_eng_name = acupunctures.get(showingAcup).img.replaceAll(".png", "");
//                    Log.e("i=which?", i + " = " + acupNumList[which]);
                    txtvAcupTitle.setText(acupList[which]);
                    txtvAcupEngTitle.setText(acup_eng_name);

                    txtvAcup.setText(detail);
                    String urlImage = urls.acup_img_url + acupunctures.get(showingAcup).img;
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
//                    showingAcup = acupunctures.get(nowShowInAcupList).num;

                    tempWhich = which;  // tempWhich

                    // 左還有資料就可亮
                    lastBtnControl();
                    // 右還有資料就可亮
                    nextBtnControl();

                    btnOpenCam.setTextColor(Color.WHITE);
                    btnOpenCam.setBackgroundResource(R.drawable.button_shape);

                    btnOpenCam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("showingAcup", "showingAcup: "+showingAcup);

                            List<Acup_pos> pos = acupunctures.get(showingAcup).pos;
                            String acup_name = acupunctures.get(showingAcup).name;
                            String posInfo = acupunctures.get(showingAcup).position;
                            String acup_times = acupunctures.get(showingAcup).times;
                            
                            if (showingAcup >= 0 && sharedprefmanager.chk_login()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("是否要記錄此次按壓，並作為就醫推薦的參考");
                                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int acupID = acupunctures.get(showingAcup).num + 1;
                                        Log.v("testtest", "" + acupID);
                                        User.pressedRec(getActivity(), acupID);
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("pos", (Serializable) pos);
                                        bundle.putString("acup_name", acup_name);
                                        bundle.putString("posInfo", posInfo);
                                        bundle.putString("acup_times", acup_times);
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
                                        bundle.putString("acup_name", acup_name);
                                        bundle.putString("posInfo", posInfo);
                                        bundle.putString("acup_times", acup_times);
                                        intent.putExtras(bundle);
                                        //呼叫照相機
                                        intent.setClass(getActivity(), CameraXLivePreviewActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.show();
                            }else{
                                //呼叫照相機
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("pos", (Serializable) pos);
                                bundle.putString("acup_name", acup_name);
                                bundle.putString("posInfo", posInfo);
                                bundle.putString("acup_times", acup_times);
                                intent.putExtras(bundle);
                                intent.setClass(getActivity(), CameraXLivePreviewActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
            // 最後顯示陣列
            dialog_list.show();
        }
    };

    public void lastBtnControl() {
        // 如果此時TextView中的穴道號碼-1也等於現在的穴道名稱，那就是還有資料
        if (tempWhich - 1 >= 0 ) {
            // 左還有資料就可亮
            btnLast.setBackgroundResource(R.drawable.lastok);
            lastCanBePressed = true;
        } else {
            btnLast.setBackgroundResource(R.drawable.last);
            lastCanBePressed = false;
        }
    }

    public void nextBtnControl() {
        // 如果此時TextView中的穴道號碼+1也等於現在的穴道名稱
        if (tempWhich + 1 < acupList2.length) {
            // 右還有資料就可亮
            btnNext.setBackgroundResource(R.drawable.nextok);
            nextCanBePressed = true;
        } else {
            btnNext.setBackgroundResource(R.drawable.next);
            nextCanBePressed = false;
        }
    }
    private View.OnClickListener btnLastAcup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (lastCanBePressed) {
                // 更改現狀
                Log.v("showingAcup", "showingAcup: "+showingAcup);
                showingAcup = acupNumList2[tempWhich - 1];
                tempWhich = tempWhich - 1;

                txtvAcupTitle.setText(acupList2[tempWhich]);
                txtvAcupEngTitle.setText(acupunctures.get(acupNumList2[tempWhich]).img.replaceAll(".png", ""));
                txtvAcup.setText(acupunctures.get(acupNumList2[tempWhich]).detail);
                String urlImage = urls.acup_img_url + acupunctures.get(acupNumList2[tempWhich]).img;
                Picasso.with(getActivity()).load(urlImage).into(ivAcup);

                // 上一個按鈕控制
                lastBtnControl();
                // 下一個按鈕控制，既然都可以去到next了那last勢必是亮的
                btnNext.setBackgroundResource(R.drawable.nextok);
                nextCanBePressed = true;
            }
        }
    };
    private View.OnClickListener btnNextAcup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (nextCanBePressed) {
                // 更改現狀
                showingAcup = acupNumList2[tempWhich + 1];
                Log.v("showingAcup", "showingAcup: "+showingAcup);
                tempWhich = tempWhich + 1;
                txtvAcupTitle.setText(acupList2[tempWhich]);
                txtvAcupEngTitle.setText(acupunctures.get(acupNumList2[tempWhich]).img.replaceAll(".png", ""));
                txtvAcup.setText(acupunctures.get(acupNumList2[tempWhich]).detail);
                String urlImage = urls.acup_img_url + acupunctures.get(acupNumList2[tempWhich]).img;
                Picasso.with(getActivity()).load(urlImage).into(ivAcup);

                // 下一個按鈕控制
                nextBtnControl();
                // 上一個按鈕控制，既然都可以去到next了那last勢必是亮的
                btnLast.setBackgroundResource(R.drawable.lastok);
                lastCanBePressed = true;
            }
        }
    };
}