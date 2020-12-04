package com.example.acupuncture;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dataclass.SharedPrefManager;
import com.example.webservice.User;

public class LoginActivity extends AppCompatActivity {

    Button btn_back, btn_login;
    public EditText et_account, et_password;
    SharedPrefManager sharedprefmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // class
        sharedprefmanager = new SharedPrefManager(this);

        // edittext
        et_account = (EditText) findViewById(R.id.eT_account_log);
        et_password = (EditText) findViewById(R.id.eT_password_log);

        // button
        btn_login = (Button) findViewById(R.id.btn_log_ok);
        btn_back = (Button) findViewById(R.id.btn_log_back);

        // 判斷空值及登入
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String faccount  = et_account.getText().toString();
                String fpassword = et_password.getText().toString();
                if (!chk_null(faccount, fpassword)) {
                    User.login(LoginActivity.this, faccount, fpassword);
                }

                if(sharedprefmanager.chk_login() == true) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                finish();
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

    // 確認是否有欄位為空值
    private boolean chk_null(String faccount, String fpassword) {
        Boolean is_null = false;
        if (faccount.isEmpty() || fpassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "請填完所有欄位", Toast.LENGTH_LONG).show();
            is_null = true;
        }
        return is_null;
    }
}
