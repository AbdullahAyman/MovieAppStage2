package app.com.project.abdullah.movieapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.project.abdullah.movieapp.Model.TrailerArrayData;
import app.com.project.abdullah.movieapp.R;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Abdullah on 09/16/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    Context context;
    Holder holder;
    View view;
    ArrayList<TrailerArrayData> results;
    public ListViewAdapter(Context c, ArrayList<TrailerArrayData> datas){
        this.context = c;
        this.results = datas;
    }
    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public TrailerArrayData getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.list_view_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        }
        else{
            holder = (Holder)view.getTag();
        }
        holder.trailerName.setText(results.get(position).getName());
        return view;
    }
    class Holder{
        private View view;
        private ImageView imageView;
        private TextView trailerName;
        //private LinearLayout linearLayout;
        public Holder(View v){
            this.view = v;
            imageView = (ImageView)view.findViewById(R.id.list_item_img);
            trailerName = (TextView)view.findViewById(R.id.t_name);
            //linearLayout = (LinearLayout)view.findViewById(R.id.list_view_linear);
        }
    }
}
