package app.com.project.abdullah.movieapp.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.com.project.abdullah.movieapp.Adapter.MainGridRecyclerAdapter;
import app.com.project.abdullah.movieapp.BuildConfig;
import app.com.project.abdullah.movieapp.Helper.LocalDB;
import app.com.project.abdullah.movieapp.Helper.MoviesContract;
import app.com.project.abdullah.movieapp.Helper.MoviesDBHelper;
import app.com.project.abdullah.movieapp.Interface.CallBackInterface;
import app.com.project.abdullah.movieapp.Interface.OnExecuteEnd;
import app.com.project.abdullah.movieapp.Model.MovieDetailData;
import app.com.project.abdullah.movieapp.Model.MoviesData;
import app.com.project.abdullah.movieapp.Parser.MoviesParser;
import app.com.project.abdullah.movieapp.R;

/**
 * Created by Abdullah on 08/12/2016.
 */
public class GridFragment extends Fragment implements OnExecuteEnd, CallBackInterface {

    GridView MainGrid;
    ArrayList<MovieDetailData> movieDetailDatas;
    CallBackInterface callBackInterface;
    LocalDB localDB;
    MoviesData moviesData;
    RecyclerView mainRecycler;
    MoviesDBHelper moviesDBHelper;
    RecyclerView.LayoutManager mLayoutManager;
    Parcelable mListState;

    public GridFragment() {
    }

