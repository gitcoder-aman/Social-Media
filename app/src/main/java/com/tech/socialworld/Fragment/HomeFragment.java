package com.tech.socialworld.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.socialworld.Adapter.DashboardAdapter;
import com.tech.socialworld.Adapter.StoryAdapter;
import com.tech.socialworld.Model.DashboardModel;
import com.tech.socialworld.Model.StoryModel;
import com.tech.socialworld.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView storyRv, dashboardRV;
    ArrayList<StoryModel> list;
    ArrayList<DashboardModel> dashboardList;

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

        storyRv = view.findViewById(R.id.storyRV);

        list = new ArrayList<>();
        list.add(new StoryModel(R.drawable.kingfisher, R.drawable.video_camera, R.drawable.user, "Aman Kr"));
        list.add(new StoryModel(R.drawable.nature, R.drawable.live, R.drawable.user, "Priya Kri"));
        list.add(new StoryModel(R.drawable.animal, R.drawable.video_camera, R.drawable.user, "Kamal Kr"));
        list.add(new StoryModel(R.drawable.bird, R.drawable.video_camera, R.drawable.user, "Jyoti Kri"));
        list.add(new StoryModel(R.drawable.bird, R.drawable.video_camera, R.drawable.user, "Jyoti Kri"));
        list.add(new StoryModel(R.drawable.bird, R.drawable.video_camera, R.drawable.user, "Jyoti Kri"));

        StoryAdapter adapter = new StoryAdapter(list, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        dashboardRV = view.findViewById(R.id.dashboardRV);
        dashboardList = new ArrayList<>();
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing with Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing with Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));
        dashboardList.add(new DashboardModel(R.drawable.user, R.drawable.animal, R.drawable.ic_bookmark, "Aman Gupta", "Playing With Cat", "464", "12", "50"));

        DashboardAdapter dashboardAdapter = new DashboardAdapter(dashboardList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setNestedScrollingEnabled(false);
        dashboardRV.setAdapter(dashboardAdapter);
        return view;
    }
}