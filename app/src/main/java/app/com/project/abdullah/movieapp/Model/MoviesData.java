package app.com.project.abdullah.movieapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Abdullah on 08/12/2016.
 */
public class MoviesData implements Parcelable {
    @SerializedName("page")
    private String page;
    @SerializedName("results")
    private ArrayList<MovieDetailData> results;
    @SerializedName("total_results")
    private String total_results;
    @SerializedName("total_pages")
    private String total_pages;


    public MoviesData() {
    }

    protected MoviesData(Parcel in) {
        page = in.readString();
        results = in.createTypedArrayList(MovieDetailData.CREATOR);
        total_results = in.readString();
        total_pages = in.readString();
    }

    public static final Creator<MoviesData> CREATOR = new Creator<MoviesData>() {
        @Override
        public MoviesData createFromParcel(Parcel in) {
            return new MoviesData(in);
        }

        @Override
        public MoviesData[] newArray(int size) {
            return new MoviesData[size];
        }
    };

    public ArrayList<MovieDetailData> getResults() {
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

    public void setPage(String page) {
        this.page = page;
    }

    public void setResults(ArrayList<MovieDetailData> results) {
        this.results = results;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(page);
        dest.writeTypedList(results);
        dest.writeString(total_results);
        dest.writeString(total_pages);
    }
}
