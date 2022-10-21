package com.tech.socialworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tech.socialworld.Model.FollowModel;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.R;
import com.tech.socialworld.databinding.UserSampleBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<UserModel> list;

    public UserAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UserModel userModel = list.get(position);
        Picasso.get()
                .load(userModel.getProfile())
                .placeholder(R.drawable.man)
                .into(holder.binding.profileImage);
        holder.binding.name.setText(userModel.getName());
        holder.binding.profession.setText(userModel.getProfession());

        //when follow button is clicked by user
        holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowModel followModel = new FollowModel();
                followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                followModel.setFollowAt(new Date().getTime());

                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(userModel.getUserID())
                        .child("followers")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .setValue(followModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Users")
                                        .child(userModel.getUserID())
                                        .child("followerCount")
                                        .setValue(userModel.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "You Followed " + userModel.getName(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        UserSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UserSampleBinding.bind(itemView);
        }
    }
}
