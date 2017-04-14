package io.push.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.entity.Video;
import io.push.movieapp.R;

/**
 * Created by nestorkokoafantchao on 3/9/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {


   private List<Video> videos = new ArrayList<Video>();
   private static final String  YOUTUBE_BASE_URL="http://www.youtube.com/watch";

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View rootView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_view,parent, false) ;
       ViewHolder viewHolder =new ViewHolder(rootView);
        return   viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL)
                .buildUpon()
                .appendQueryParameter("v",videos.get(position).getKey())
                .build();
        holder.textViewName.setText(videos.get(position).getName());
        holder.uri=builtUri;

    }

    @Override
    public int getItemCount() {
        return  videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener {
        @BindView(R.id.tv_video_name)TextView textViewName;
        @BindView(R.id.button_play)Button button;
        private  Uri uri;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

             Intent intent   = new Intent(Intent.ACTION_VIEW,uri);
             Context myContext= v.getContext();


            if (intent.resolveActivity(myContext.getPackageManager()) != null) {
                myContext.startActivity(intent);
                Log.d(this.getClass().toString(),"this is the Url "+ uri.toString());
            }
            else {
                Toast.makeText(myContext,myContext.getString(R.string.no_app),Toast.LENGTH_SHORT);
            }

        }
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }
}
