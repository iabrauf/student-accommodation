package com.example.studentaccommodation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.studentaccommodation.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.postTitle.setText(post.getTitle());
        holder.postDescription.setText(post.getDescription());


        Glide.with(context).load(post.getImage1())
                .into(holder.postImage1);
        Glide.with(context).load(post.getImage2())
                .into(holder.postImage2);

        holder.fNameTextView.setText(post.getfName());
        holder.phoneTextView.setText(post.getPhone());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle;
        TextView postDescription;
        ImageView postImage1;
        ImageView postImage2;

        TextView fNameTextView;
        TextView phoneTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.post_title);
            postDescription = itemView.findViewById(R.id.post_description);
            postImage1 = itemView.findViewById(R.id.post_image_1);
            postImage2 = itemView.findViewById(R.id.post_image_2);
            fNameTextView = itemView.findViewById(R.id.fNameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }
    }

}
