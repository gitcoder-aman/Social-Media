package com.tech.socialworld.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.socialworld.CommentActivity;
import com.tech.socialworld.Model.NotificationModel;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.R;
import com.tech.socialworld.databinding.Notification2sampleBinding;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification2sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel notificationModel = list.get(position);

        String type = notificationModel.getType();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notificationModel.getNotificationBy()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Picasso.get()
                                .load(userModel.getProfile())
                                .placeholder(R.drawable.man)
                                .into(holder.binding.profileImage);

                        if (type.equals("like")) {
                            holder.binding.notification.setText(Html.fromHtml("<b>" + userModel.getName() + "</b>" + " liked your post"));
                        } else if (type.equals("comment")) {
                            holder.binding.notification.setText(Html.fromHtml("<b>" + userModel.getName() + "</b>" + " commented on your post"));
                        } else {
                            holder.binding.notification.setText(Html.fromHtml("<b>" + userModel.getName() + "</b>" + " start following you"));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!type.equals("follow")) {

                    //for checkOpen
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(notificationModel.getPostedBy())
                            .child(notificationModel.getNotificationID())
                            .child("checkOpen")
                            .setValue(true);
                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postId", notificationModel.getPostID());
                    intent.putExtra("postedBy", notificationModel.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        boolean checkOpen = notificationModel.isCheckOpen();
        if(checkOpen){
            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        Notification2sampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = Notification2sampleBinding.bind(itemView);
        }
    }
}
