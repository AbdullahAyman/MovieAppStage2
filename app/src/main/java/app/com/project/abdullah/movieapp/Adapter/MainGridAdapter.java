package app.com.project.abdullah.movieapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import app.com.project.abdullah.movieapp.Model.MovieDetailData;
import app.com.project.abdullah.movieapp.R;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Abdullah on 08/12/2016.
 */
public class MainGridAdapter extends BaseAdapter{

    Holder holder;
    View view;
    Context context;
    ArrayList<MovieDetailData> movieDetailDatas;
    public MainGridAdapter(Context cont, ArrayList<MovieDetailData> datas){
        this.context = cont;
        this.movieDetailDatas = datas;

    }
    @Override
    public int getCount() {
        return movieDetailDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return movieDetailDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.grid_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        }
        else{
           holder = (Holder)view.getTag();
        }
        String imagePath = holder.imageUrl+movieDetailDatas.get(position).getPoster_path();
        Picasso.with(context)
                .load(imagePath)
                .into(holder.imageView);
        return view;
    }

    class Holder{
        private View view;
        private ImageView imageView;
        private String imageUrl;
        public Holder(View v){
            this.view = v;
            imageView = (ImageView)view.findViewById(R.id.main_pic);
            imageUrl = context.getResources().getString(R.string.image_url);
        }
    }
}
