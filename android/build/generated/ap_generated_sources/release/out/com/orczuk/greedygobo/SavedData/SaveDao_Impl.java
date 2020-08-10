package com.orczuk.greedygobo.SavedData;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SaveDao_Impl implements SaveDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SaveEntry> __insertionAdapterOfSaveEntry;

  private final EntityDeletionOrUpdateAdapter<SaveEntry> __updateAdapterOfSaveEntry;

  public SaveDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaveEntry = new EntityInsertionAdapter<SaveEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `SaveEntry` (`uid`,`currentHighScore`,`holyFlag`,`blueFlag`,`toxicFlag`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SaveEntry value) {
        stmt.bindLong(1, value.uid);
        if (value.currentHighScore == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.currentHighScore);
        }
        final Integer _tmp;
        _tmp = value.holyFlag == null ? null : (value.holyFlag ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp);
        }
        final Integer _tmp_1;
        _tmp_1 = value.blueFlag == null ? null : (value.blueFlag ? 1 : 0);
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
        final Integer _tmp_2;
        _tmp_2 = value.toxicFlag == null ? null : (value.toxicFlag ? 1 : 0);
        if (_tmp_2 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_2);
        }
      }
    };
    this.__updateAdapterOfSaveEntry = new EntityDeletionOrUpdateAdapter<SaveEntry>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `SaveEntry` SET `uid` = ?,`currentHighScore` = ?,`holyFlag` = ?,`blueFlag` = ?,`toxicFlag` = ? WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SaveEntry value) {
        stmt.bindLong(1, value.uid);
        if (value.currentHighScore == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.currentHighScore);
        }
        final Integer _tmp;
        _tmp = value.holyFlag == null ? null : (value.holyFlag ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp);
        }
        final Integer _tmp_1;
        _tmp_1 = value.blueFlag == null ? null : (value.blueFlag ? 1 : 0);
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
        final Integer _tmp_2;
        _tmp_2 = value.toxicFlag == null ? null : (value.toxicFlag ? 1 : 0);
        if (_tmp_2 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_2);
        }
        stmt.bindLong(6, value.uid);
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
  public void updateEntry(final SaveEntry... saveEntries) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSaveEntry.handleMultiple(saveEntries);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
      final int _cursorIndexOfHolyFlag = CursorUtil.getColumnIndexOrThrow(_cursor, "holyFlag");
      final int _cursorIndexOfBlueFlag = CursorUtil.getColumnIndexOrThrow(_cursor, "blueFlag");
      final int _cursorIndexOfToxicFlag = CursorUtil.getColumnIndexOrThrow(_cursor, "toxicFlag");
      final List<SaveEntry> _result = new ArrayList<SaveEntry>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SaveEntry _item;
        final String _tmpCurrentHighScore;
        _tmpCurrentHighScore = _cursor.getString(_cursorIndexOfCurrentHighScore);
        final Boolean _tmpHolyFlag;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfHolyFlag)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfHolyFlag);
        }
        _tmpHolyFlag = _tmp == null ? null : _tmp != 0;
        final Boolean _tmpBlueFlag;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBlueFlag)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfBlueFlag);
        }
        _tmpBlueFlag = _tmp_1 == null ? null : _tmp_1 != 0;
        final Boolean _tmpToxicFlag;
        final Integer _tmp_2;
        if (_cursor.isNull(_cursorIndexOfToxicFlag)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getInt(_cursorIndexOfToxicFlag);
        }
        _tmpToxicFlag = _tmp_2 == null ? null : _tmp_2 != 0;
        _item = new SaveEntry(_tmpCurrentHighScore,_tmpHolyFlag,_tmpBlueFlag,_tmpToxicFlag);
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
