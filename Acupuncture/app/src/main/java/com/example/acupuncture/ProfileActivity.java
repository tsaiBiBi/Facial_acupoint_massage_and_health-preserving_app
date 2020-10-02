package com.example.acupuncture;

import android.content.Intent;
import com.example.dataclass.Urls;
import com.example.webservice.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import de.hdodenhof.circleimageview.CircleImageView;
import com.example.dataclass.SharedPrefManager;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    // widget
    public static EditText et_name , et_account , et_height , et_weight , et_birth;
    public static RadioButton radio_man , radio_woman;
    public static Integer int_gender;
    public CircleImageView user_img;
    public String img_url;

    // class
    RadioGroup rgrp_gender;
    SharedPrefManager sharedprefmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // img
        user_img = (CircleImageView) findViewById(R.id.iV_photo_my);

        // textview
        et_name    = (EditText) findViewById(R.id.eT_name_my);
        et_account = (EditText) findViewById(R.id.eT_account_my);
        et_height  = (EditText) findViewById(R.id.eT_height_my);
        et_weight  = (EditText) findViewById(R.id.eT_weight_my);
        et_birth   = (EditText) findViewById(R.id.dT_birth_my);

        // radio
        rgrp_gender = (RadioGroup) findViewById(R.id.rBt_gender_profile);
        radio_man = (RadioButton) findViewById(R.id.rBt_man_my);
        radio_woman = (RadioButton) findViewById(R.id.rBt_woman_my);

        // set gender
        rgrp_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group , int selected) {
                int_gender = selected == R.id.rBt_man_my ? 1 : 0;
            }
        });

        chk_usr_identity();

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // 設置返回鍵
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 點擊編輯按鈕
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                Bundle extras = new Bundle();
                extras.putString("usr_name"   , String.valueOf(et_name.getText()));
                extras.putString("usr_height" , String.valueOf(et_height.getText()));
                extras.putString("usr_weight" , String.valueOf(et_weight.getText()));
                extras.putString("img_url" , String.valueOf(img_url));
                extras.putInt("usr_gender" , int_gender);
                startActivity(intent.putExtras(extras));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 點擊返回鍵，關閉當前活動
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 確認使用者身分
    private void chk_usr_identity() {
        sharedprefmanager = new SharedPrefManager(this);
        if(sharedprefmanager.chk_login()) {
            HashMap<String , String> user = sharedprefmanager.get_user_detail();
            String fid = user.get(SharedPrefManager.ID);
            img_url = Urls.self_img_url + user.get(SharedPrefManager.IMG);
            Func.set_user_image(ProfileActivity.this , img_url , user_img);
            User.que(ProfileActivity.this , fid);
        }
    }

    // refresh
    protected void onResume() {
        super.onResume();
        chk_usr_identity();
        Func.set_user_image_no_cache(ProfileActivity.this , img_url , user_img);
    }
}
