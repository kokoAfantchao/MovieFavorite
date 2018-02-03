package io.push.movieapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.push.movieapp.R;



public class OverViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_DESCRIPTION = "DESCRIPTION";
    private static final String ARG_PARAM_DATE = "DATE";
    private static final String ARG_PARAM_VOTE="VOTE";
    // TODO: Rename and change types of parameters
    private String  description;
    private String  date;
    private String  title;
    private Double  vote_average;
    @BindView(R.id.tv_description) TextView textView_decription ;
    @BindView(R.id.tv_released_date) TextView textViewReleasedDate;
    @BindView(R.id.movie_ratingBar) RatingBar mratingBar;
    @BindView(R.id.tv_detail_title) TextView textView_title;


    public OverViewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OverViewFragment newInstance(String title ,String description, String date, Double vote_average) {
        OverViewFragment fragment = new OverViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_DESCRIPTION, description);
        args.putString(ARG_PARAM_DATE,date);
        args.putDouble(ARG_PARAM_VOTE,vote_average);
        fragment.setArguments(args);
        fragment.description = description;
        fragment.date=date;
        fragment.title = title;
        fragment.vote_average=vote_average;

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString(ARG_PARAM_DESCRIPTION);
            date= getArguments().getString(ARG_PARAM_DATE);
            vote_average = getArguments().getDouble(ARG_PARAM_VOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView = inflater.inflate(R.layout.fragment_over_view, container, false);
        ButterKnife.bind(this,rootView);
        textView_decription.setText(description);
        textViewReleasedDate.setText(date);
       // textView_title.setText(title);
        mratingBar.setRating(vote_average.floatValue());
        return  rootView;
    }

}
