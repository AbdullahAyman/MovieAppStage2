package app.com.project.abdullah.movieapp.Interface;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by Abdullah on 08/12/2016.
 */
public interface OnExecuteEnd {
    void onSuccess(Object obj);
    void onError(VolleyError obj);
}