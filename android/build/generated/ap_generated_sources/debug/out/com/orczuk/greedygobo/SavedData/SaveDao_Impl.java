package com.orczuk.greedygobo.SavedData;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SaveDao_Impl implements SaveDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SaveEntry> __insertionAdapterOfSaveEntry;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public SaveDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaveEntry = new EntityInsertionAdapter<SaveEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `SaveEntry` (`uid`,`currentHighScore`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SaveEntry value) {
        stmt.bindLong(1, value.uid);
        if (value.currentHighScore == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.currentHighScore);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM saveentry";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final SaveEntry... saveEntries) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSaveEntry.insert(saveEntries);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public List<SaveEntry> getAll() {
    final String _sql = "SELECT * FROM saveentry";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
      final int _cursorIndexOfCurrentHighScore = CursorUtil.getColumnIndexOrThrow(_cursor, "currentHighScore");
      final List<SaveEntry> _result = new ArrayList<SaveEntry>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SaveEntry _item;
        final String _tmpCurrentHighScore;
        _tmpCurrentHighScore = _cursor.getString(_cursorIndexOfCurrentHighScore);
        _item = new SaveEntry(_tmpCurrentHighScore);
        _item.uid = _cursor.getInt(_cursorIndexOfUid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
