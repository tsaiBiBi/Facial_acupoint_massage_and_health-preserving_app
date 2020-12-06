package com.example.webservice;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.acupuncture.homeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Weather {

    static TextView tv_weatherInfo;
    static Context ctx;
    public static Double lat, lng;

    public static void get_predict(Context ctx, String cityName, TextView tv_weather) {

//        String countryName = "臺南市";
        String url = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-2F5A7CDF-44C8-4B8C-9EBE-9C69CCA87516&locationName="+cityName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Thread t = Thread.currentThread();
                        t.setName("Admin Thread");
                        t.setPriority(1);
                        Log.e("B8-thread", String.valueOf(t));
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
                            tv_weather.setText(String.valueOf(weatherInfo));
                            tv_weather.setText(acupSuggestion(cityName, weatherInfo));
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
                    Toast.makeText(ctx , city_name , Toast.LENGTH_LONG).show();
                    get_predict(ctx, city_name, Weather.tv_weatherInfo);
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

    public static void get_weather_suggestion(Context ctx, TextView tv_weatherInfo){
        Weather.ctx = ctx;
        Weather.tv_weatherInfo = tv_weatherInfo;
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

    public static String acupSuggestion(String cityName, JSONObject weatherInfo) throws JSONException {
        String suggestion = "";
        JSONObject minT = weatherInfo.getJSONObject("curMinT").getJSONObject("parameter");
        JSONObject maxT = weatherInfo.getJSONObject("curMaxT").getJSONObject("parameter");
        String startTIme = weatherInfo.getJSONObject("curMinT").getString("startTime");
        String endTime = weatherInfo.getJSONObject("curMinT").getString("endTime");
        suggestion = cityName + "氣象資訊";
        suggestion += "\n時間: "+ startTIme + " ~ " + endTime ;
        suggestion += "\n最高溫: "+ maxT.getString("parameterName")+"°"+maxT.getString("parameterUnit");
        suggestion += "\n最低溫: "+ minT.getString("parameterName")+"°"+maxT.getString("parameterUnit");
        // too hot --> 中暑
        if(maxT.getInt("parameterName") >= 25){
            suggestion += "\n\n天氣炎熱注意補充水分以免中暑!\n";
            suggestion += "\n穴道小知識:";
            suggestion += "\n水溝穴被視為昏迷急救的穴道，中醫師在急救中暑昏迷的病人時會按壓水溝穴，使病人感到劇烈疼痛達到喚醒病人的效果";
            Log.e("sug", suggestion);
        }
        // too cold --> 頭痛
        else if(minT.getInt("parameterName") < 20){
            suggestion += "\n\n天氣寒冷注意保暖!\n";
            suggestion += "\n穴道小知識:";
            suggestion += "\n天冷容易造成氣血循環不順，增加頭痛發生的機率。當頭痛找上門時可以按壓「太陽穴」、「印堂穴」與「絲竹空」來舒緩喔~";
        }
        return suggestion;
    }
}
