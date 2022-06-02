package com.shreya_scademy.app.ui.galary.galleryvideos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shreya_scademy.app.R;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.video.ActivityYoutubeVideo;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;
import com.shreya_scademy.app.utils.widgets.CustomTextExtraBold;

import java.util.ArrayList;

public class AdapterGalleryVideos extends RecyclerView.Adapter<AdapterGalleryVideos.AdapterGalleryVideosViewHolder> {

    Context mContext;
    String cId = "";
    ModelLogin modelLogin;
    ArrayList<YouTubeVideosModel.Data> videosUrlsList;
    SharedPref pref;
    private String subId;


    public AdapterGalleryVideos(Context mContext, ArrayList<YouTubeVideosModel.Data> videosUrlsList, String cId, String subId) {
        this.mContext = mContext;
        this.videosUrlsList = videosUrlsList;
        this.cId = cId;
        pref = SharedPref.getInstance(mContext);
        modelLogin = pref.getUser(AppConsts.STUDENT_DATA);
        this.subId = subId;
        Toast.makeText(mContext,"AdapterGalleryVideos\nTotal="+videosUrlsList.size(),Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public AdapterGalleryVideosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery_videos, viewGroup, false);
        return new AdapterGalleryVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGalleryVideosViewHolder holder, int i1) {

        int i = holder.getBindingAdapterPosition();
        holder.videoName.setText("" + videosUrlsList.get(i).getTitle());
        holder.videoName.setTextSize(14f);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videosUrlsList.get(i).getVideoType().equalsIgnoreCase("youtube")) {
                    mContext.startActivity(new Intent(mContext, ActivityYoutubeVideo.class)
                            .putExtra("vId", "" + videosUrlsList.get(i).getVideoId())
                            .putExtra("title", "" + videosUrlsList.get(i).getTitle())
                            .putExtra("type", "" + videosUrlsList.get(i).getVideoType())
                            .putExtra("WEB_URL", "" + videosUrlsList.get(i).getUrl())
                            .putExtra("COURSE_ID", "" + videosUrlsList.get(i).getUrl()));

                } else {
                    if (videosUrlsList.get(i).getVideoType().equalsIgnoreCase("video")) {
                        Intent intent = new Intent(mContext, ExoplayerVideos.class);
                        intent.putExtra("WEB_URL", "" + videosUrlsList.get(i).getUrl());
                        intent.putExtra("VIDEO_ID", "" + videosUrlsList.get(i).getVideoId());
                        intent.putExtra("COURSE_ID", "" + subId);
                        mContext.startActivity(intent);
                    } else {
                        mContext.startActivity(new Intent(mContext, ActivityVimeoVideo.class)
                                .putExtra("vId", "" + videosUrlsList.get(i).getVideoId())
                                .putExtra("title", "" + videosUrlsList.get(i).getTitle())
                                .putExtra("type", "" + videosUrlsList.get(i).getVideoType())
                                .putExtra("WEB_URL", "" + videosUrlsList.get(i).getUrl()));
                    }
                }


            }
        });


        if (50 < videosUrlsList.get(i).getTitle().length()) {
            holder.viewMore.setVisibility(View.VISIBLE);

        } else {
            holder.viewMore.setVisibility(View.GONE);
            holder.videoName.setMaxLines(Integer.MAX_VALUE);
        }
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.videoName.getMaxLines() == 2) {
                    holder.viewMore.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                    holder.videoName.setMaxLines(Integer.MAX_VALUE);
                } else {
                    holder.viewMore.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                    holder.videoName.setMaxLines(2);

                }
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return videosUrlsList.size();
    }

    class AdapterGalleryVideosViewHolder extends RecyclerView.ViewHolder {
        CustomTextExtraBold videoName;
        ImageView viewMore;

        public AdapterGalleryVideosViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.tvVideoName);
            viewMore = itemView.findViewById(R.id.viewMore);
        }
    }
}
