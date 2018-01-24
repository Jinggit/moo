package com.moocall.moocall.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class CalvingDbDao extends AbstractDao<CalvingDb, Long> {
    public static final String TABLENAME = "CALVING_DB";

    public static class Properties {
        public static final Property Bull_id = new Property(3, String.class, "bull_id", false, "BULL_ID");
        public static final Property Bull_tag_number = new Property(4, String.class, "bull_tag_number", false, "BULL_TAG_NUMBER");
        public static final Property Calves = new Property(7, String.class, "calves", false, "CALVES");
        public static final Property Calves_number = new Property(6, String.class, "calves_number", false, "CALVES_NUMBER");
        public static final Property Cow_id = new Property(1, String.class, "cow_id", false, "COW_ID");
        public static final Property Cow_tag_number = new Property(2, String.class, "cow_tag_number", false, "COW_TAG_NUMBER");
        public static final Property Datetime = new Property(5, String.class, "datetime", false, "DATETIME");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    }

    public CalvingDbDao(DaoConfig config) {
        super(config);
    }

    public CalvingDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"CALVING_DB\" (" + "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + "\"COW_ID\" TEXT," + "\"COW_TAG_NUMBER\" TEXT," + "\"BULL_ID\" TEXT," + "\"BULL_TAG_NUMBER\" TEXT," + "\"DATETIME\" TEXT," + "\"CALVES_NUMBER\" TEXT," + "\"CALVES\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CALVING_DB\"");
    }

    protected final void bindValues(DatabaseStatement stmt, CalvingDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String cow_id = entity.getCow_id();
        if (cow_id != null) {
            stmt.bindString(2, cow_id);
        }
        String cow_tag_number = entity.getCow_tag_number();
        if (cow_tag_number != null) {
            stmt.bindString(3, cow_tag_number);
        }
        String bull_id = entity.getBull_id();
        if (bull_id != null) {
            stmt.bindString(4, bull_id);
        }
        String bull_tag_number = entity.getBull_tag_number();
        if (bull_tag_number != null) {
            stmt.bindString(5, bull_tag_number);
        }
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(6, datetime);
        }
        String calves_number = entity.getCalves_number();
        if (calves_number != null) {
            stmt.bindString(7, calves_number);
        }
        String calves = entity.getCalves();
        if (calves != null) {
            stmt.bindString(8, calves);
        }
    }

    protected final void bindValues(SQLiteStatement stmt, CalvingDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String cow_id = entity.getCow_id();
        if (cow_id != null) {
            stmt.bindString(2, cow_id);
        }
        String cow_tag_number = entity.getCow_tag_number();
        if (cow_tag_number != null) {
            stmt.bindString(3, cow_tag_number);
        }
        String bull_id = entity.getBull_id();
        if (bull_id != null) {
            stmt.bindString(4, bull_id);
        }
        String bull_tag_number = entity.getBull_tag_number();
        if (bull_tag_number != null) {
            stmt.bindString(5, bull_tag_number);
        }
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(6, datetime);
        }
        String calves_number = entity.getCalves_number();
        if (calves_number != null) {
            stmt.bindString(7, calves_number);
        }
        String calves = entity.getCalves();
        if (calves != null) {
            stmt.bindString(8, calves);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public CalvingDb readEntity(Cursor cursor, int offset) {
        String str = null;
        Long valueOf = cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
        String string = cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
        String string2 = cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2);
        String string3 = cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3);
        String string4 = cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4);
        String string5 = cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5);
        String string6 = cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6);
        if (!cursor.isNull(offset + 7)) {
            str = cursor.getString(offset + 7);
        }
        return new CalvingDb(valueOf, string, string2, string3, string4, string5, string6, str);
    }

    public void readEntity(Cursor cursor, CalvingDb entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setCow_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCow_tag_number(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBull_id(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBull_tag_number(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDatetime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCalves_number(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        if (!cursor.isNull(offset + 7)) {
            str = cursor.getString(offset + 7);
        }
        entity.setCalves(str);
    }

    protected final Long updateKeyAfterInsert(CalvingDb entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(CalvingDb entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(CalvingDb entity) {
        return entity.getId() != null;
    }

    protected final boolean isEntityUpdateable() {
        return true;
    }
}
