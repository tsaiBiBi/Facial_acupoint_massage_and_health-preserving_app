package com.example.acupuncture;

import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.dataclass.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {

    SharedPrefManager sharedprefmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void chk_usr_identity() {
        sharedprefmanager = new SharedPrefManager(this);
        if (sharedprefmanager.chk_login()) {

        }
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
