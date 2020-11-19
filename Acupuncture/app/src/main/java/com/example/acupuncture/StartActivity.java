package com.example.acupuncture;

import android.app.AppOpsManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dataclass.SharedPrefManager;
import com.example.screenTime.ScreenNotifyService;

public class StartActivity extends AppCompatActivity {

    SharedPrefManager sharedprefmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        // class
        sharedprefmanager = new SharedPrefManager(this);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Toolbar 取代 ActionBar

        // 點擊跳至註冊頁面
        Button register = (Button) findViewById(R.id.bt_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 點擊跳至登入頁面
        Button login = (Button) findViewById(R.id.bt_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Class activity;
                
                if (sharedprefmanager.chk_login()) {
                    Toast.makeText(StartActivity.this, "您已登入", Toast.LENGTH_LONG).show();
                    activity = MainActivity.class;
                }
                else {
                    activity = LoginActivity.class;
                }

                intent.setClass(StartActivity.this, activity);
                startActivity(intent);
            }
        });

        // 點擊跳至主頁面
        Button guest = (Button) findViewById(R.id.bt_guest);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            android.content.pm.ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(this.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