    public GridFragment(CallBackInterface caller) {
        callBackInterface = caller;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mainRecycler.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("moviesData", moviesData);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("LIST_STATE_KEY", mainRecycler.getLayoutManager().onSaveInstanceState());
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable("LIST_STATE_KEY");
            moviesData = savedInstanceState.getParcelable("moviesData");
            mainRecycler.getLayoutManager().onRestoreInstanceState(mListState);
            if (moviesData != null)
                LoadGrid(moviesData.getResults());
        } else
            getData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_recycler_fragment,
                container, false);
        /*MainGrid = (GridView) view.findViewById(R.id.main_grid);*/
        mainRecycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mainRecycler.setLayoutManager(mLayoutManager);
        mainRecycler.setItemAnimator(new DefaultItemAnimator());
        moviesDBHelper = new MoviesDBHelper(getActivity());

        // localDB = new LocalDB(getActivity().getApplicationContext());
        getData();
       /* MainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callBackInterface.OnItemGridPressed(movieDetailDatas.get(position));
            }
        });*/
        return view;
    }

    private void getData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String category = pref.getString(getString(R.string.pref_unit_key), getString(R.string.pref_units_popular));
        if (!category.equals(getString(R.string.pref_units_favourited))) {
            String url = getActivity().getResources().getString(R.string.main_url) + "3/movie/" + category + "?api_key=" +
                    /*getActivity().getResources().getString(R.string.api_key)*/BuildConfig.API_KEY;
            MoviesParser moviesParser = new MoviesParser(getActivity(), this);
            moviesParser.getData(url);

        } else {
            loadFavouriteData();
        }


    }

    private void loadFavouriteData() {
        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                null, null, null, null);


        // Cursor cursor = moviesDBHelper.getFavouriteMovies(moviesDBHelper);
        if (cursor.getCount() != 0) {
            /*ArrayList<MovieDetailData> movieDetailDatas =
                    new ArrayList<>();*/
            movieDetailDatas = new ArrayList<>();
            MovieDetailData detailData;
            cursor.moveToFirst();
            do {
                String fav = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Favourite));
                if (fav.equals("1")) {
                    detailData = new MovieDetailData();
                    detailData.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Id)));
                    detailData.setOriginal_title(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.OriginalTitle)));
                    //cursor.getColumnIndex(MoviesContract.MoviesEntry.OriginalTitle);
                    detailData.setOriginal_language(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.OriginalLanguage)));
                    detailData.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Title)));
                    detailData.setPoster_path(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.PosterPath)));
                    detailData.setPopularity(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Popularity)));
                    detailData.setVote_count(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.VoteCount)));
                    detailData.setVideo(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Video)));
                    detailData.setVote_average(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.VoteAverage)));
                    detailData.setAdult(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.Adult)));
                    detailData.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.OverView)));
                    detailData.setRelease_date(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.ReleaseDate)));
                    movieDetailDatas.add(detailData);
                }

            } while (cursor.moveToNext());
            LoadGrid(movieDetailDatas);
        }
    }

    /*
      DBDataClass.TableInfo.Id + TEXT_TYPE+" PRIMARY KEY," +
                        DBDataClass.TableInfo.OriginalTitle + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.OriginalLanguage + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.Title + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.BackDropPath + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.Popularity + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.VoteCount + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.Video + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.VoteAverage + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.Adult + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.OverView + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.ReleaseDate + TEXT_TYPE + COMMA_SEP +
                        DBDataClass.TableInfo.Favourite + TEXT_TYPE +" );";

    */
    @Override
    public void onSuccess(Object obj) {
        Gson gson = new Gson();
        JSONArray jsonArray;
        Type listType = new TypeToken<MoviesData>() {
        }.getType();

        JSONObject jsonObject = (JSONObject) obj;
        moviesData = gson.fromJson(jsonObject.toString(), listType);
        Type type = new TypeToken<ArrayList<MovieDetailData>>() {
        }.getType();
        movieDetailDatas = new ArrayList<>();
        try {
            jsonArray = jsonObject.getJSONArray("results");
            movieDetailDatas = gson.fromJson(jsonArray.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*for (int i = 0; i < movieDetailDatas.size(); i++) {
            Cursor c = localDB.get_if_exist(localDB, movieDetailDatas.get(i).getId());
            if (c.getCount() == 0)
                localDB.putInFavourite(localDB, movieDetailDatas.get(i));
        }*/
        addInFavouriteMovies(movieDetailDatas);
        LoadGrid(movieDetailDatas);


    }

    void addInFavouriteMovies(ArrayList<MovieDetailData> movieDetailData) {
        //ContentValues[] values = new ContentValues[]{};

        for (int i = 0; i < movieDetailData.size(); i++) {
            Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                    null, MoviesContract.MoviesEntry.Id + "=?", new String[]{movieDetailData.get(i).getId()}, MoviesContract.MoviesEntry.Id);
            // Cursor cursor = moviesDBHelper.get_if_exist(moviesDBHelper, movieDetailData.get(i).getId());
            if (cursor == null || cursor.getCount() <= 0) {
                //moviesDBHelper.putInFavourite(moviesDBHelper, movieDetailData.get(i));
                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesContract.MoviesEntry.Id, movieDetailData.get(i).getId());
                contentValues.put(MoviesContract.MoviesEntry.Adult, movieDetailData.get(i).getAdult());
                contentValues.put(MoviesContract.MoviesEntry.OriginalLanguage, movieDetailData.get(i).getOriginal_language());
                contentValues.put(MoviesContract.MoviesEntry.Favourite, "0");
                contentValues.put(MoviesContract.MoviesEntry.PosterPath, movieDetailData.get(i).getPoster_path());
                contentValues.put(MoviesContract.MoviesEntry.OriginalTitle, movieDetailData.get(i).getOriginal_title());
                contentValues.put(MoviesContract.MoviesEntry.Popularity, movieDetailData.get(i).getPopularity());
                contentValues.put(MoviesContract.MoviesEntry.VoteCount, movieDetailData.get(i).getVote_count());
                contentValues.put(MoviesContract.MoviesEntry.OverView, movieDetailData.get(i).getOverview());
                contentValues.put(MoviesContract.MoviesEntry.ReleaseDate, movieDetailData.get(i).getRelease_date());
                contentValues.put(MoviesContract.MoviesEntry.Title, movieDetailData.get(i).getTitle());
                contentValues.put(MoviesContract.MoviesEntry.VoteAverage, movieDetailData.get(i).getVote_average());
                contentValues.put(MoviesContract.MoviesEntry.Video, movieDetailData.get(i).getVideo());
                contentValues.put(MoviesContract.MoviesEntry.Title, movieDetailData.get(i).getTitle());

                Uri uri = getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
                if (uri != null) {
                    Log.d("insert uri:", "" + uri.toString());
                }


            }


           /* if (c.getCount() == 0)
                localDB.putInFavourite(localDB, movieDetailDatas.get(i));*/
        }

    }

    private void LoadGrid(ArrayList<MovieDetailData> movieDetailDatas) {

        MainGridRecyclerAdapter mainGridRecyclerAdapter = new MainGridRecyclerAdapter(getActivity(), movieDetailDatas, this);
        mainRecycler.setAdapter(mainGridRecyclerAdapter);

        /*MainGridAdapter mainGridAdapter = new MainGridAdapter(getActivity(), movieDetailDatas);
        MainGrid.setAdapter(mainGridAdapter);*/
        if (getResources().getBoolean(R.bool.isTab)) {
            callBackInterface.OnItemGridPressed(movieDetailDatas.get(0));
        }

    }

    @Override
    public void onError(VolleyError obj) {
    }


    @Override
    public void OnItemGridPressed(MovieDetailData movieDetailData) {
        callBackInterface.OnItemGridPressed(movieDetailData);
    }
}
