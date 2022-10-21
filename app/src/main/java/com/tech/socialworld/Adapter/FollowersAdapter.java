package com.tech.socialworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.socialworld.Model.FollowModel;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.R;
import com.tech.socialworld.databinding.FollowersRvSampleBinding;

import java.util.ArrayList;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.viewHolder>{

    ArrayList<FollowModel>list;
    Context context;

    public FollowersAdapter(ArrayList<FollowModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.followers_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        FollowModel followModel = list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(followModel.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        Picasso.get()
                                .load(userModel.getProfile())
                                .placeholder(R.drawable.man)
                                .into(holder.binding.profileImage);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

       FollowersRvSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = FollowersRvSampleBinding.bind(itemView);
        }
    }
}
