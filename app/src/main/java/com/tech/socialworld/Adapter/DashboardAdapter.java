package com.tech.socialworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.socialworld.Model.DashboardModel;
import com.tech.socialworld.R;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.viewHolder>{

    ArrayList<DashboardModel>list;
    Context context;

    public DashboardAdapter(ArrayList<DashboardModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DashboardModel model = list.get(position);

        holder.profile.setImageResource(model.getProfile());
        holder.postImage.setImageResource(model.getPostImage());
        holder.name.setText(model.getName());
        holder.about.setText(model.getAbout());
        holder.like.setText(model.getLike());
        holder.commment.setText(model.getComment());
        holder.share.setText(model.getShare());
        holder.save.setImageResource(model.getSave());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        ImageView profile,postImage,save;
        TextView name,about,like,commment,share;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.postImage);
            save = itemView.findViewById(R.id.save);
            name = itemView.findViewById(R.id.userName);
            about = itemView.findViewById(R.id.about);
            commment = itemView.findViewById(R.id.comment);
            like = itemView.findViewById(R.id.like);
            share = itemView.findViewById(R.id.share);

        }
    }
}
