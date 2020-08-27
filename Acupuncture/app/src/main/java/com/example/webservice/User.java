package com.example.webservice;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dataclass.SharedPrefManager;
import com.example.dataclass.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User {

    private static RequestQueue requestQueue;
    static SharedPrefManager sharedprefmanager;

    // 註冊
    public static void register(final Context cxt_register, final String fname, final String faccount, final String fpassword, final String fheight, final String fweight, final String fbirth, final Integer fgender) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.register , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(cxt_register , response , Toast.LENGTH_LONG).show();
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
    public static void login(final Context cxt_login, final String faccount, final String fpassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.login , new Response.Listener<String>() {
            @Override
                public void onResponse(String response) {
                    requestQueue.getCache().clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");

                        if(status.equals("1")) {
                            JSONObject object = jsonArray.getJSONObject(0);
                            String id     = object.getString("id");
                            String gender = object.getString("gender");
                            String name   = object.getString("name");

                            sharedprefmanager = new SharedPrefManager(cxt_login);
                            sharedprefmanager.create_shared_pref(id , name , gender);
                            Toast.makeText(cxt_login , "登入成功" , Toast.LENGTH_LONG).show();
                        }
                        else if(status.equals("0")) {
                            Toast.makeText(cxt_login , "帳號不存在，請確認後再試" , Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(cxt_login , "帳號或密碼輸入錯誤" , Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    // 查詢使用者資料
    public static void que(final Context cxt_profile) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.que , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if(status.equals("1")) {
                        JSONObject object = jsonArray.getJSONObject(0);
                        String id = object.getString("id");
                        String gender = object.getString("gender");
                        String name = object.getString("name");
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }} ,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(cxt_profile , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                }
            })
        {
            @Override
            protected Map<String , String> getParams()  {
                Map<String , String> params = new HashMap<String , String>();
                sharedprefmanager = new SharedPrefManager(cxt_profile);
                params.put("usr_id" , sharedprefmanager.get_user_detail().get(SharedPrefManager.ID));
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_profile.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // 查詢使用者資料
    public static void edit(final Context cxt_edit) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.edit , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }} ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_edit , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String , String> getParams()  {
                Map<String , String> params = new HashMap<String , String>();
                sharedprefmanager = new SharedPrefManager(cxt_edit);
                params.put("usr_id" , sharedprefmanager.get_user_detail().get(SharedPrefManager.ID));
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_edit.getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
