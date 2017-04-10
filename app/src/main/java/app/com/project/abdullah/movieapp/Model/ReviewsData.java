package app.com.project.abdullah.movieapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Abdullah on 09/15/2016.
 */
public class ReviewsData {
    @SerializedName("id")
    private String id;
    @SerializedName("page")
    private String page;
    @SerializedName("results")
    private ArrayList<ReviewsArrayData> results;
    @SerializedName("total_pages")
    private String total_pages;
    @SerializedName("total_results")
    private String total_results;

    public String getId() {
        return id;
    }

    public ArrayList<ReviewsArrayData> getResults() {
        return results;
    }

    public String getPage() {
        return page;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResults(ArrayList<ReviewsArrayData> results) {
        this.results = results;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }
}
