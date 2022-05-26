package com.shreya_scademy.app.utils;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.shreya_scademy.app.data.AppDatabase;
import com.shreya_scademy.app.data.ContentModel;
import com.shreya_scademy.app.data.DaoHelper;
import com.shreya_scademy.app.ui.galary.galleryvideos.ContentViewCountModel;
import com.shreya_scademy.app.ui.galary.galleryvideos.UpdateVideoViewModel;

import java.util.List;

/**
 * @Authoer Dharmesh
 * @Date 03-04-2022
 * <p>
 * Information
 **/
public class NetworkApiCaller {

    private final DaoHelper daoHelper;
    private boolean isConnected;
    private List<ContentModel> contentModelList;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public NetworkApiCaller(DaoHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    public void updateContentView() {
        if (isConnected) {
            contentModelList = daoHelper.getAll();
            if (contentModelList != null && !contentModelList.isEmpty()) {
                ContentModel contentModel = contentModelList.get(0);
                updateContentCount(contentModel);
            }
        }
    }

    private void updateContentCount(ContentModel contentModel) {
        if (isConnected) {
            AndroidNetworking.get(AppConsts.BASE_URL + AppConsts.UPDATE_VIDEO_VIEW_COUNT)
                    .addPathParameter(AppConsts.VIDEO_ID, contentModel.getContentId())
                    .addPathParameter(AppConsts.USER_ID, contentModel.getUserId())
                    .addPathParameter(AppConsts.COUNT, String.valueOf(contentModel.getContentView()))
                    .build().getAsObject(UpdateVideoViewModel.class, new ParsedRequestListener<UpdateVideoViewModel>() {
                @Override
                public void onResponse(UpdateVideoViewModel response) {
                    if (response != null) {
                        if (response.getData() != null) {
                            contentModel.setContentLimit(response.getData().getViewLimit());
                            if (response.getData().getCount() != null && !response.getData().getCount().isEmpty()) {
                                contentModel.setContentView(Integer.parseInt(response.getData().getCount()));
                            }
                            daoHelper.insertOrUpdate(contentModel);
                        }
                        contentModelList.remove(0);
                        if (!contentModelList.isEmpty()) {
                            updateContentCount(contentModelList.get(0));
                        } else {
                            updateContentView();
                        }
                    }
                }

                @Override
                public void onError(ANError anError) {
                    contentModelList.remove(0);
                    if (!contentModelList.isEmpty()) {
                        updateContentCount(contentModelList.get(0));
                    } else {
                        updateContentView();
                    }
                }
            });
        }
    }


}
