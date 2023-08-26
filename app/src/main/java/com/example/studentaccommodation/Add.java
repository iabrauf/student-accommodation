package com.example.studentaccommodation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Add extends Fragment {
    private EditText priceEditText;
    private EditText universityEditText;
    private EditText locationEditText;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private ImageView imageView1;
    private ImageView imageView2;
    private Button uploadImageButton1;
    private Button uploadImageButton2;
    private Button createPostButton;
    private Uri imageUri1;
    private Uri imageUri2;
    private static final int PICK_IMAGE1 = 1;
    private static final int PICK_IMAGE2 = 2;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        mAuth = FirebaseAuth.getInstance();
        titleEditText = rootView.findViewById(R.id.titleEditText);
        descriptionEditText = rootView.findViewById(R.id.descriptionEditText);
        imageView1 = rootView.findViewById(R.id.imageView1);
        imageView2 = rootView.findViewById(R.id.imageView2);
        uploadImageButton1 = rootView.findViewById(R.id.uploadImageButton1);
        uploadImageButton2 = rootView.findViewById(R.id.uploadImageButton2);
        createPostButton = rootView.findViewById(R.id.createPostButton);
        progressBar = rootView.findViewById(R.id.progressBar);
        priceEditText = rootView.findViewById(R.id.priceEditText);
        universityEditText = rootView.findViewById(R.id.universityEditText);
        locationEditText = rootView.findViewById(R.id.locationEditText);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        uploadImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(PICK_IMAGE1);
            }
        });

        uploadImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(PICK_IMAGE2);
            }
        });

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });

        return rootView;
    }

    private void chooseImage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE1) {
                imageUri1 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri1);
                    imageView1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_IMAGE2) {
                imageUri2 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri2);
                    imageView2.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createPost() {
        final String title = titleEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();
        final String price = priceEditText.getText().toString().trim();
        final String university = universityEditText.getText().toString().trim();
        final String location = locationEditText.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(getActivity(), "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        if (imageUri1 == null || imageUri2 == null) {
            Toast.makeText(getActivity(), "Please select two images", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (imageUri1.equals(imageUri2)) {
            Toast.makeText(getActivity(), "Please upload different images", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (price.isEmpty() || university.isEmpty() || location.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String userName = documentSnapshot.getString("fName");
                    String phoneNumber = documentSnapshot.getString("phone");

                    final Map<String, Object> post = new HashMap<>();
                    post.put("title", title);
                    post.put("description", description);
                    post.put("fName", userName);
                    post.put("phone", phoneNumber);
                    post.put("ownerId", userId);
                    post.put("timestamp", FieldValue.serverTimestamp());
                    post.put("price", price);
                    post.put("university", university);
                    post.put("location", location);

                    final StorageReference filePath1 = storageReference.child("images").child(imageUri1.getLastPathSegment());
                    filePath1.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl1 = uri.toString();
                                    post.put("image1", imageUrl1);

                                    final StorageReference filePath2 = storageReference.child("images").child(imageUri2.getLastPathSegment());
                                    filePath2.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl2 = uri.toString();
                                                    post.put("image2", imageUrl2);

                                                    savePostToFirestore(post);
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }



    private void savePostToFirestore(Map<String, Object> post) {
        db.collection("posts").add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Post created successfully", Toast.LENGTH_SHORT).show();
                        titleEditText.setText("");
                        descriptionEditText.setText("");
                        priceEditText.setText("");
                        universityEditText.setText("");
                        locationEditText.setText("");
                        imageView1.setImageResource(android.R.drawable.ic_menu_camera);
                        imageView2.setImageResource(android.R.drawable.ic_menu_camera);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to create post", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

}



