package com.shreya_scademy.app.ui.galary.galleryvideos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.model.modelgallery.ModelVideo;
import com.shreya_scademy.app.model.modelgallery.ModelVideoTopics;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.home.ActivityHome;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.ProjectUtils;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;
import com.shreya_scademy.app.utils.widgets.CustomTextExtraBold;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityGalleryVideos extends BaseActivity {
    ImageView ivBack;
    RecyclerView rvVideos;
    Context mContext;
    AdapterGalleryVideos adapterGalleryVideos;
    ArrayList<YouTubeVideosModel.Data> videosUrlsList;
    SharedPref sharedPref;
    ModelLogin modelLogin;
    ImageView tvNoRecord;
    int page = 1;
    boolean isLoading = false;
    String tag = "";
    String subId = "";
    String nameSubject = "";
    CustomTextExtraBold tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery_videos);
        mContext = ActivityGalleryVideos.this;
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);

        if (!ProjectUtils.checkConnection(mContext)) {
            Toast.makeText(mContext, getResources().getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
        }
        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        rvVideos = (RecyclerView) findViewById(R.id.rvVideos);
        tvNoRecord = findViewById(R.id.no_record_found);
        if (ProjectUtils.checkConnection(mContext)) {
            subjectApi();
        } else {
            Toast.makeText(mContext, getResources().getString(R.string.NoInternetConnection), Toast.LENGTH_SHORT).show();
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void subjectApi() {
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.Loading___));
        tvHeader.setText(getResources().getString(R.string.subject));
        tag = "sub";
        AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_HOME_GET_SUB)
                .addBodyParameter(AppConsts.IS_ADMIN, "" + modelLogin.getStudentData().getAdminId())
                .addBodyParameter(AppConsts.BATCH_ID, "" + modelLogin.getStudentData().getBatchId())
                .addBodyParameter(AppConsts.STUDENT_ID, "" + modelLogin.getStudentData().getStudentId())
                .build()
                .getAsObject(ModelVideo.class, new ParsedRequestListener<ModelVideo>() {
                    @Override
                    public void onResponse(ModelVideo response) {
                        if (response.getStatus().equalsIgnoreCase("true") && response.getSubject() != null && !response.getSubject().isEmpty()) {
                            tvNoRecord.setVisibility(View.GONE);
//                            ArrayList<String> arrayList = new ArrayList<>();
//                            ArrayList<String> arrayListId = new ArrayList<>();
//
//                            for (int i = 0; i < response.getSubject().size(); i++) {
//                                arrayList.add("" + response.getSubject().get(i).getSubjectName());
//                                arrayListId.add("" + response.getSubject().get(i).getId());
//
//                            }
                            tvNoRecord.setVisibility(View.GONE);
                            AdapterSubjectList adapterSubjectList = new AdapterSubjectList(response.getSubject(), "subject");
                            rvVideos.setLayoutManager(new LinearLayoutManager(mContext));
                            rvVideos.setAdapter(adapterSubjectList);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                        }
                        ProjectUtils.pauseProgressDialog();
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                    }
                });

    }

    void topicApi(String s, String name) {
        tvHeader.setText("" + name);
        nameSubject = name;
        tag = "back";
        AndroidNetworking.post(AppConsts.BASE_URL + AppConsts.API_HOME_GET_CHAPTER)
                .addBodyParameter(AppConsts.SUBJECT_ID, "" + s)
                .build()
                .getAsObject(ModelVideoTopics.class, new ParsedRequestListener<ModelVideoTopics>() {
                    @Override
                    public void onResponse(ModelVideoTopics response) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        if (response.getStatus().equalsIgnoreCase("true")) {
                            tvNoRecord.setVisibility(View.GONE);
                            for (int i = 0; i < response.getChapter().size(); i++) {
                                arrayList.add("" + response.getChapter().get(i).getChapterName());
                            }
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);
                        }
                        AdapterTopicList adapterSubjectList = new AdapterTopicList(arrayList, arrayList, "topic");
                        rvVideos.setLayoutManager(new LinearLayoutManager(mContext));
                        rvVideos.setAdapter(adapterSubjectList);

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    private void playVideoApi(final String url, String chapter) {

        tag = "back1";
        final HashMap<String, String> stringStringHashMap = new HashMap<>();

        stringStringHashMap.put(AppConsts.IS_ADMIN, modelLogin.getStudentData().getAdminId());
        stringStringHashMap.put(AppConsts.BATCH_ID, "" + modelLogin.getStudentData().getBatchId());
        stringStringHashMap.put("chapter", "" + chapter);
        tvHeader.setText(chapter);

        AndroidNetworking.post(AppConsts.BASE_URL + url)
                .addBodyParameter(stringStringHashMap)
                .addBodyParameter(AppConsts.PAGE_NO, page + "")
                .setTag(AppConsts.API_PLAY_VIDEO)
                .build()
                .getAsObject(YouTubeVideosModel.class, new ParsedRequestListener<YouTubeVideosModel>() {
                    @Override
                    public void onResponse(YouTubeVideosModel response) {
                        ProjectUtils.pauseProgressDialog();
                        rvVideos.setAdapter(null);

                        if (AppConsts.TRUE.equals(response.getStatus())) {
                            videosUrlsList = new ArrayList<>();
                            videosUrlsList = response.getVideoLecture();
                            tvNoRecord.setVisibility(View.GONE);
                            adapterGalleryVideos = new AdapterGalleryVideos(mContext, videosUrlsList, response.getEncCode(),subId);
                            rvVideos.setLayoutManager(new LinearLayoutManager(mContext));
                            rvVideos.setAdapter(adapterGalleryVideos);
                        } else {
                            tvNoRecord.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                    }
                });

    }


    @Override
    public void onBackPressed() {
        if (tag.equals("back")) {
            subjectApi();
        } else {
            if (tag.equals("back1")) {
                if (subId.isEmpty()) {
                    subjectApi();
                } else {
                    topicApi(subId, nameSubject);
                }
            } else {
                mContext.startActivity(new Intent(mContext, ActivityHome.class));
                page = 1;
                isLoading = false;
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void showPopUpMenu(View view, ModelVideo.Data data) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.course_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_renew_course:
                        renewCourse(item);
                        break;
                    case R.id.menu_transfer_course:
                        transferCourse(item);
                        break;
                    case R.id.menu_rate_course:
                        rateCourse(item);
                        break;
                    case R.id.menu_download_offline:
                        downloadCourse(data);
                        break;
                }
                return false;
            }
        });
//        if (popup.getMenu() instanceof MenuBuilder){
//            MenuBuilder menuBuilder = (MenuBuilder) popup.getMenu();
//            menuBuilder.setOptionalIconsVisible(true);
//            for (MenuItemImpl item1 : menuBuilder.getVisibleItems()) {
//                int iconMarginPx =
//                        TypedValue.applyDimension(
//                                TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())
//                                .toInt();
//                if (item1.getIcon() != null) {
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                        item1.setIcon(new InsetDrawable(item1.getIcon(), iconMarginPx, 0, iconMarginPx, 0));
//                    } else {
//                        item1.setIcon(new InsetDrawable(item1.getIcon(), iconMarginPx, 0, iconMarginPx, 0) {
//                            @Override
//                            public int getIntrinsicWidth() {
//                                return getIntrinsicHeight() + iconMarginPx + iconMarginPx;
//                            }
//                        });
//                    }
//                }
//            }
//        }
        popup.show();
    }

    private void renewCourse(MenuItem item) {
    }

    private void transferCourse(MenuItem item) {
    }

    private void rateCourse(MenuItem item) {
    }

    private void downloadCourse(ModelVideo.Data data) {

    }

    /*----------------------------------------------------*/
    public class AdapterSubjectList extends RecyclerView.Adapter<HolderAdapterQuestionList> {

        ArrayList<ModelVideo.Data> strings;
        String string;
        View view;

        public AdapterSubjectList(ArrayList<ModelVideo.Data> strings, String str) {
            this.strings = strings;
            this.string = str;
        }

        @NonNull
        @Override
        public HolderAdapterQuestionList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery_subject, viewGroup, false);
            return new HolderAdapterQuestionList(view);

        }

        @Override
        public void onBindViewHolder(@NonNull HolderAdapterQuestionList holder, int i) {
            ModelVideo.Data item = strings.get(i);
            holder.tvName.setText(item.getSubjectName());
            holder.tvName.setTextSize(14f);
            if (string.equalsIgnoreCase("subject")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.subjects));
            }
            if (string.contains("topic")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.topic));
            }

            holder.llSublist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (string.equals("topic")) {
                        playVideoApi(AppConsts.API_PLAY_VIDEO, "" + item.getSubjectName());
                    } else {
                        subId = "" + item.getId();
                        topicApi("" + item.getId(), "" + item.getSubjectName());
                    }
                }
            });

            if (item.getSubjectName().length() > 50) {
                holder.viewMore.setVisibility(View.VISIBLE);
            } else {
                holder.viewMore.setVisibility(View.GONE);
            }
            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.tvName.getMaxLines() == 2) {
                        holder.viewMore.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                        holder.tvName.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        holder.viewMore.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                        holder.tvName.setMaxLines(2);
                    }
                }
            });

            holder.imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUpMenu(holder.imgMenu, item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }

    /*----------------------------------------------------*/
    public class AdapterTopicList extends RecyclerView.Adapter<HolderAdapterQuestionList> {

        ArrayList<String> strings;
        ArrayList<String> id;
        String string;
        View view;

        public AdapterTopicList(ArrayList<String> strings, ArrayList<String> id, String str) {
            this.strings = strings;
            this.id = id;
            this.string = str;
        }

        @NonNull
        @Override
        public HolderAdapterQuestionList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery_videos, viewGroup, false);
            return new HolderAdapterQuestionList(view);

        }

        @Override
        public void onBindViewHolder(@NonNull HolderAdapterQuestionList holder, int i1) {
            int i = holder.getBindingAdapterPosition();
            holder.tvName.setText(strings.get(i));
            holder.tvName.setTextSize(14f);
            if (string.equalsIgnoreCase("subject")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.subjects));
            }
            if (string.contains("topic")) {
                holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.topic));
            }

            holder.llSublist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideoApi(AppConsts.API_PLAY_VIDEO, "" + strings.get(i));
                }
            });

            if (strings.get(i).length() > 50) {
                holder.viewMore.setVisibility(View.VISIBLE);
            } else {
                holder.viewMore.setVisibility(View.GONE);
            }
            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.tvName.getMaxLines() == 2) {
                        holder.viewMore.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                        holder.tvName.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        holder.viewMore.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                        holder.tvName.setMaxLines(2);

                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }

    public class HolderAdapterQuestionList extends RecyclerView.ViewHolder {
        LinearLayout llSublist;
        CustomTextExtraBold tvName;
        ImageView viewMore;
        ImageView img;
        ImageView imgMenu;

        public HolderAdapterQuestionList(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvVideoName);
            viewMore = itemView.findViewById(R.id.viewMore);
            llSublist = (LinearLayout) itemView.findViewById(R.id.llsublist);
            img = itemView.findViewById(R.id.img);
            imgMenu = itemView.findViewById(R.id.imgMenu);
        }
    }
}
