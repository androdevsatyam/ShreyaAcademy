package com.shreya_scademy.app.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @Authoer Dharmesh
 * @Date 26-03-2022
 * <p>
 * Information
 **/
@Database(entities = {ContentModel.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public final static String CONTENT_TABLE ="content";

    public abstract ContentDao contentDao();
}
