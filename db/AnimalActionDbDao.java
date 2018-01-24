package com.moocall.moocall.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class AnimalActionDbDao extends AbstractDao<AnimalActionDb, Long> {
    public static final String TABLENAME = "ANIMAL_ACTION_DB";

    public static class Properties {
        public static final Property Action = new Property(1, Integer.TYPE, "action", false, "ACTION");
        public static final Property Animal_id = new Property(2, String.class, "animal_id", false, "ANIMAL_ID");
        public static final Property Animal_tag_number = new Property(3, String.class, "animal_tag_number", false, "ANIMAL_TAG_NUMBER");
        public static final Property Animal_type = new Property(4, String.class, "animal_type", false, "ANIMAL_TYPE");
        public static final Property Bull_id = new Property(10, String.class, "bull_id", false, "BULL_ID");
        public static final Property Bull_tag_number = new Property(9, String.class, "bull_tag_number", false, "BULL_TAG_NUMBER");
        public static final Property Cow_id = new Property(18, String.class, "cow_id", false, "COW_ID");
        public static final Property Cow_tag_number = new Property(17, String.class, "cow_tag_number", false, "COW_TAG_NUMBER");
        public static final Property Datetime = new Property(7, String.class, "datetime", false, "DATETIME");
        public static final Property Dead_weight = new Property(14, String.class, "dead_weight", false, "DEAD_WEIGHT");
        public static final Property Description = new Property(8, String.class, "description", false, "DESCRIPTION");
        public static final Property Fat_score = new Property(12, String.class, "fat_score", false, "FAT_SCORE");
        public static final Property Grade = new Property(13, String.class, "grade", false, "GRADE");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Moocall_tag_number = new Property(19, String.class, "moocall_tag_number", false, "MOOCALL_TAG_NUMBER");
        public static final Property Price = new Property(15, String.class, Param.PRICE, false, "PRICE");
        public static final Property Sensor = new Property(5, String.class, "sensor", false, "SENSOR");
        public static final Property Text = new Property(16, String.class, "text", false, "TEXT");
        public static final Property Weaned = new Property(11, String.class, "weaned", false, "WEANED");
        public static final Property Weight = new Property(6, String.class, "weight", false, "WEIGHT");
    }

    public AnimalActionDbDao(DaoConfig config) {
        super(config);
    }

    public AnimalActionDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        db.execSQL("CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + "\"ANIMAL_ACTION_DB\" (" + "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + "\"ACTION\" INTEGER NOT NULL ," + "\"ANIMAL_ID\" TEXT NOT NULL ," + "\"ANIMAL_TAG_NUMBER\" TEXT NOT NULL ," + "\"ANIMAL_TYPE\" TEXT NOT NULL ," + "\"SENSOR\" TEXT," + "\"WEIGHT\" TEXT," + "\"DATETIME\" TEXT," + "\"DESCRIPTION\" TEXT," + "\"BULL_TAG_NUMBER\" TEXT," + "\"BULL_ID\" TEXT," + "\"WEANED\" TEXT," + "\"FAT_SCORE\" TEXT," + "\"GRADE\" TEXT," + "\"DEAD_WEIGHT\" TEXT," + "\"PRICE\" TEXT," + "\"TEXT\" TEXT," + "\"COW_TAG_NUMBER\" TEXT," + "\"COW_ID\" TEXT," + "\"MOOCALL_TAG_NUMBER\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ANIMAL_ACTION_DB\"");
    }

    protected final void bindValues(DatabaseStatement stmt, AnimalActionDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, (long) entity.getAction().intValue());
        stmt.bindString(3, entity.getAnimal_id());
        stmt.bindString(4, entity.getAnimal_tag_number());
        stmt.bindString(5, entity.getAnimal_type());
        String sensor = entity.getSensor();
        if (sensor != null) {
            stmt.bindString(6, sensor);
        }
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(7, weight);
        }
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(8, datetime);
        }
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(9, description);
        }
        String bull_tag_number = entity.getBull_tag_number();
        if (bull_tag_number != null) {
            stmt.bindString(10, bull_tag_number);
        }
        String bull_id = entity.getBull_id();
        if (bull_id != null) {
            stmt.bindString(11, bull_id);
        }
        String weaned = entity.getWeaned();
        if (weaned != null) {
            stmt.bindString(12, weaned);
        }
        String fat_score = entity.getFat_score();
        if (fat_score != null) {
            stmt.bindString(13, fat_score);
        }
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(14, grade);
        }
        String dead_weight = entity.getDead_weight();
        if (dead_weight != null) {
            stmt.bindString(15, dead_weight);
        }
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(16, price);
        }
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(17, text);
        }
        String cow_tag_number = entity.getCow_tag_number();
        if (cow_tag_number != null) {
            stmt.bindString(18, cow_tag_number);
        }
        String cow_id = entity.getCow_id();
        if (cow_id != null) {
            stmt.bindString(19, cow_id);
        }
        String moocall_tag_number = entity.getMoocall_tag_number();
        if (moocall_tag_number != null) {
            stmt.bindString(20, moocall_tag_number);
        }
    }

    protected final void bindValues(SQLiteStatement stmt, AnimalActionDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, (long) entity.getAction().intValue());
        stmt.bindString(3, entity.getAnimal_id());
        stmt.bindString(4, entity.getAnimal_tag_number());
        stmt.bindString(5, entity.getAnimal_type());
        String sensor = entity.getSensor();
        if (sensor != null) {
            stmt.bindString(6, sensor);
        }
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(7, weight);
        }
        String datetime = entity.getDatetime();
        if (datetime != null) {
            stmt.bindString(8, datetime);
        }
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(9, description);
        }
        String bull_tag_number = entity.getBull_tag_number();
        if (bull_tag_number != null) {
            stmt.bindString(10, bull_tag_number);
        }
        String bull_id = entity.getBull_id();
        if (bull_id != null) {
            stmt.bindString(11, bull_id);
        }
        String weaned = entity.getWeaned();
        if (weaned != null) {
            stmt.bindString(12, weaned);
        }
        String fat_score = entity.getFat_score();
        if (fat_score != null) {
            stmt.bindString(13, fat_score);
        }
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(14, grade);
        }
        String dead_weight = entity.getDead_weight();
        if (dead_weight != null) {
            stmt.bindString(15, dead_weight);
        }
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(16, price);
        }
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(17, text);
        }
        String cow_tag_number = entity.getCow_tag_number();
        if (cow_tag_number != null) {
            stmt.bindString(18, cow_tag_number);
        }
        String cow_id = entity.getCow_id();
        if (cow_id != null) {
            stmt.bindString(19, cow_id);
        }
        String moocall_tag_number = entity.getMoocall_tag_number();
        if (moocall_tag_number != null) {
            stmt.bindString(20, moocall_tag_number);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public AnimalActionDb readEntity(Cursor cursor, int offset) {
        return new AnimalActionDb(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), Integer.valueOf(cursor.getInt(offset + 1)), cursor.getString(offset + 2), cursor.getString(offset + 3), cursor.getString(offset + 4), cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
    }

    public void readEntity(Cursor cursor, AnimalActionDb entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setAction(Integer.valueOf(cursor.getInt(offset + 1)));
        entity.setAnimal_id(cursor.getString(offset + 2));
        entity.setAnimal_tag_number(cursor.getString(offset + 3));
        entity.setAnimal_type(cursor.getString(offset + 4));
        entity.setSensor(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setWeight(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDatetime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDescription(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setBull_tag_number(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setBull_id(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setWeaned(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFat_score(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setGrade(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setDead_weight(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPrice(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setText(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setCow_tag_number(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setCow_id(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        if (!cursor.isNull(offset + 19)) {
            str = cursor.getString(offset + 19);
        }
        entity.setMoocall_tag_number(str);
    }

    protected final Long updateKeyAfterInsert(AnimalActionDb entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(AnimalActionDb entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(AnimalActionDb entity) {
        return entity.getId() != null;
    }

    protected final boolean isEntityUpdateable() {
        return true;
    }
}
