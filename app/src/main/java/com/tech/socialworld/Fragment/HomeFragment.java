package com.tech.socialworld.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tech.socialworld.Adapter.PostAdapter;
import com.tech.socialworld.Adapter.StoryAdapter;
import com.tech.socialworld.Model.PostModel;
import com.tech.socialworld.Model.StoryModel;
import com.tech.socialworld.Model.UserStories;
import com.tech.socialworld.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    RecyclerView storyRv;
    ShimmerRecyclerView dashboardRV;
    ArrayList<StoryModel> storyList;
    ArrayList<PostModel> postList;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    RoundedImageView addStoryImage;
    Uri selectImageUri;
    ProgressDialog dialog,storyDialog;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dashboardRV = view.findViewById(R.id.dashboardRV);
        dashboardRV.showShimmerAdapter();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Open gallery");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        storyDialog = new ProgressDialog(getContext());
        storyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        storyDialog.setTitle("Story Uploading...");
        storyDialog.setMessage("Please wait...");
        storyDialog.setCancelable(false);


        storyRv = view.findViewById(R.id.storyRV);

        storyList = new ArrayList<>();


        StoryAdapter adapter = new StoryAdapter(storyList, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.addItemDecoration(new DividerItemDecoration(storyRv.getContext(),DividerItemDecoration.HORIZONTAL));
        storyRv.setAdapter(adapter);

        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            storyList.clear();
                            for (DataSnapshot storySnapshot : snapshot.getChildren()){
                                StoryModel storyModel = new StoryModel();
                                storyModel.setStoryBy(storySnapshot.getKey()); // key is just user id
                                storyModel.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStories>stories = new ArrayList<>();
                                for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()){
                                    UserStories userStories = snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }
                                storyModel.setStories(stories);
                                storyList.add(storyModel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //dashboard RecyclerView
        postList = new ArrayList<>();
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing with Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing with Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
//        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));

        PostAdapter postAdapter = new PostAdapter(postList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setNestedScrollingEnabled(false);
        dashboardRV.addItemDecoration(new DividerItemDecoration(dashboardRV.getContext(),DividerItemDecoration.VERTICAL));

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    postList.add(postModel);
                }
                dashboardRV.setAdapter(postAdapter);
                dashboardRV.hideShimmerAdapter();
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addStoryImage = view.findViewById(R.id.storyImage);
        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                galleryLauncher.launch(intent);
            }
        });
        return view;
    }

    ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        dialog.dismiss();
                        Intent data = result.getData();

                        if (data != null && data.getData() != null) {
                            selectImageUri = data.getData();
                            Bitmap selectedImageBitmap = null;

                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(HomeFragment.this.requireContext().getContentResolver(), selectImageUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            addStoryImage.setImageBitmap(selectedImageBitmap);

                            storyDialog.show();
                            //stories store in storage
                            final StorageReference reference = storage.getReference()
                                    .child("stories")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(new Date().getTime()+"");

                            reference.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            StoryModel storyModel = new StoryModel();
                                            storyModel.setStoryAt(new Date().getTime());
                                            database.getReference()
                                                    .child("stories")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("postedBy")
                                                    .setValue(storyModel.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            UserStories stories = new UserStories(uri.toString(), storyModel.getStoryAt());

                                                            database.getReference()
                                                                    .child("stories")
                                                                    .child(FirebaseAuth.getInstance().getUid())
                                                                    .child("userStories")
                                                                    .push() // create random child
                                                                    .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            storyDialog.dismiss();
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                                }
                            });

                        }
                    }
                    dialog.dismiss();
                }
            });
}