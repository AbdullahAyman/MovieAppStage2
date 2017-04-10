package app.com.project.abdullah.movieapp.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.project.abdullah.movieapp.Model.ReviewsArrayData;
import app.com.project.abdullah.movieapp.Model.TrailerArrayData;
import app.com.project.abdullah.movieapp.R;

/**
 * Created by Abdullah on 09/18/2016.
 */
public class ReviewsAdapter extends BaseAdapter {

    Context context;
    Holder holder;
    View view;
    ArrayList<ReviewsArrayData> results;
    public ReviewsAdapter(Context c, ArrayList<ReviewsArrayData> datas){
        this.context = c;
        this.results = datas;
    }
    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public ReviewsArrayData getItem(int position) {
        return results.get(position);
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
            view = vi.inflate(R.layout.footer_list_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        }
        else{
            holder = (Holder)view.getTag();
        }
        holder.authorName.setText(results.get(position).getAuthor());
        holder.Content.setText(results.get(position).getContent());

        String url = results.get(position).getUrl();
        SpannableString content = new SpannableString(url);
        content.setSpan(new UnderlineSpan(), 0, url.length(), 0);
        holder.url.setText(content);

        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(results.get(position).getUrl()));
                    context.startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
    class Holder{
        private View view;
        private TextView authorName,Content,url;
        public Holder(View v){
            this.view = v;
            authorName = (TextView)view.findViewById(R.id.author);
            Content = (TextView)view.findViewById(R.id.content);
            url = (TextView)view.findViewById(R.id.url);
        }
    }
}

