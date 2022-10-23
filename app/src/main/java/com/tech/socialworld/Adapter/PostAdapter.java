package com.tech.socialworld.Adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.socialworld.Model.PostModel;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.R;
import com.tech.socialworld.databinding.DashboardRvSampleBinding;

import java.util.ArrayList;
import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<PostModel> list;
    Context context;

    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel postModel = list.get(position);
        Picasso.get()
                .load(postModel.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.postImage);
        holder.binding.like.setText(postModel.getPostLike() + "");

        String description = postModel.getPostDescription();
        if (description.equals("")) {
            holder.binding.postDescription.setVisibility(View.GONE);
        } else {
            holder.binding.postDescription.setText(postModel.getPostDescription());
            holder.binding.postDescription.setVisibility(View.VISIBLE);
        }

        //set name,profession,profile
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(postModel.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Picasso.get()
                                .load(Objects.requireNonNull(userModel).getProfile())
                                .placeholder(R.drawable.man)
                                .into(holder.binding.profileImage);
                        holder.binding.userName.setText(userModel.getName());
                        holder.binding.bio.setText(userModel.getProfession());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //like function

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(postModel.getPostId())
                .child("likes")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
                        } else {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(postModel.getPostId())
                                            .child("likes")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(postModel.getPostId())
                                                            .child("postLike")
                                                            .setValue(postModel.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
                                                                   // Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                        }
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

    public static class viewHolder extends RecyclerView.ViewHolder {

        DashboardRvSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvSampleBinding.bind(itemView);

        }
    }
}
