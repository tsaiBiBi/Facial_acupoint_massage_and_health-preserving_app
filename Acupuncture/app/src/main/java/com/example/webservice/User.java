package com.example.webservice;

// widget & data structure
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// volley & json
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// dataclass & activity
import com.example.acupuncture.DiseaseClickedActivity;
import com.example.acupuncture.ProfileActivity;
import com.example.acupuncture.RegisterActivity;
import com.example.dataclass.SharedPrefManager;
import com.example.acupuncture.chatFragment;
import com.example.acupuncture.doctorFragment;
import com.example.dataclass.Urls;

public class User {

    private static RequestQueue requestQueue;
    static SharedPrefManager sharedprefmanager;
    public static boolean recordIsGotten;

    // 註冊
    public static void register(final Context cxt_register, final String fname, final String faccount, final String fpassword, final String fheight, final String fweight, final String fbirth, final Integer fgender) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.register , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    msg = jsonObject.getString("rtn_msg");
//                    if(msg.equals("註冊成功")){
//                        RegisterActivity.registeredJump();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(cxt_register , msg , Toast.LENGTH_LONG).show();
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
                        String img   = object.getString("img");

                        sharedprefmanager = new SharedPrefManager(cxt_login);
                        sharedprefmanager.create_shared_pref(id , name , gender , img);
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
    public static void que(final Context cxt_profile , final String fid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.que , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null) {
                        new ProfileActivity().et_name.setText(jsonObject.getString("usr_name"));
                        ProfileActivity.et_account.setText(jsonObject.getString("usr_account"));
                        ProfileActivity.et_height.setText(jsonObject.getString("usr_height"));
                        ProfileActivity.et_weight.setText(jsonObject.getString("usr_weight"));
                        ProfileActivity.et_birth.setText(jsonObject.getString("usr_birth"));
                        if(jsonObject.getInt("usr_gender") == 1) {
                            ProfileActivity.radio_man.setChecked(true);
                        }
                        else {
                            ProfileActivity.radio_woman.setChecked(true);
                        }
                    }
                    else {
                        Toast.makeText(cxt_profile , "系統錯誤，請稍後在試" , Toast.LENGTH_LONG).show();
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
                params.put("usr_id" , fid);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_profile.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // 修改使用者資料
    public static void edit(final Context cxt_edit , final String fname, final String fheight , final String fweight , final Integer fgender) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.edit , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String rtn_msg = jsonObject.getString("rtn_msg");

                    if(status.equals("1")) {
                        sharedprefmanager = new SharedPrefManager(cxt_edit);
                        sharedprefmanager.reset_shared_pref(fname , fgender.toString());
                    }

                    Toast.makeText(cxt_edit , rtn_msg , Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }},
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
                params.put("usr_id"     , sharedprefmanager.get_user_detail().get(SharedPrefManager.ID));
                params.put("usr_name"   , fname);
                params.put("usr_height" , fheight);
                params.put("usr_weight" , fweight);
                params.put("usr_gender" , String.valueOf(fgender));
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_edit.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // 修改使用者密碼
    public static void update_pwd(final Context cxt_update_pwd , final String fold_pwd, final String fnew_pwd) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.update_pwd , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
                Toast.makeText(cxt_update_pwd , response , Toast.LENGTH_LONG).show();
            }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_update_pwd , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String , String> getParams()  {
                Map<String , String> params = new HashMap<String , String>();
                sharedprefmanager = new SharedPrefManager(cxt_update_pwd);
                params.put("usr_id" , sharedprefmanager.get_user_detail().get(SharedPrefManager.ID));
                params.put("usr_old_pwd" , fold_pwd);
                params.put("usr_new_pwd" , fnew_pwd);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_update_pwd.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public static void upload_img(final Context cxt_edit , final String img) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.upload_img , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String rtn_msg = jsonObject.getString("rtn_msg");
                    String img = jsonObject.getString("img");

                    if(status.equals("1")) {
                        sharedprefmanager = new SharedPrefManager(cxt_edit);
                        sharedprefmanager.reset_shared_img(img);
                    }

                    Toast.makeText(cxt_edit , rtn_msg , Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }},
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
                params.put("img" , img);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_edit.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // 紀錄使用者按壓穴道
    public static void pressedRec(final Context cxt_face, final int acupID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST , Urls.pressedRec , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestQueue.getCache().clear();
            }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_face , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String , String> getParams()  {
                Map<String , String> params = new HashMap<String , String>();
                sharedprefmanager = new SharedPrefManager(cxt_face);
                params.put("usr_id" , sharedprefmanager.get_user_detail().get(SharedPrefManager.ID));
                params.put("acup_id" , String.valueOf(acupID));
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(cxt_face.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public static void pressedCount(final Context cxt_record) {
        sharedprefmanager = new SharedPrefManager(cxt_record);
        String s = sharedprefmanager.get_user_detail().get(SharedPrefManager.ID);
        String url = Urls.pressedCount+"?usr_id="+s;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // clear
                            chatFragment.record = new ArrayList<>();
                            doctorFragment.recordweek = new ArrayList<>();
                            String date, func;
                            int usr, times;
                            recordIsGotten = true;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                usr = jsonObject.getInt("usr_id");
                                date = jsonObject.getString("day");
                                func = jsonObject.getString("acup_func");
                                times = jsonObject.getInt("times");

                                chatFragment.recordMap(i, usr, date, func, times);
                                doctorFragment.recordWeekMap(i, usr, date, func, times);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_record , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                    }
                })
        {
        };

        requestQueue = Volley.newRequestQueue(cxt_record.getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }


}
