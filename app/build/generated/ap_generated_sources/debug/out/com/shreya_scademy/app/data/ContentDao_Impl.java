package com.shreya_scademy.app.data;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ContentDao_Impl implements ContentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ContentModel> __insertionAdapterOfContentModel;

  private final EntityDeletionOrUpdateAdapter<ContentModel> __updateAdapterOfContentModel;

  public ContentDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContentModel = new EntityInsertionAdapter<ContentModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `content` (`id`,`user_id`,`course_id`,`content_id`,`content_limit`,`content_view`,`content_progress`,`updated`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ContentModel value) {
        stmt.bindLong(1, value.id);
        if (value.userId == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.userId);
        }
        if (value.courseId == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.courseId);
        }
        if (value.contentId == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.contentId);
        }
        stmt.bindLong(5, value.contentLimit);
        stmt.bindLong(6, value.contentView);
        if (value.contentProgress == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.contentProgress);
        }
        stmt.bindLong(8, value.updated);
      }
    };
    this.__updateAdapterOfContentModel = new EntityDeletionOrUpdateAdapter<ContentModel>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `content` SET `id` = ?,`user_id` = ?,`course_id` = ?,`content_id` = ?,`content_limit` = ?,`content_view` = ?,`content_progress` = ?,`updated` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ContentModel value) {
        stmt.bindLong(1, value.id);
        if (value.userId == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.userId);
        }
        if (value.courseId == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.courseId);
        }
        if (value.contentId == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.contentId);
        }
        stmt.bindLong(5, value.contentLimit);
        stmt.bindLong(6, value.contentView);
        if (value.contentProgress == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.contentProgress);
        }
        stmt.bindLong(8, value.updated);
        stmt.bindLong(9, value.id);
      }
    };
  }

  @Override
  public long insert(final ContentModel contentModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfContentModel.insertAndReturnId(contentModel);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final ContentModel contentModel) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfContentModel.handle(contentModel);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public ContentModel findContentById(final String userId, final String courseId,
      final String contentId) {
    final String _sql = "SELECT * FROM content WHERE user_id = ? AND course_id = ? AND content_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (courseId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, courseId);
    }
    _argIndex = 3;
    if (contentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, contentId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "course_id");
      final int _cursorIndexOfContentId = CursorUtil.getColumnIndexOrThrow(_cursor, "content_id");
      final int _cursorIndexOfContentLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "content_limit");
      final int _cursorIndexOfContentView = CursorUtil.getColumnIndexOrThrow(_cursor, "content_view");
      final int _cursorIndexOfContentProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "content_progress");
      final int _cursorIndexOfUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "updated");
      final ContentModel _result;
      if(_cursor.moveToFirst()) {
        _result = new ContentModel();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _result.userId = null;
        } else {
          _result.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfCourseId)) {
          _result.courseId = null;
        } else {
          _result.courseId = _cursor.getString(_cursorIndexOfCourseId);
        }
        if (_cursor.isNull(_cursorIndexOfContentId)) {
          _result.contentId = null;
        } else {
          _result.contentId = _cursor.getString(_cursorIndexOfContentId);
        }
        _result.contentLimit = _cursor.getInt(_cursorIndexOfContentLimit);
        _result.contentView = _cursor.getInt(_cursorIndexOfContentView);
        if (_cursor.isNull(_cursorIndexOfContentProgress)) {
          _result.contentProgress = null;
        } else {
          _result.contentProgress = _cursor.getLong(_cursorIndexOfContentProgress);
        }
        _result.updated = _cursor.getInt(_cursorIndexOfUpdated);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ContentModel> getAll() {
    final String _sql = "SELECT * FROM content WHERE updated = 0 ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "course_id");
      final int _cursorIndexOfContentId = CursorUtil.getColumnIndexOrThrow(_cursor, "content_id");
      final int _cursorIndexOfContentLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "content_limit");
      final int _cursorIndexOfContentView = CursorUtil.getColumnIndexOrThrow(_cursor, "content_view");
      final int _cursorIndexOfContentProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "content_progress");
      final int _cursorIndexOfUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "updated");
      final List<ContentModel> _result = new ArrayList<ContentModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ContentModel _item;
        _item = new ContentModel();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item.userId = null;
        } else {
          _item.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfCourseId)) {
          _item.courseId = null;
        } else {
          _item.courseId = _cursor.getString(_cursorIndexOfCourseId);
        }
        if (_cursor.isNull(_cursorIndexOfContentId)) {
          _item.contentId = null;
        } else {
          _item.contentId = _cursor.getString(_cursorIndexOfContentId);
        }
        _item.contentLimit = _cursor.getInt(_cursorIndexOfContentLimit);
        _item.contentView = _cursor.getInt(_cursorIndexOfContentView);
        if (_cursor.isNull(_cursorIndexOfContentProgress)) {
          _item.contentProgress = null;
        } else {
          _item.contentProgress = _cursor.getLong(_cursorIndexOfContentProgress);
        }
        _item.updated = _cursor.getInt(_cursorIndexOfUpdated);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
