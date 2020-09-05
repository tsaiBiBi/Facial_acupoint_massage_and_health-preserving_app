package com.example.acupuncture;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.dataclass.SharedPrefManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    SharedPrefManager sharedprefmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 取得 BottomNav__obj
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // nav
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 設定右上角的 menu
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        // 將 drawerLayout 和 toolbar 整合，出現「三」按鈕
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // sidebar
        NavigationView navViewSide = (NavigationView) findViewById(R.id.nav_view_side);

        // get sidebar textview
        View headerView = navViewSide.getHeaderView(0);
        TextView nav_user_name = (TextView) headerView.findViewById(R.id.username);

        // 側滑選單點擊事件
        navViewSide.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // 關閉選單
                mDrawerLayout.closeDrawers();
                // 各選項點擊事件
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_drink:

                        break;
                    case R.id.nav_about:

                        break;
                    case R.id.nav_logout:
                        sharedprefmanager.logout();
                        break;
                    default:
                }
                return true;
            }
        });
        chk_usr_identity(nav_user_name);
    }

    // 辨別使用者為會員或者訪客
    private void chk_usr_identity(TextView nav_user_name) {
        sharedprefmanager = new SharedPrefManager(this);
        if(sharedprefmanager.chk_login()) {
            HashMap<String , String> user = sharedprefmanager.get_user_detail();
            String vname = user.get(SharedPrefManager.NAME);
            nav_user_name.setText(vname);
        }
        else {
            nav_user_name.setText("guest");
        }
    }
}
