package com.example.webservice;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.acupuncture.R;
import com.example.acupuncture.homeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Weather {

    static TextView tv_wPre, tv_wSug, tv_wKnow;
    static Context ctx;
    public static Double lat, lng;

    public static void get_predict(Context ctx, String cityName) {

        String url = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-2F5A7CDF-44C8-4B8C-9EBE-9C69CCA87516&locationName="+cityName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray wEle = response.getJSONObject("records")
                                                .getJSONArray("location")
                                                .getJSONObject(0)
                                                .getJSONArray("weatherElement");

                            Log.e("wEle", String.valueOf(wEle));

                            JSONObject curMinT = wEle.getJSONObject(2).getJSONArray("time").getJSONObject(0);
                            Log.e("curMinT", String.valueOf(curMinT));

                            JSONObject curMaxT = wEle.getJSONObject(4).getJSONArray("time").getJSONObject(0);
                            Log.e("curMaxT", String.valueOf(curMaxT));

                            JSONObject weatherInfo = new JSONObject();
                            weatherInfo.put("curMaxT", curMaxT);
                            weatherInfo.put("curMinT", curMinT);

                            Log.e("wInfo", String.valueOf(weatherInfo));
                            homeFragment.weatherInfo = weatherInfo;
                            JSONObject wInfo = acupSuggestion(cityName, weatherInfo);
                            tv_wPre.setText(wInfo.getString("wPre"));
                            tv_wSug.setText(wInfo.getString("wSug"));
                            tv_wKnow.setText(wInfo.getString("wKnow"));
                        } catch (JSONException e) {
                            Log.e("B8-Err","sth wrong");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        // Access the RequestQueue through your singleton class.
//        Weather.getInstance(this).addToRequestQueue(jsonObjectRequest);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(jsonObjectRequest);
    }

    public static void q_city() {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&language=zh-TW&key=AIzaSyA9BBZG2jwIcPYbxNKRunsRL9XsEUsA-sE";
        Log.d("url" , url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String compound_code = response.getJSONObject("plus_code").getString("compound_code");
                    String city_name = compound_code.substring(10,13);
                    if(city_name.contains("台")) {
                        city_name = city_name.replaceFirst("台", "臺");
                    }
                    get_predict(ctx, city_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(request);
    }

    public static void get_weather_suggestion(Context ctx, TextView tv_wPre, TextView tv_wSug, TextView tv_wKnow){
        Weather.ctx = ctx;
        Weather.tv_wPre = tv_wPre;
        Weather.tv_wSug = tv_wSug;
        Weather.tv_wKnow = tv_wKnow;
        getLocation();
    }

    private static void getLocation() {
        // gps
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    Log.e("B8-Lat" , String.valueOf(location.getLatitude()));
                    Log.e("B8-Long" , String.valueOf(location.getLongitude()));
                    Weather.lat = location.getLatitude();
                    Weather.lng = location.getLongitude();
                    q_city();
                }
            }
        });
    }

    public static JSONObject acupSuggestion(String cityName, JSONObject weatherInfo) throws JSONException {
        JSONObject wInfo = new JSONObject();
        String wPre = "";
        String wSug = "";
        String wKnow = "";
        JSONObject minT = weatherInfo.getJSONObject("curMinT").getJSONObject("parameter");
        JSONObject maxT = weatherInfo.getJSONObject("curMaxT").getJSONObject("parameter");
        String startTIme = weatherInfo.getJSONObject("curMinT").getString("startTime");
        String endTime = weatherInfo.getJSONObject("curMinT").getString("endTime");
        wPre = "⛅ "+ cityName + "氣象資訊";
        wPre += "\n時間: "+ startTIme + " - " + endTime ;
        wPre += "\n最高溫: "+ maxT.getString("parameterName")+"°"+maxT.getString("parameterUnit");
        wPre += "\n最低溫: "+ minT.getString("parameterName")+"°"+maxT.getString("parameterUnit");
        tv_wPre.setBackground(ContextCompat.getDrawable(ctx, R.drawable.msg));
        // too hot --> 中暑
        if(maxT.getInt("parameterName") >= 25){
            wSug += "天氣炎熱注意補充水分以免中暑！";
            tv_wSug.setBackground(ContextCompat.getDrawable(ctx, R.drawable.msg));
            wKnow += "😀 穴道小知識：";
            wKnow += "\n水溝穴被視為昏迷急救的穴道，中醫師在急救中暑昏迷的病人時會按壓水溝穴，使病人感到劇烈疼痛達到喚醒病人的效果。";
            tv_wKnow.setBackground(ContextCompat.getDrawable(ctx, R.drawable.tip));
        }
        // too cold --> 頭痛
        else if(minT.getInt("parameterName") < 25){
            wSug += "天氣寒冷注意保暖！";
            tv_wSug.setBackground(ContextCompat.getDrawable(ctx, R.drawable.msg));
            wKnow += "❤️ 穴道小知識：";
            wKnow += "\n天冷容易造成氣血循環不順，增加頭痛發生的機率。當頭痛找上門時可以按壓「太陽穴」、「印堂穴」與「絲竹空」來舒緩喔～";
            tv_wKnow.setBackground(ContextCompat.getDrawable(ctx, R.drawable.tip));
        }
        wInfo.put("wPre", wPre);
        wInfo.put("wSug", wSug);
        wInfo.put("wKnow", wKnow);
        return wInfo;
    }
}
