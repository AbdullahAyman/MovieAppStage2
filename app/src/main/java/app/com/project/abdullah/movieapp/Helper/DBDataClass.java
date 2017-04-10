package app.com.project.abdullah.movieapp.Helper;

import android.provider.BaseColumns;
import android.provider.BaseColumns;

/**
 * Created by abdallah.ayman on 04/27/2016.
 */
public class DBDataClass implements BaseColumns{
    public DBDataClass()
    {

    }
    public static abstract class TableInfo implements BaseColumns {
        public static final String DatabaseName = "MovieAppSQlDB";
        public static final String TableName = "MovieTable";
        public static final String Id = "id";
        public static final String OriginalTitle = "original_title";
        public static final String OriginalLanguage = "original_language";
        public static final String Title = "title";
        public static final String PosterPath = "posterpath";
        public static final String Popularity = "popularity";
        public static final String VoteCount = "vote_count";
        public static final String Video = "video";
        public static final String VoteAverage = "vote_average";
        public static final String Adult = "adult";
        public static final String OverView = "overview";
        public static final String ReleaseDate = "release_date";
        public static final String Favourite = "favourite";
    }
}
