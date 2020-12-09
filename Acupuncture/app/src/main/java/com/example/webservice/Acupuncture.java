package com.example.webservice;

import java.util.ArrayList;
import java.util.List;

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
import com.example.acupuncture.faceFragment;
import com.example.dataclass.Acup;
import com.example.dataclass.Urls;
import com.example.dataclass.Acup_pos;


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
                            int pos_id;
                            List<Acup_pos> pos;
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
                                pos_id = jsonObject.getInt("pos_id");
                                pos = get_points(cxt_face, pos_id);
                                faceFragment.acupMap(i, name, part, position, times, func, detail, img, pos);
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

    public static List<Acup_pos> get_points(final Context cxt_face, int pos_id){
        final List<Acup_pos> pos = new ArrayList<Acup_pos>();

        // get points via pos_id
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.POST, Urls.acup_pos+"/"+pos_id, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String pos_type, side;
                            double bias_x, bias_y, ratio;
                            int a_idx, b_idx, c_idx, d_idx, a_con, b_con, c_con, d_con, sym;

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String pos_name = jsonObject.getString("acup_name");
                                pos_type = jsonObject.getString("pos_type");
                                a_con = jsonObject.getInt("a_contour");
                                b_con = jsonObject.getInt("b_contour");
                                c_con = jsonObject.getInt("c_contour");
                                d_con = jsonObject.getInt("d_contour");

                                a_idx = jsonObject.getInt("a_index");
                                b_idx = jsonObject.getInt("b_index");
                                c_idx = jsonObject.getInt("c_index");
                                d_idx = jsonObject.getInt("d_index");

                                ratio = jsonObject.getDouble("ratio");
                                bias_x = jsonObject.getDouble("bias_x");
                                bias_y = jsonObject.getDouble("bias_y");
                                sym = jsonObject.getInt("sym");
                                side = jsonObject.getString("side");

                                pos.add( new Acup_pos(pos_type, a_con, a_idx, b_con, b_idx, c_con, c_idx, d_con, d_idx, ratio, bias_x, bias_y, sym, side) );
                            }
                        }
                        catch (JSONException e) {
                            String TAG = "Webservice/Acupuncture";
                            Log.e(TAG,Log.getStackTraceString(e));
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
        String TAG = "Test";
        Log.e(TAG, "b5"+pos);
        return pos;
    }
}
