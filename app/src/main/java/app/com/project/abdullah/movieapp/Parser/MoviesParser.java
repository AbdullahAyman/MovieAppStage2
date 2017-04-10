package app.com.project.abdullah.movieapp.Parser;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import app.com.project.abdullah.movieapp.Interface.OnExecuteEnd;

/**
 * Created by Abdullah on 08/12/2016.
 */
public class MoviesParser implements Response.Listener<JSONObject>, Response.ErrorListener {

    Context context;
    OnExecuteEnd onexecuteEnd;

    public MoviesParser(Context Cont, OnExecuteEnd executed) {
        this.context = Cont;
        this.onexecuteEnd = executed;
    }

    public void getData(String URL) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onexecuteEnd.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onexecuteEnd.onError(error);
            }
        });
        requestQueue.add(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onexecuteEnd.onError(error);
    }

    @Override
    public void onResponse(JSONObject response) {
        onexecuteEnd.onSuccess(response);
    }


}
