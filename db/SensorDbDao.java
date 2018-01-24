package com.moocall.moocall.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class SensorDbDao extends AbstractDao<SensorDb, Long> {
    public static final String TABLENAME = "SENSOR_DB";

    public static class Properties {
        public static final Property Device_code = new Property(1, String.class, "device_code", false, "DEVICE_CODE");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Type = new Property(2, Integer.TYPE, "type", false, "TYPE");
    }

    public SensorDbDao(DaoConfig config) {
        super(config);
    }

    public SensorDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"SENSOR_DB\" (" + "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + "\"DEVICE_CODE\" TEXT NOT NULL ," + "\"TYPE\" INTEGER NOT NULL );");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SENSOR_DB\"");
    }

    protected final void bindValues(DatabaseStatement stmt, SensorDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getDevice_code());
        stmt.bindLong(3, (long) entity.getType());
    }

    protected final void bindValues(SQLiteStatement stmt, SensorDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindString(2, entity.getDevice_code());
        stmt.bindLong(3, (long) entity.getType());
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public SensorDb readEntity(Cursor cursor, int offset) {
        return new SensorDb(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.getString(offset + 1), cursor.getInt(offset + 2));
    }

    public void readEntity(Cursor cursor, SensorDb entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setDevice_code(cursor.getString(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
    }

    protected final Long updateKeyAfterInsert(SensorDb entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(SensorDb entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(SensorDb entity) {
        return entity.getId() != null;
    }

    protected final boolean isEntityUpdateable() {
        return true;
    }
}
