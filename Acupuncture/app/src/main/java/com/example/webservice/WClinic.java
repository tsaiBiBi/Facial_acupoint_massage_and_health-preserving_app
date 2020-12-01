package com.example.webservice;

// widget
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

// volley & json
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// fragment & dataclass
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class WClinic {

    private static RequestQueue requestQueue;
    public static boolean acupIsGotten;

    public static void q_clinic(Context cli_cnt, GoogleMap mMap, final double lat, final double lng, final String type) {
        final String clinic_url = "http://10.32.21.159/project/clinic/get_clinic?lat=" + lat + "&lng=" + lng + "&type=" + type;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, clinic_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        MarkerOptions options = new MarkerOptions();
                        options.position(new LatLng(jsonObject.getDouble("clinic_lat"), jsonObject.getDouble("clinic_lng")));
                        options.title(jsonObject.getString("clinic_name"));
                        options.snippet(jsonObject.getString("clinic_address"));
                        mMap.addMarker((options));
                    }
                    Toast.makeText(cli_cnt, "ok", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cli_cnt, "Some error occurred -> " + error, Toast.LENGTH_LONG).show();
                Log.d("error" , String.valueOf(error));
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(cli_cnt);
        requestQueue.add(jsonArrayRequest);
    }
}
