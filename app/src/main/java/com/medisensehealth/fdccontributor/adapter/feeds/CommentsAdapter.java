package com.medisensehealth.fdccontributor.adapter.feeds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.medisensehealth.fdccontributor.DataModel.Comments;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lenovo on 04/09/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private List<Comments> commentsList;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_image;
        public CustomTextViewSemiBold comment_name, comment_date;
        public CustomTextViewItalic comment_desc;

        public MyViewHolder(View view) {
            super(view);
            profile_image = (ImageView) view.findViewById(R.id.comment_icon);
            comment_name = (CustomTextViewSemiBold) view.findViewById(R.id.comment_list_name);
            comment_desc = (CustomTextViewItalic) view.findViewById(R.id.comments_list_desc);
            comment_date = (CustomTextViewSemiBold) view.findViewById(R.id.comments_list_date);
        }
    }

    public CommentsAdapter(Context context, List<Comments> commList) {
        this.commentsList = commList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comments comment = commentsList.get(position);

        Log.d(Utils.TAG, " comm uname" + comment.getCommentUsername());
        Log.d(Utils.TAG, " comm date" + comment.getCommentPostDate());

        holder.comment_name.setText(comment.getCommentUsername());
        holder.comment_desc.setText(comment.getCommentDescription());
        holder.comment_date.setText(stripHtml(comment.getCommentPostDate()));

        Picasso.with(mContext).load(String.valueOf(comment.getCommentUserImage()))
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .resize(400, 400)
                .centerInside()
                .into(holder.profile_image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }
}
