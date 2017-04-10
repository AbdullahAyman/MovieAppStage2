package app.com.project.abdullah.movieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import app.com.project.abdullah.movieapp.Interface.CallBackInterface;
import app.com.project.abdullah.movieapp.Model.MovieDetailData;
import app.com.project.abdullah.movieapp.R;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by E-Jawdaa on 3/31/2017.
 */

public class MainGridRecyclerAdapter extends RecyclerView.Adapter<MainGridRecyclerAdapter.DataViewHolder> {

    ArrayList<MovieDetailData> movieDetailDatas;
    public static Context context;
    CallBackInterface callBackInterface;
    public MainGridRecyclerAdapter(Context cont, ArrayList<MovieDetailData> movieDetail, CallBackInterface callBackClick) {
        context = cont;
        movieDetailDatas = movieDetail;
        callBackInterface = callBackClick;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.grid_item,
                parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        String imagePath = holder.imageUrl + movieDetailDatas.get(position).getPoster_path();
        Picasso.with(context)
                .load(imagePath)
                .into(holder.imgFilm);
        holder.imgFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetailDatas!=null)
                    callBackInterface.OnItemGridPressed(movieDetailDatas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieDetailDatas != null) {
            return movieDetailDatas.size(); // number of cards in this recyclerView
        } else {
            return 0;
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFilm;
        private String imageUrl;

        public DataViewHolder(View view) {
            super(view);
            /** Init Views with its ids*/
            imgFilm = (ImageView)view.findViewById(R.id.main_pic);
            imageUrl = context.getResources().getString(R.string.image_url);

        }
    }
}
