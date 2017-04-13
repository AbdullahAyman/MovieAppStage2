package app.com.project.abdullah.movieapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.com.project.abdullah.movieapp.Adapter.ListViewAdapter;
import app.com.project.abdullah.movieapp.Adapter.ReviewsAdapter;
import app.com.project.abdullah.movieapp.BuildConfig;
import app.com.project.abdullah.movieapp.Helper.LocalDB;
import app.com.project.abdullah.movieapp.Helper.MoviesContract;
import app.com.project.abdullah.movieapp.Interface.OnExecuteEnd;
import app.com.project.abdullah.movieapp.Model.MovieDetailData;
import app.com.project.abdullah.movieapp.Model.ReviewsArrayData;
import app.com.project.abdullah.movieapp.Model.TrailerArrayData;
import app.com.project.abdullah.movieapp.Parser.MoviesParser;
import app.com.project.abdullah.movieapp.R;
import dmax.dialog.SpotsDialog;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Abdullah on 09/15/2016.
 */
public class DetailsFragment extends Fragment {
    /*
    Plz, Don't forget to set you API Key before test App.
    I am Look forward to your comments.
    Hope you get satisfied.
    */
    View view;
    ImageView image;
    TextView Title, Description, Year, Duration, Rate;
    MovieDetailData movieDetailData;
    ListView listView, footerList;
    RelativeLayout Header;
    LinearLayout footer;
    Button favourite;
    AlertDialog dialog;
    ArrayList<TrailerArrayData> trailerArrayDatas;
    String fav = "0";

    public DetailsFragment() {
    }
    public DetailsFragment(MovieDetailData movieDetailData) {
        this.movieDetailData = movieDetailData;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movieDetailData", movieDetailData);
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            movieDetailData = savedInstanceState.getParcelable("movieDetailData");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_with_header, null);

        Header = (RelativeLayout) getActivity().getLayoutInflater().inflate(
                R.layout.activity_item_details, null);

        footer = (LinearLayout) getActivity().getLayoutInflater().inflate(
                R.layout.footer_list_view, null);

        intialize();
        String imageUrl = this.getResources().getString(R.string.image_url);
        String imagePath = imageUrl + movieDetailData.getPoster_path();
        Picasso.with(getActivity())
                .load(imagePath)
                .into(image);
        String[] date = movieDetailData.getRelease_date().split("-");
        Title.setText(movieDetailData.getTitle());
        Description.setText(movieDetailData.getOverview());
        Rate.setText(movieDetailData.getVote_average() + "/10");
        Year.setText(date[0]);

