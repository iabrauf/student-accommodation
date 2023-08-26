package com.example.studentaccommodation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MyPosts extends Fragment {
    private RecyclerView recyclerView;

    private List<Post> allPosts;
    private SearchView searchView;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Post, PostViewHolder> adapter;
    private FirestoreRecyclerAdapter<Post, PostViewHolder> searchAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_posts, container, false);
        searchView = rootView.findViewById(R.id.searchView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Query query = db.collection("posts")
                    .whereEqualTo("ownerId", currentUserId)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            setupRecyclerView(query);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPosts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPosts(newText);
                return false;
            }
        });

        return rootView;
    }

    private void setupRecyclerView(Query query) {
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post post) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                holder.post_title.setText(post.getTitle());
                holder.post_description.setText(post.getDescription());
                holder.fNameTextView.setText(post.getfName()); // Add this line
                holder.phoneTextView.setText(post.getPhone());


                Glide.with(requireContext())
                        .load(post.getImage1())
                        .placeholder(R.drawable.baseline_camera_alt_24)
                        .into(holder.post_image_1);

                Glide.with(requireContext())
                        .load(post.getImage2())
                        .placeholder(R.drawable.baseline_camera_alt_24) // Replace with a placeholder image in your drawable folder
                        .into(holder.post_image_2);

                    holder.deleteButton.setVisibility(View.VISIBLE);
                    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int currentPosition = holder.getAdapterPosition();
                            if (currentPosition != RecyclerView.NO_POSITION) {
                                deletePost(currentPosition);
                            } else {
                                Toast.makeText(getActivity(), "Error: Unable to delete post", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }

        };

        recyclerView.setAdapter(adapter);
    }

    protected void searchPosts(String query) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Query searchQuery = db.collection("posts")
                    .whereEqualTo("ownerId", currentUserId)
                    .orderBy("title")
                    .startAt(query)
                    .endAt(query + "\uf8ff");

            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                    .setQuery(searchQuery, Post.class)
                    .build();

            searchAdapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {

                @NonNull
                @Override
                public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                    return new PostViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post post) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                    holder.post_title.setText(post.getTitle());
                    holder.post_description.setText(post.getDescription());
                    holder.fNameTextView.setText(post.getfName()); // Add this line
                    holder.phoneTextView.setText(post.getPhone());


                    Glide.with(requireContext())
                            .load(post.getImage1())
                            .placeholder(R.drawable.baseline_camera_alt_24)
                            .into(holder.post_image_1);

                    Glide.with(requireContext())
                            .load(post.getImage2())
                            .placeholder(R.drawable.baseline_camera_alt_24)
                            .into(holder.post_image_2);

                        holder.deleteButton.setVisibility(View.VISIBLE);
                    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int currentPosition = holder.getAdapterPosition();
                            if (currentPosition != RecyclerView.NO_POSITION) {
                                deletePost(currentPosition);
                            } else {
                                Toast.makeText(getActivity(), "Error: Unable to delete post", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            };

            searchAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    List<Post> sortedList = new ArrayList<>(searchAdapter.getSnapshots());
                    Collections.sort(sortedList, new Comparator<Post>() {
                        @Override
                        public int compare(Post o1, Post o2) {
                            if (o1.getTimestamp() == null || o2.getTimestamp() == null) {
                                return 0;
                            }
                            return Long.compare(o2.getTimestamp().toDate().getTime(), o1.getTimestamp().toDate().getTime());
                        }
                    });

                    searchAdapter.notifyDataSetChanged();
                }
            });


            recyclerView.setAdapter(searchAdapter);
            searchAdapter.startListening();
        }
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView post_title;
        TextView post_description;
        ImageView post_image_1;
        ImageView post_image_2;
        TextView fNameTextView;
        TextView phoneTextView;
        ImageButton deleteButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            post_title = itemView.findViewById(R.id.post_title);
            post_description = itemView.findViewById(R.id.post_description);
            post_image_1 = itemView.findViewById(R.id.post_image_1);
            post_image_2 = itemView.findViewById(R.id.post_image_2);
            fNameTextView = itemView.findViewById(R.id.fNameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
        if (searchAdapter != null && searchAdapter.getItemCount() > 0) {
            searchAdapter.stopListening();
        }
    }




    private void deletePost(int position) {
        FirestoreRecyclerAdapter<Post, PostViewHolder> currentAdapter = (searchAdapter != null && searchAdapter.getItemCount() > 0) ? searchAdapter : adapter;
        DocumentSnapshot snapshot = currentAdapter.getSnapshots().getSnapshot(position);
        String postId = snapshot.getId();
        db.collection("posts").document(postId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        String query = searchView.getQuery().toString();
                        if (!query.isEmpty()) {
                            searchPosts(query); // Refresh search results
                        } else {
                            refreshData(); // Refresh adapter data
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to delete post", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refreshData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Query query = db.collection("posts")
                    .whereEqualTo("ownerId", currentUserId)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            setupRecyclerView(query);
        }
    }






}