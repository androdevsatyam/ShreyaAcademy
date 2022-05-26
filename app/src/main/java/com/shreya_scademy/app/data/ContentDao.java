package com.shreya_scademy.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.shreya_scademy.app.App;

import java.util.List;

/**
 * @Autho Dharmesh
 * @Date 26-03-2022
 * <p>
 * Information
 **/

@Dao
public interface ContentDao {

    @Insert
    long insert(ContentModel contentModel);

    @Update
    int update(ContentModel contentModel);

    @Query("SELECT * FROM " + AppDatabase.CONTENT_TABLE + " WHERE user_id = :userId AND course_id = :courseId AND content_id = :contentId")
    ContentModel findContentById(String userId, String courseId, String contentId);

    @Query("SELECT * FROM " + AppDatabase.CONTENT_TABLE + " WHERE updated = 0 ")
    List<ContentModel> getAll();
}
