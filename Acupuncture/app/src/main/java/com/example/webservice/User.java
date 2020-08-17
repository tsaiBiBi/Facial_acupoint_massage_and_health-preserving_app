package com.example.webservice;

import android.content.Context;
import android.support.constraint.solver.LinearSystem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acupuncture.LoginActivity;
import com.example.acupuncture.RegisterActivity;
import com.example.dataclass.Urls;

import java.util.HashMap;
import java.util.Map;

public class User {

    private static RequestQueue requestQueue;

    // 註冊
    public static void register(final Context cxt_register , final String fname , final String faccount, final String fpassword, final String fheight, final String fweight, final String fbirth , final Integer fgender) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.register , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(cxt_register , response , Toast.LENGTH_LONG).show();
                        RegisterActivity.et_name.setText("");
                        RegisterActivity.et_account.setText("");
                        RegisterActivity.et_password.setText("");
                        RegisterActivity.et_height.setText("");
                        RegisterActivity.et_weight.setText("");
                        RegisterActivity.et_birth.setText("");
                        RegisterActivity.rgrp_gender.clearCheck();

                    }
                } ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_register , error.toString() , Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String , String> getParams()  {
                Map<String , String> params = new HashMap<String , String>();
                params.put("usr_name"    , fname);
                params.put("usr_account" , faccount);
                params.put("usr_pwd"     , fpassword);
                params.put("usr_height"  , fheight);
                params.put("usr_weight"  , fweight);
                params.put("usr_birth"   , fbirth);
                params.put("usr_gender"  , String.valueOf(fgender));
                params.put("role"        , "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(cxt_register.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // 登入
    public static void login(final Context cxt_login , final String faccount , final String fpassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.login , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        requestQueue.getCache().clear();
                        if(response.equals("1")) {
                            Toast.makeText(cxt_login , "登入成功" , Toast.LENGTH_LONG).show();
                        }
                        else if (response.equals("0")) {
                            Toast.makeText(cxt_login , "帳號不存在，請確認後再試" , Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(cxt_login , "帳號或密碼輸入錯誤" , Toast.LENGTH_LONG).show();
                        }
                        LoginActivity.et_account.setText("");
                        LoginActivity.et_password.setText("");
                    }} ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_login , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String , String> getParams()  {
                Map<String , String> params = new HashMap<String , String>();
                params.put("usr_account" , faccount);
                params.put("usr_pwd"     , fpassword);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_login.getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
