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
import com.example.acupuncture.gameplayingFragment;
import com.example.acupuncture.gameFragment;
import com.example.dataclass.Urls;


public class Questions {

    private static RequestQueue requestQueue;
    public static boolean queIsGotten;

    // 穴道連線
    public static void que(final Context cxt_face) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.POST, Urls.questions, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Integer id;
                            String topic, answer, select1, select2, select3, parsing;
                            queIsGotten = true;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                id = jsonObject.getInt("question_id");
                                topic = jsonObject.getString("question_topic");
                                answer = jsonObject.getString("question_answer");
                                select1 = jsonObject.getString("question_select_1");
                                select2 = jsonObject.getString("question_select_2");
                                select3 = jsonObject.getString("question_select_3");
                                parsing = jsonObject.getString("question_parsing");
                                gameFragment.quesMap(i,id, topic, answer, select1, select2, select3, parsing);
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
