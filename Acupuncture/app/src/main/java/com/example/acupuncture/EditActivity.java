package com.example.acupuncture;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.webservice.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    public static EditText et_name , et_height , et_weight , et_birth;
    public static RadioButton radio_man , radio_woman;
    public static Integer int_gender;
    public static final int EDIT_PHOTO = 2;
    public Button btn_edit;
    private ImageView photo;
    private Bitmap bitmap;
    public String img_url;
    public CircleImageView img;
    public static Integer int_year, int_month, int_day;

    Button btn_date;
    RadioGroup rgrp_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // image
        img = (CircleImageView) findViewById(R.id.iV_photo_edit);

        // edittext
        et_name = (EditText) findViewById(R.id.eT_name_edit);
        et_height = (EditText) findViewById(R.id.eT_height_edit);
        et_weight = (EditText) findViewById(R.id.eT_weight_edit);
        et_birth = (EditText) findViewById(R.id.eT_birth_edit);

        // button
        btn_edit = (Button) findViewById(R.id.btn_edit_ok);

        // radio
        rgrp_gender = (RadioGroup) findViewById(R.id.rBt_gender_edit);
        radio_man = (RadioButton) findViewById(R.id.rBt_man_edit);
        radio_woman = (RadioButton) findViewById(R.id.rBt_woman_edit);

        // set intent value
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        et_name.setText(extras.getString("usr_name"));
        et_height.setText(extras.getString("usr_height"));
        et_weight.setText(extras.getString("usr_weight"));
        et_birth.setText(extras.getString("usr_birth"));
        img_url = extras.getString("img_url");
        int_gender = extras.getInt("usr_gender");

        Func.set_user_image(EditActivity.this , img_url , img);

        // gender
        if(extras.getInt("usr_gender") == 1) {
            radio_man.setChecked(true);
        }
        else {
            radio_woman.setChecked(true);
        }

        // radio
        rgrp_gender = (RadioGroup) findViewById(R.id.rBt_gender_edit);

        // 判斷性別
        rgrp_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group , int selected) {
                int_gender = selected == R.id.rBt_man_edit ? 1 : 0;
            }
        });
        et_birth = (EditText) findViewById(R.id.eT_birth_edit);
        btn_date = (Button)findViewById(R.id.dBt_birth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 設置返回鍵
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        photo = (ImageView) findViewById(R.id.iV_photo_edit);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_photo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 外部存取權權限處理
                if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
            }
        });

        // 判斷空值及登入
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname   = et_name.getText().toString();
                String fheight = et_height.getText().toString();
                String fweight = et_weight.getText().toString();
                String fbirth = et_birth.getText().toString();
                Integer fgender = int_gender;
                if (!chk_null(fname , fheight , fweight , fgender)) {
                    User.edit(EditActivity.this, fname , fheight , fweight , fgender , fbirth);
                }
            }
        });

        // 點擊跳至修改密碼頁面
        EditText editpwd = (EditText) findViewById(R.id.eT_password_edit);
        editpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EditActivity.this, EditpwdActivity.class);
                startActivity(intent);
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
                DatePickerDialog dialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
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
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, EDIT_PHOTO);  // 打開相簿
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            upload_img(get_string_image(bitmap));
        }
    }

    // 上傳圖片
    private void upload_img(final String img) {
        User.upload_img(EditActivity.this , img);
    }

    public String get_string_image(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] image_byte_array = byteArrayOutputStream.toByteArray();
        String encode_img = Base64.encodeToString(image_byte_array, Base64.DEFAULT);
        Log.i("uri", encode_img);
        return encode_img;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    // 顯示日期
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
    }

    // 確認是否有欄位為空值
    private boolean chk_null(String fname , String fheight , String fweight , Integer fgender) {
        Boolean is_null = false;
        if(fname.isEmpty() || fheight.isEmpty() || fweight.isEmpty() || fgender == null) {
            Toast.makeText(EditActivity.this ,"請填完所有欄位" , Toast.LENGTH_LONG).show();
            is_null = true;
        }
        return is_null;
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
}