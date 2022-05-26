package com.shreya_scademy.app.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Authoer Dharmesh
 * @Date 26-03-2022
 * <p>
 * Information
 **/
@Entity(tableName = AppDatabase.CONTENT_TABLE)
public class ContentModel {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name = "course_id")
    public String courseId;

    @ColumnInfo(name = "content_id")
    public String contentId;

    @ColumnInfo(name = "content_limit")
    public int contentLimit;

    @ColumnInfo(name = "content_view")
    public int contentView;

    @ColumnInfo(name = "content_progress")
    public Long contentProgress;

    @ColumnInfo(name = "updated")
    public int updated;

    public String getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getContentLimit() {
        return contentLimit;
    }

    public void setContentLimit(int contentLimit) {
        this.contentLimit = contentLimit;
    }

    public int getContentView() {
        return contentView;
    }

    public void setContentView(int contentView) {
        this.contentView = contentView;
    }

    public Long getContentProgress() {
        return contentProgress;
    }

    public void setContentProgress(Long contentProgress) {
        this.contentProgress = contentProgress;
    }

    public int isUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }
}
