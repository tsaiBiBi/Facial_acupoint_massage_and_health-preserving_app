package com.example.dataclass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.acupuncture.StartActivity;

import java.util.HashMap;

public class SharedPrefManager {

    private SharedPreferences.Editor editor;
    private Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME  = "LOGIN";
    private static final String IS_LOGIN   = "IS_LOGIN";
    public  static final String ID         = "ID";
    public  static final String GENDER     = "GENDER";
    public  static final String NAME       = "NAME";
    public  static final String IMG        = "IMG";

    public SharedPrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME , PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    // 建立
    public void create_shared_pref(String id , String name , String gender , String img) {

        editor.putBoolean(IS_LOGIN , true);
        editor.putString(ID , id);
        editor.putString(GENDER , gender);
        editor.putString(NAME , name);
        editor.putString(IMG , img);
        editor.apply();
    }

    // 重設
    public void reset_shared_pref(String name , String gender) {

        editor.remove(GENDER).putString(GENDER , gender);
        editor.remove(NAME).putString(NAME , name);
        editor.commit();
    }

    // 重設圖片
    public void reset_shared_img(String img) {
        editor.remove(IMG).putString(IMG , img);
        editor.commit();
    }

    public boolean is_login() {
        return sharedPreferences.getBoolean(IS_LOGIN , false);
    }

    public boolean chk_login() {
        if(!this.is_login()) {
            return false;
        }
        else {
            return true;
        }
    }

    // 使用者登出清理
    public void logout() {
        editor.clear().commit();
        context.startActivity(new Intent(context , StartActivity.class));
    }

    public HashMap<String , String> get_user_detail() {
        HashMap<String , String> user = new HashMap<>();
        user.put(ID     , sharedPreferences.getString(ID , null));
        user.put(GENDER , sharedPreferences.getString(GENDER , null));
        user.put(NAME   , sharedPreferences.getString(NAME , null));
        user.put(IMG    , sharedPreferences.getString(IMG , null));
        return user;
    }
}
