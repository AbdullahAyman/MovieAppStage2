package app.com.project.abdullah.movieapp.Activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.project.abdullah.movieapp.Fragments.DetailsFragment;
import app.com.project.abdullah.movieapp.Fragments.GridFragment;
import app.com.project.abdullah.movieapp.Interface.CallBackInterface;
import app.com.project.abdullah.movieapp.Model.MovieDetailData;
import app.com.project.abdullah.movieapp.R;

public class MainActivity extends AppCompatActivity implements CallBackInterface{
    ImageView back;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (ImageView) findViewById(R.id.back_btn);
        title = (TextView) findViewById(R.id.title_action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{"Manifest.permission.CAMERA"}, 0);
        } else {
            LoadMainFragment();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMainFragment();
            }
        });
       /* MoviesParser moviesParser = new MoviesParser(getApplicationContext(),this);
        moviesParser.getData("https://api.themoviedb.org/3/movie/popular?api_key=4fe6c00f887948198f3c8db2065ffc60");
      */  /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // do nothing, just override
    }

    private void LoadMainFragment() {
        back.setVisibility(View.GONE);
        title.setText("Movie App");
        Fragment gridFragment = new GridFragment(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, gridFragment).commit();
    }

    private void LoadDetailsFragment(MovieDetailData movieDetailData) {
        Fragment gridFragment = new DetailsFragment(movieDetailData);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if (isTab) {
            ft.replace(R.id.details_fragment, gridFragment).commit();
        } else {
            back.setVisibility(View.VISIBLE);
            title.setText("Details");
            ft.replace(R.id.main_fragment, gridFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent in = new Intent(this, SettingActivity.class);
                startActivity(in);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void OnItemGridPressed(MovieDetailData movieDetailData) {
        LoadDetailsFragment(movieDetailData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadMainFragment();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
