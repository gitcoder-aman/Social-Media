package com.tech.socialworld.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.socialworld.Adapter.PostAdapter;
import com.tech.socialworld.Adapter.StoryAdapter;
import com.tech.socialworld.Model.PostModel;
import com.tech.socialworld.Model.StoryModel;
import com.tech.socialworld.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView storyRv, dashboardRV;
    ArrayList<StoryModel> storyList;
    ArrayList<PostModel> postList;
    ImageView addStory;
    FirebaseAuth auth;
    FirebaseDatabase database;

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

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        storyRv = view.findViewById(R.id.storyRV);

        storyList = new ArrayList<>();
        storyList.add(new StoryModel(R.drawable.kingfisher, R.drawable.video_camera, R.drawable.user, "Aman Kr"));
        storyList.add(new StoryModel(R.drawable.nature, R.drawable.live, R.drawable.user, "Priya Kri"));
        storyList.add(new StoryModel(R.drawable.animal, R.drawable.video_camera, R.drawable.user, "Kamal Kr"));
        storyList.add(new StoryModel(R.drawable.bird, R.drawable.video_camera, R.drawable.user, "Jyoti Kri"));
        storyList.add(new StoryModel(R.drawable.bird, R.drawable.video_camera, R.drawable.user, "Jyoti Kri"));
        storyList.add(new StoryModel(R.drawable.bird, R.drawable.video_camera, R.drawable.user, "Jyoti Kri"));

        StoryAdapter adapter = new StoryAdapter(storyList, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
//        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        //dashboard RecyclerView
        dashboardRV = view.findViewById(R.id.dashboardRV);
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
        dashboardRV.addItemDecoration(new DividerItemDecoration(dashboardRV.getContext(),DividerItemDecoration.HORIZONTAL));
        dashboardRV.setAdapter(postAdapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    postList.add(postModel);
                }
                postAdapter.notifyDataSetChanged();
//                dashboardRV.smoothScrollToPosition(dashboardRV.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}