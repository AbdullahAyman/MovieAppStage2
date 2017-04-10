package app.com.project.abdullah.movieapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.project.abdullah.movieapp.Model.MovieDetailData;

/**
 * Created by Abdullah on 04/07/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT ";
    private static final String COMMA_SEP = " ,";

    private static final String SQL_CREATE_TABLE_ =
            "CREATE TABLE " + MoviesContract.MoviesEntry.TableName + " (" +
                    MoviesContract.MoviesEntry.Id + TEXT_TYPE + " PRIMARY KEY," +
                    MoviesContract.MoviesEntry.OriginalTitle + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.OriginalLanguage + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.Title + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.PosterPath + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.Popularity + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.VoteCount + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.Video + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.VoteAverage + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.Adult + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.OverView + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.ReleaseDate + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MoviesEntry.Favourite + TEXT_TYPE + " );";

    private static final String SQL_DELETE_ENTRIES_ =
            "DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TableName;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_);
        onCreate(db);
    }

    public Cursor getFavouriteMovies(MoviesDBHelper dop) {
        Cursor cursor = null;
        String _SELECT_Favourited_Data = "select * from " + MoviesContract.MoviesEntry.TableName + " where " + MoviesContract.MoviesEntry.Favourite + " = " + "1";
        SQLiteDatabase sql = dop.getReadableDatabase();
        cursor = sql.rawQuery(_SELECT_Favourited_Data, null);
        return cursor;
    }
    public void SetFavourite(MoviesDBHelper dop, String userId)
    {
        String _UPDATE_Favourite = "UPDATE "+MoviesContract.MoviesEntry.TableName +" SET "+MoviesContract.MoviesEntry.Favourite+" = "+ "1" +" WHERE "+MoviesContract.MoviesEntry.Id+" = "+ userId;
        SQLiteDatabase sqldb= this.getWritableDatabase();
        sqldb.execSQL(_UPDATE_Favourite);
    }
    public void putInFavourite(MoviesDBHelper dop, MovieDetailData movieDetailData)
    {

        SQLiteDatabase sqldb= dop.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(MoviesContract.MoviesEntry.Favourite,"0");
        cv.put(MoviesContract.MoviesEntry.Adult,movieDetailData.getAdult());
        cv.put(MoviesContract.MoviesEntry.PosterPath, movieDetailData.getPoster_path());
        cv.put(MoviesContract.MoviesEntry.Id, movieDetailData.getId());
        cv.put(MoviesContract.MoviesEntry.OriginalLanguage, movieDetailData.getOriginal_language());
        cv.put(MoviesContract.MoviesEntry.OriginalTitle, movieDetailData.getOriginal_title());
        cv.put(MoviesContract.MoviesEntry.Popularity, movieDetailData.getPopularity());
        cv.put(MoviesContract.MoviesEntry.VoteCount, movieDetailData.getVote_count());
        cv.put(MoviesContract.MoviesEntry.OverView, movieDetailData.getOverview());
        cv.put(MoviesContract.MoviesEntry.ReleaseDate, movieDetailData.getRelease_date());
        cv.put(MoviesContract.MoviesEntry.Title, movieDetailData.getTitle());
        cv.put(MoviesContract.MoviesEntry.VoteAverage, movieDetailData.getVote_average());
        cv.put(MoviesContract.MoviesEntry.Video, movieDetailData.getVideo());
        sqldb.insert(MoviesContract.MoviesEntry.TableName, null, cv);
        sqldb.close();
    }
    public Cursor get_if_exist(MoviesDBHelper dop, String id)
    {
        Cursor cursor=null;
        String _SELECT_Data_With_ID = "select * from " + MoviesContract.MoviesEntry.TableName+ " where " + MoviesContract.MoviesEntry.Id + " = "+ id;
        SQLiteDatabase sql=dop.getReadableDatabase();
        cursor =  sql.rawQuery(_SELECT_Data_With_ID, null);
        return cursor;
    }
}
