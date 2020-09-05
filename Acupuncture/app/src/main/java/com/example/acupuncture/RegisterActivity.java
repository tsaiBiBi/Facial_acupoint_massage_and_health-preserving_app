package com.example.acupuncture;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.webservice.User;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    public static RadioGroup rgrp_gender;
    public static Integer int_year, int_month, int_day , int_gender;
    public static EditText et_name , et_account , et_password , et_height , et_weight , et_birth;
    Button btn_back , btn_register , btn_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //edittext
        et_name     = (EditText) findViewById(R.id.eT_name_regis);
        et_account  = (EditText) findViewById(R.id.eT_account_regis);
        et_password = (EditText) findViewById(R.id.eT_password_regis);
        et_height   = (EditText) findViewById(R.id.eT_height_regis);
        et_weight   = (EditText) findViewById(R.id.eT_weight_regis);
        et_birth    = (EditText) findViewById(R.id.dT_birth_regis);

        // button
        btn_register = (Button) findViewById(R.id.btn_regis_ok);
        btn_back  = (Button) findViewById(R.id.btn_regis_back);
        btn_date = (Button)findViewById(R.id.dBt_birth);

        // radio
        rgrp_gender = (RadioGroup) findViewById(R.id.rBt_gender_regis);

        // 判斷性別
        rgrp_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group , int selected) {
                int_gender = selected == R.id.male ? 1 : 0;
            }
        });

        // 選擇日期
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int_year = c.get(Calendar.YEAR);
                int_month = c.get(Calendar.MONTH);
                int_day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year, month, day);
                        et_birth.setText(format);
                    }
                }, int_year, int_month, int_day);
                dialog.getDatePicker().setMaxDate((new Date()).getTime());
                dialog.show();
            }
        });

        // 判斷空值及註冊
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname     = et_name.getText().toString().trim();
                String faccount  = et_account.getText().toString().trim();
                String fpassword = et_password.getText().toString().trim();
                String fheight   = et_height.getText().toString().trim();
                String fweight   = et_weight.getText().toString().trim();
                String fbirth    = et_birth.getText().toString().trim();
                Integer fgender  = int_gender;
                if(!chk_null(fname , faccount , fpassword , fheight , fweight , fbirth , fgender)) {
                    User.register(RegisterActivity.this , fname , faccount , fpassword , fheight , fweight , fbirth , fgender);
                }
            }
        });

        // 關閉當前頁面
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 顯示日期
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
    }

    // 判斷是否有空值
    private boolean chk_null(String fname , String faccount, String fpassword, String fheight, String fweight, String fbirth , Integer fgender) {
        Boolean isnull = false;
        if(fname.isEmpty() || faccount.isEmpty() || fpassword.isEmpty() ||
                fheight.isEmpty() || fweight.isEmpty() || fbirth.isEmpty() || fgender == null) {
            Toast.makeText(RegisterActivity.this ,"請填完所有表格" , Toast.LENGTH_LONG).show();
            isnull = true;
        }
        return isnull;
    }
}
