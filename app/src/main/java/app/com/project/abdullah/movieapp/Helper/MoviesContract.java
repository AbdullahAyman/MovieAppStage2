package app.com.project.abdullah.movieapp.Helper;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abdullah on 04/07/2017.
 */

public final class MoviesContract {

    public static final String AUTHORITY = "app.com.project.abdullah.movieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES= "MovieTable";

    private MoviesContract() {
    }
    public static class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
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
