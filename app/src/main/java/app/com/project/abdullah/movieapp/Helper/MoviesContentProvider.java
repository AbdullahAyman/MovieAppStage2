package app.com.project.abdullah.movieapp.Helper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Abdullah on 04/08/2017.
 */

public class MoviesContentProvider extends ContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    private static final UriMatcher sURI_MATCHER = buildUriMatcher();
    private MoviesDBHelper moviesDBHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        moviesDBHelper = new MoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = moviesDBHelper.getReadableDatabase();
        int uriMatch = sURI_MATCHER.match(uri);
        Cursor cursor;
        switch (uriMatch) {
            case MOVIES:
                cursor = db.query(MoviesContract.MoviesEntry.TableName,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                cursor.close();
                break;
            case MOVIES_WITH_ID:
                // Get the id from the URI
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = MoviesContract.MoviesEntry.Id + "="+id;
                String[] mSelectionArgs = new String[]{id};

                // Construct a query as you would normally, passing in the selection/args
                cursor = db.query(MoviesContract.MoviesEntry.TableName,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                cursor.close();
                break;

            default:
                throw new UnsupportedOperationException("unknown URi: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        int uriMatch = sURI_MATCHER.match(uri);
        Uri returnUri;
        switch (uriMatch) {
            case MOVIES:
                long id = db.insert(MoviesContract.MoviesEntry.TableName, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                } else
                    throw new android.database.SQLException("failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("unknown URi: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int tasksUpdated;

        // match code
        int match = sURI_MATCHER.match(uri);

        switch (match) {
            case MOVIES_WITH_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = moviesDBHelper.getWritableDatabase().update(MoviesContract.MoviesEntry.TableName, values, MoviesContract.MoviesEntry.Id + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return number of tasks updated
        return tasksUpdated;
    }
}
