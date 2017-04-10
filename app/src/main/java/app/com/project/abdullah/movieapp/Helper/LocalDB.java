package app.com.project.abdullah.movieapp.Helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.project.abdullah.movieapp.Model.MovieDetailData;

/**
 * Created by abdallah.ayman on 04/27/2016.
 */
public class LocalDB extends SQLiteOpenHelper{

    // database version to create database
    public static final int database_version=1;
    // default strings help in sql language
    private static final String TEXT_TYPE = " TEXT ";
    private static final String COMMA_SEP = " ,";

    public LocalDB(Context context) {
        super(context, DBDataClass.TableInfo.DatabaseName, null, database_version);
    }

    private static final String _SQL_CREATE_TABLE_ =
            "CREATE TABLE " + DBDataClass.TableInfo.TableName+ " (" +
                    DBDataClass.TableInfo.Id + TEXT_TYPE+" PRIMARY KEY," +
                    DBDataClass.TableInfo.OriginalTitle + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.OriginalLanguage + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.Title + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.PosterPath + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.Popularity + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.VoteCount + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.Video + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.VoteAverage + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.Adult + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.OverView + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.ReleaseDate + TEXT_TYPE + COMMA_SEP +
                    DBDataClass.TableInfo.Favourite + TEXT_TYPE +" );";

    private static final String _SQL_DELETE_ENTRIES_ =
            "DROP TABLE IF EXISTS " + DBDataClass.TableInfo.TableName;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(_SQL_CREATE_TABLE_);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(_SQL_DELETE_ENTRIES_);

        db.execSQL(_SQL_CREATE_TABLE_);
    }
    public Cursor get_Favourite_Data(LocalDB dop)
    {
        Cursor cursor=null;
        String _SELECT_Favourited_Data = "select * from " + DBDataClass.TableInfo.TableName+ " where " + DBDataClass.TableInfo.Favourite + " = "+ "1";
        SQLiteDatabase sql=dop.getReadableDatabase();
        cursor =  sql.rawQuery(_SELECT_Favourited_Data, null);
        return cursor;
    }
/*    public Cursor get_InFavourited_Data(LocalDB dop)
    {
        Cursor cursor=null;
        String _SELECT_InFavourited_Data = "select * from " + DBDataClass.TableInfo.TableName;
        SQLiteDatabase sql=dop.getReadableDatabase();
        cursor =  sql.rawQuery(_SELECT_InFavourited_Data, null);
        return cursor;
    }*/
    public void SetFavourite(LocalDB dop,String userId)
    {
        String _UPDATE_Favourite = "UPDATE "+DBDataClass.TableInfo.TableName +" SET "+DBDataClass.TableInfo.Favourite+" = "+ "1" +" WHERE "+DBDataClass.TableInfo.Id+" = "+ userId;
        SQLiteDatabase sqldb= this.getWritableDatabase();
        sqldb.execSQL(_UPDATE_Favourite);
    }
    public Cursor get_if_exist(LocalDB dop,String id)
    {
        Cursor cursor=null;
        String _SELECT_Data_With_ID = "select * from " + DBDataClass.TableInfo.TableName+ " where " + DBDataClass.TableInfo.Id + " = "+ id;
        SQLiteDatabase sql=dop.getReadableDatabase();
        cursor =  sql.rawQuery(_SELECT_Data_With_ID, null);
        return cursor;
    }

    public void putInFavourite(LocalDB dop,MovieDetailData movieDetailData)
    {

        SQLiteDatabase sqldb= dop.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(DBDataClass.TableInfo.Favourite,"0");
        cv.put(DBDataClass.TableInfo.Adult,movieDetailData.getAdult());
        cv.put(DBDataClass.TableInfo.PosterPath, movieDetailData.getPoster_path());
        cv.put(DBDataClass.TableInfo.Id, movieDetailData.getId());
        cv.put(DBDataClass.TableInfo.OriginalLanguage, movieDetailData.getOriginal_language());
        cv.put(DBDataClass.TableInfo.OriginalTitle, movieDetailData.getOriginal_title());
        cv.put(DBDataClass.TableInfo.Popularity, movieDetailData.getPopularity());
        cv.put(DBDataClass.TableInfo.VoteCount, movieDetailData.getVote_count());
        cv.put(DBDataClass.TableInfo.OverView, movieDetailData.getOverview());
        cv.put(DBDataClass.TableInfo.ReleaseDate, movieDetailData.getRelease_date());
        cv.put(DBDataClass.TableInfo.Title, movieDetailData.getTitle());
        cv.put(DBDataClass.TableInfo.VoteAverage, movieDetailData.getVote_average());
        cv.put(DBDataClass.TableInfo.Video, movieDetailData.getVideo());
        sqldb.insert(DBDataClass.TableInfo.TableName, null, cv);
        sqldb.close();
    }
}
