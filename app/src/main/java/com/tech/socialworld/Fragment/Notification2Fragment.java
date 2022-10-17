package com.tech.socialworld.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.socialworld.Adapter.NotificationAdapter;
import com.tech.socialworld.Model.NotificationModel;
import com.tech.socialworld.R;

import java.util.ArrayList;


public class Notification2Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<NotificationModel>list;

    public Notification2Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification2, container, false);

        recyclerView = view.findViewById(R.id.notificationRV);
        list = new ArrayList<>();
        list.add(new NotificationModel(R.drawable.man,"<b>Tej</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Tej</b> Mention in your comment,you also comment in your post"
                ,"just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Tej</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Tej</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));
        list.add(new NotificationModel(R.drawable.man,"<b>Rakesh</b> Mention in your comment","just now"));

        NotificationAdapter adapter = new NotificationAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
        return view;
    }
}