package com.moocall.moocall.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class StateHistoryDbDao extends AbstractDao<StateHistoryDb, Long> {
    public static final String TABLENAME = "STATE_HISTORY_DB";

    public static class Properties {
        public static final Property Animal_id = new Property(1, String.class, "animal_id", false, "ANIMAL_ID");
        public static final Property Datetime = new Property(5, String.class, "datetime", false, "DATETIME");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Last_state = new Property(2, Integer.class, "last_state", false, "LAST_STATE");
        public static final Property Last_state_datetime = new Property(3, String.class, "last_state_datetime", false, "LAST_STATE_DATETIME");
        public static final Property Weight = new Property(4, String.class, "weight", false, "WEIGHT");
    }

    public StateHistoryDbDao(DaoConfig config) {
        super(config);
    }

    public StateHistoryDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"STATE_HISTORY_DB\" (" + "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + "\"ANIMAL_ID\" TEXT," + "\"LAST_STATE\" INTEGER," + "\"LAST_STATE_DATETIME\" TEXT," + "\"WEIGHT\" TEXT," + "\"DATETIME\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STATE_HISTORY_DB\"");
    }

    protected final void bindValues(DatabaseStatement stmt, StateHistoryDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String animal_id = entity.getAnimal_id();
        if (animal_id != null) {
            stmt.bindString(2, animal_id);
        }
        Integer last_state = entity.getLast_state();
        if (last_state != null) {
            stmt.bindLong(3, (long) last_state.intValue());
        }
        String last_state_datetime = entity.getLast_state_datetime();
        if (last_state_datetime != null) {
            stmt.bindString(4, last_state_datetime);
        }
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(5, weight);
        }
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(6, datetime);
        }
    }

    protected final void bindValues(SQLiteStatement stmt, StateHistoryDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String animal_id = entity.getAnimal_id();
        if (animal_id != null) {
            stmt.bindString(2, animal_id);
        }
        Integer last_state = entity.getLast_state();
        if (last_state != null) {
            stmt.bindLong(3, (long) last_state.intValue());
        }
        String last_state_datetime = entity.getLast_state_datetime();
        if (last_state_datetime != null) {
            stmt.bindString(4, last_state_datetime);
        }
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(5, weight);
        }
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(6, datetime);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public StateHistoryDb readEntity(Cursor cursor, int offset) {
        String str = null;
        Long valueOf = cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
        String string = cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
        Integer valueOf2 = cursor.isNull(offset + 2) ? null : Integer.valueOf(cursor.getInt(offset + 2));
        String string2 = cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3);
        String string3 = cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4);
        if (!cursor.isNull(offset + 5)) {
            str = cursor.getString(offset + 5);
        }
        return new StateHistoryDb(valueOf, string, valueOf2, string2, string3, str);
    }

    public void readEntity(Cursor cursor, StateHistoryDb entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setAnimal_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLast_state(cursor.isNull(offset + 2) ? null : Integer.valueOf(cursor.getInt(offset + 2)));
        entity.setLast_state_datetime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWeight(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        if (!cursor.isNull(offset + 5)) {
            str = cursor.getString(offset + 5);
        }
        entity.setDatetime(str);
    }

    protected final Long updateKeyAfterInsert(StateHistoryDb entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(StateHistoryDb entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(StateHistoryDb entity) {
        return entity.getId() != null;
    }

    protected final boolean isEntityUpdateable() {
        return true;
    }
}
