package com.orczuk.greedygobo.SavedData;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SaveDatabase_AppDatabase_Impl extends SaveDatabase.AppDatabase {
  private volatile SaveDao _saveDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SaveEntry` (`uid` INTEGER NOT NULL, `currentHighScore` TEXT, `holyFlag` INTEGER, `blueFlag` INTEGER, `toxicFlag` INTEGER, PRIMARY KEY(`uid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a00bacd8d37149b8c7da14894bd07d46')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `SaveEntry`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSaveEntry = new HashMap<String, TableInfo.Column>(5);
        _columnsSaveEntry.put("uid", new TableInfo.Column("uid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveEntry.put("currentHighScore", new TableInfo.Column("currentHighScore", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveEntry.put("holyFlag", new TableInfo.Column("holyFlag", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveEntry.put("blueFlag", new TableInfo.Column("blueFlag", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveEntry.put("toxicFlag", new TableInfo.Column("toxicFlag", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSaveEntry = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSaveEntry = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSaveEntry = new TableInfo("SaveEntry", _columnsSaveEntry, _foreignKeysSaveEntry, _indicesSaveEntry);
        final TableInfo _existingSaveEntry = TableInfo.read(_db, "SaveEntry");
        if (! _infoSaveEntry.equals(_existingSaveEntry)) {
          return new RoomOpenHelper.ValidationResult(false, "SaveEntry(com.orczuk.greedygobo.SavedData.SaveEntry).\n"
                  + " Expected:\n" + _infoSaveEntry + "\n"
                  + " Found:\n" + _existingSaveEntry);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a00bacd8d37149b8c7da14894bd07d46", "5b7848d382e9cc81f8f6322aecaf4b60");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "SaveEntry");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `SaveEntry`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public SaveDao saveDao() {
    if (_saveDao != null) {
      return _saveDao;
    } else {
      synchronized(this) {
        if(_saveDao == null) {
          _saveDao = new SaveDao_Impl(this);
        }
        return _saveDao;
      }
    }
  }
}