        loadTrialData(movieDetailData.getId());
        loadReviewData(movieDetailData.getId());
        final String[] id = new String[]{movieDetailData.getId()};
        Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(
                MoviesContract.MoviesEntry.CONTENT_URI, movieDetailData.getId()),
                null, null /*MoviesContract.MoviesEntry.Id + " LIKE ?"*//*+movieDetailData.getId()*/, id, MoviesContract.MoviesEntry.Id);
        int c = cursor.getCount();
        cursor.moveToFirst();
        do {
            if (c != 0)
                fav = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Favourite));
            if (fav.equals("0")) {
                favourite.setText("" + getActivity().getResources().getString(R.string.favourit));
            } else {
                favourite.setText("" + getActivity().getResources().getString(R.string.unfavourit));
            }

        } while (cursor.moveToNext());
        //checkIfFavourite();
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.equals("0")) {
                    fav = "1";
                    Toast.makeText(getActivity(), "This Movie added Successfully", Toast.LENGTH_SHORT).show();
                    setAsFavouriteMovie(movieDetailData, movieDetailData.getId(), "1");
                    favourite.setText("" + getActivity().getResources().getString(R.string.unfavourit));
                } else {
                    fav = "0";
                    favourite.setText("" + getActivity().getResources().getString(R.string.favourit));
                    Toast.makeText(getActivity(), "This Movie removed Successfully", Toast.LENGTH_SHORT).show();
                    setAsFavouriteMovie(movieDetailData, movieDetailData.getId(), "0");

                }


                // LocalDB localDB = new LocalDB(getActivity().getApplicationContext());
                //Cursor cursor = moviesDBHelper.get_if_exist(moviesDBHelper, movieDetailData.getId());
                /*if (cursor.getCount() != 0) {
                    setAsFavouriteMovie(movieDetailData, movieDetailData.getId());
                    //moviesDBHelper.SetFavourite(moviesDBHelper, movieDetailData.getId());
                }*/
                favourite.setBackgroundColor(getResources().getColor(R.color.fav_bg));


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Key = trailerArrayDatas.get(position - 1).getKey();
                /*try {*/
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.youtube) + Key));

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
                /*} catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No application can handle this request.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/
            }
        });
        return view;
    }

    private void setAsFavouriteMovie(MovieDetailData movieDetailData, String movieId, String fav) {
        ContentValues contentValues = new ContentValues();
       /* contentValues.put(MoviesContract.MoviesEntry.Id, movieDetailData.getId());
        contentValues.put(MoviesContract.MoviesEntry.Adult, movieDetailData.getAdult());
        contentValues.put(MoviesContract.MoviesEntry.OriginalLanguage, movieDetailData.getOriginal_language());*/
        contentValues.put(MoviesContract.MoviesEntry.Favourite, fav);
        /*contentValues.put(MoviesContract.MoviesEntry.OriginalTitle, movieDetailData.getOriginal_title());
        contentValues.put(MoviesContract.MoviesEntry.Popularity, movieDetailData.getPopularity());
        contentValues.put(MoviesContract.MoviesEntry.VoteCount, movieDetailData.getVote_count());
        contentValues.put(MoviesContract.MoviesEntry.OverView, movieDetailData.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.PosterPath, movieDetailData.getPoster_path());
        contentValues.put(MoviesContract.MoviesEntry.ReleaseDate, movieDetailData.getRelease_date());
        contentValues.put(MoviesContract.MoviesEntry.Title, movieDetailData.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.VoteAverage, movieDetailData.getVote_average());
        contentValues.put(MoviesContract.MoviesEntry.Video, movieDetailData.getVideo());
        contentValues.put(MoviesContract.MoviesEntry.Title, movieDetailData.getTitle());
*/
        int count = getActivity().getContentResolver().update(Uri.withAppendedPath(
                MoviesContract.MoviesEntry.CONTENT_URI, movieId), contentValues,
                MoviesContract.MoviesEntry.Id + "=?", new String[]{movieId});
        if (count != 0) {
            Log.d("updated id:", movieId);
        }
    }


    private void checkIfFavourite() {
        LocalDB localDB = new LocalDB(getActivity().getApplicationContext());
        Cursor c = localDB.get_if_exist(localDB, movieDetailData.getId());
        String fav, title;
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                fav = c.getString(12);
                title = c.getColumnName(1);
            } while (c.moveToNext());
            if (fav.equals("1"))
                favourite.setBackgroundColor(getResources().getColor(R.color.fav_bg));
            else
                favourite.setBackgroundColor(getResources().getColor(R.color.gray));

        }
    }

    private void loadReviewData(String id) {
        String url = getActivity().getResources().getString(R.string.main_url) + "3/movie/" + id + "/reviews?api_key=" +
                                /*getActivity().getResources().getString(R.string.api_key)*/BuildConfig.API_KEY;
        MoviesParser moviesParser = new MoviesParser(getActivity(), new OnExecuteEnd() {
            @Override
            public void onSuccess(Object obj) {
                Gson gson = new Gson();
                JSONArray jsonArray;
                Type type = new TypeToken<ArrayList<ReviewsArrayData>>() {
                }.getType();
                JSONObject jsonObject = (JSONObject) obj;
                ArrayList<ReviewsArrayData> reviewsArrayDatas = new ArrayList<>();
                try {
                    jsonArray = jsonObject.getJSONArray("results");
                    reviewsArrayDatas = gson.fromJson(jsonArray.toString(), type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), reviewsArrayDatas);
                footerList.setAdapter(reviewsAdapter);
                if (reviewsArrayDatas.size() > 0)
                    listView.addFooterView(footer);
                dialog.dismiss();

            }

            @Override
            public void onError(VolleyError obj) {

            }
        });
        moviesParser.getData(url);
    }

    private void loadTrialData(String id) {
        String url = getActivity().getResources().getString(R.string.main_url) + "3/movie/" + id + "/videos?api_key=" +
                /*getActivity().getResources().getString(R.string.api_key)*/BuildConfig.API_KEY;
        MoviesParser moviesParser = new MoviesParser(getActivity(), new OnExecuteEnd() {
            @Override
            public void onSuccess(Object obj) {
                Gson gson = new Gson();
                JSONArray jsonArray;
                Type type = new TypeToken<ArrayList<TrailerArrayData>>() {
                }.getType();
                JSONObject jsonObject = (JSONObject) obj;
                trailerArrayDatas = new ArrayList<>();
                try {
                    jsonArray = jsonObject.getJSONArray("results");
                    trailerArrayDatas = gson.fromJson(jsonArray.toString(), type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), trailerArrayDatas);
                listView.setAdapter(listViewAdapter);
                listView.addHeaderView(Header);
                /*listView.addFooterView(Header);*/
            }

            @Override
            public void onError(VolleyError obj) {

            }
        });
        moviesParser.getData(url);
    }


    private void intialize() {

        image = (ImageView) Header.findViewById(R.id.detail_image);
        Title = (TextView) Header.findViewById(R.id.title);
        Year = (TextView) Header.findViewById(R.id.year);
        Duration = (TextView) Header.findViewById(R.id.duration);
        Rate = (TextView) Header.findViewById(R.id.rate);
        Description = (TextView) Header.findViewById(R.id.description);
        favourite = (Button) Header.findViewById(R.id.fav);
        listView = (ListView) view.findViewById(R.id.list_details);
        footerList = (ListView) footer.findViewById(R.id.review_list_details);
        dialog = new SpotsDialog(getActivity());
        dialog.show();

    }
}
