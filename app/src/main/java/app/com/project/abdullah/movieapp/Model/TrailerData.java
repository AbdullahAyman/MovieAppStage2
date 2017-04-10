package app.com.project.abdullah.movieapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Abdullah on 09/15/2016.
 */
public class TrailerData {
    @SerializedName("id")
    private String id;
    @SerializedName("results")
    private ArrayList<TrailerArrayData> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<TrailerArrayData> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerArrayData> results) {
        this.results = results;
    }
}