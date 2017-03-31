package io.push.movieapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.Entity.Review;
import io.push.movieapp.R;

/**
 * Created by nestorkokoafantchao on 3/9/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>  {
    private List<Review>reviews = new ArrayList<Review>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rootView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.authorTextView.setText(reviews.get(position).getAuthor());
        holder.contentTextView.setText(reviews.get(position).getContent().trim());


    }

    @Override
    public int getItemCount() {
        return  reviews.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder  implements View.OnClickListener{
        @BindView(R.id.tv_review_author) TextView  authorTextView;
        @BindView(R.id.tv_review_content)TextView contentTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
