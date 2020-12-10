package com.example.acupuncture;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.dataclass.Urls;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.dataclass.SharedPrefManager;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextView nav_user_name;
    private CircleImageView nav_user_image;
    public String img_url;
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
        nav_user_name = (TextView) headerView.findViewById(R.id.username);
        nav_user_image = (CircleImageView) headerView.findViewById(R.id.icon_image);

//        MenuItem logout = (MenuItem) findViewById(R.id.nav_logout);
//        logout.setTitle("離開");

        // 側滑選單點擊事件
        navViewSide.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // 關閉選單
                mDrawerLayout.closeDrawers();
                // 各選項點擊事件
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        if (sharedprefmanager.get_user_detail().get(SharedPrefManager.ID) == null) {
                            Toast.makeText(MainActivity.this, "訪客無法使用此功能", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_developer:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, DeveloperActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_about:
                        Intent intent2 = new Intent();
                        intent2.setClass(MainActivity.this, AboutActivity.class);
                        startActivity(intent2);
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
        if (sharedprefmanager.chk_login()) {
            getPrefs();
        } else {
            nav_user_name.setText("guest");
        }
    }

    // 設定使用者名稱
    private void getPrefs() {
        HashMap<String, String> user = sharedprefmanager.get_user_detail();
        String name = user.get(SharedPrefManager.NAME) == null ? "遊客" : user.get(SharedPrefManager.NAME);
        String img = user.get(SharedPrefManager.IMG) == null ? "遊客" : user.get(SharedPrefManager.IMG);
        nav_user_name.setText(name);
        img_url = Urls.self_img_url + img;
        Func.set_user_image(MainActivity.this, img_url, nav_user_image);
    }

    public String set_gender_img() {
        String vgender = sharedprefmanager.get_user_detail().get(SharedPrefManager.GENDER);
        vgender = vgender == null ? "0" : vgender;
        return vgender;
    }

    // refresh
    protected void onResume() {
        super.onResume();
        getPrefs();
        set_gender_img();
        Func.set_user_image_no_cache(MainActivity.this, img_url, nav_user_image);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        //點擊通知欄到主Activity時 會執行這個方法
        super.onNewIntent(intent);
        getIntent().putExtra("toFragment", "faceFragment");

        String extras = getIntent().getStringExtra("toFragment");
        ;
        if (extras != null) {
            getNotify(intent);
        }
        setIntent(intent);
    }

    private void getNotify(Intent intent) {
        try {
            if (!intent.getStringExtra("toFragment").isEmpty()) {
                String toFragment = intent.getStringExtra("toFragment");

                // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
                if (toFragment.equals("faceFragment")) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment faceFragment = new faceFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment, faceFragment);
                    fragmentTransaction.commit();
                }
            }
        } catch (NullPointerException e) {
            Log.e("FragmentTest", "NullPointerException");
        }
        //做完操作以後必須將toValue的值初始化
        intent.putExtra("toFragment", "");
        super.onNewIntent(intent);
    }
}
