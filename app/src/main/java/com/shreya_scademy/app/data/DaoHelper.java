package com.shreya_scademy.app.data;

import com.shreya_scademy.app.App;

import java.util.List;

/**
 * @Authoer Dharmesh
 * @Date 26-03-2022
 * <p>
 * Information
 **/
public class DaoHelper {

    private final ContentDao contentDao;

    public DaoHelper(AppDatabase appDatabase) {
        contentDao = appDatabase.contentDao();
    }

    public long insertOrUpdate(ContentModel contentModel) {
        ContentModel content = contentDao.findContentById(contentModel.getUserId(),contentModel.getCourseId(),contentModel.getContentId());
        if (content == null) {
            return contentDao.insert(contentModel);
        } else {
            return contentDao.update(contentModel);
        }
    }

    public long updateContentView(String... params) {
        ContentModel contentModel = new ContentModel();
        return insertOrUpdate(contentModel);
    }

    public ContentModel getContentModel(long progress,int limit,int view,String... params){
        ContentModel contentModel = new ContentModel();
        contentModel.setUserId(params[0]);
        contentModel.setCourseId(params[1]);
        contentModel.setContentId(params[2]);
        contentModel.setContentLimit(limit);
        contentModel.setContentView(view);
        contentModel.setContentProgress(progress);
        return contentModel;
    }

    public List<ContentModel> getAll(){
        return contentDao.getAll();
    }
}
