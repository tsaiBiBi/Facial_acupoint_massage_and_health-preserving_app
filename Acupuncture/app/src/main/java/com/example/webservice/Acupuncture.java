package com.example.webservice;
// widget
import android.content.Context;
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
import com.example.acupuncture.faceFragment;
import com.example.dataclass.Urls;


public class Acupuncture {

    private static RequestQueue requestQueue;
    public static boolean acupIsGotten;

    // 穴道連線
    public static void acup(final Context cxt_face) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.POST, Urls.acup, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String name, part, position, times, func, detail, img;
                            acupIsGotten = true;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                name = jsonObject.getString("acup_name");
                                part = jsonObject.getString("acup_part");
                                position = jsonObject.getString("acup_position");
                                times = jsonObject.getString("acup_times");
                                func = jsonObject.getString("acup_func");
                                detail = jsonObject.getString("acup_detail");
                                img = jsonObject.getString("acup_image");
                                faceFragment.acupMap(i, name, part, position, times, func, detail, img);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cxt_face , "Some error occurred -> " + error , Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue = Volley.newRequestQueue(cxt_face.getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
