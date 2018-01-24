package com.moocall.moocall.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class AnimalDbDao extends AbstractDao<AnimalDb, Long> {
    public static final String TABLENAME = "ANIMAL_DB";

    public static class Properties {
        public static final Property Animal_id = new Property(1, Integer.class, "animal_id", false, "ANIMAL_ID");
        public static final Property Birth_date = new Property(6, String.class, "birth_date", false, "BIRTH_DATE");
        public static final Property Breed = new Property(14, String.class, "breed", false, "BREED");
        public static final Property Bull_sensor = new Property(30, String.class, "bull_sensor", false, "BULL_SENSOR");
        public static final Property Cycle_date = new Property(9, String.class, "cycle_date", false, "CYCLE_DATE");
        public static final Property Date_slaughtered = new Property(25, String.class, "date_slaughtered", false, "DATE_SLAUGHTERED");
        public static final Property Date_sold = new Property(24, String.class, "date_sold", false, "DATE_SOLD");
        public static final Property Deleted_animal = new Property(29, Integer.class, "deleted_animal", false, "DELETED_ANIMAL");
        public static final Property Died = new Property(23, Integer.class, "died", false, "DIED");
        public static final Property Due_date = new Property(11, String.class, "due_date", false, "DUE_DATE");
        public static final Property Gestation = new Property(17, Integer.class, "gestation", false, "GESTATION");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property In_heat_date = new Property(10, String.class, "in_heat_date", false, "IN_HEAT_DATE");
        public static final Property Insemenation_bull = new Property(26, String.class, "insemenation_bull", false, "INSEMENATION_BULL");
        public static final Property Insemination_date = new Property(8, String.class, "insemination_date", false, "INSEMINATION_DATE");
        public static final Property Last_calving_date = new Property(12, String.class, "last_calving_date", false, "LAST_CALVING_DATE");
        public static final Property Last_state = new Property(18, Integer.class, "last_state", false, "LAST_STATE");
        public static final Property Moocall_tag_number = new Property(19, String.class, "moocall_tag_number", false, "MOOCALL_TAG_NUMBER");
        public static final Property Name = new Property(3, String.class, "name", false, "NAME");
        public static final Property New_animal = new Property(27, Integer.class, "new_animal", false, "NEW_ANIMAL");
        public static final Property Sensor = new Property(13, String.class, "sensor", false, "SENSOR");
        public static final Property Server_state_cet_time = new Property(32, String.class, "server_state_cet_time", false, "SERVER_STATE_CET_TIME");
        public static final Property Slaughtered = new Property(22, Integer.class, "slaughtered", false, "SLAUGHTERED");
        public static final Property Sold = new Property(21, Integer.class, "sold", false, "SOLD");
        public static final Property State = new Property(31, Integer.class, "state", false, "STATE");
        public static final Property Status = new Property(20, Integer.class, "status", false, "STATUS");
        public static final Property Tag_number = new Property(2, String.class, "tag_number", false, "TAG_NUMBER");
        public static final Property Temperament = new Property(15, String.class, "temperament", false, "TEMPERAMENT");
        public static final Property Time_update = new Property(5, String.class, "time_update", false, "TIME_UPDATE");
        public static final Property Type = new Property(4, Integer.class, "type", false, "TYPE");
        public static final Property Updated_animal = new Property(28, Integer.class, "updated_animal", false, "UPDATED_ANIMAL");
        public static final Property Vendor = new Property(16, String.class, "vendor", false, "VENDOR");
        public static final Property Weight = new Property(7, String.class, "weight", false, "WEIGHT");
    }

    public AnimalDbDao(DaoConfig config) {
        super(config);
    }

    public AnimalDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"ANIMAL_DB\" (" + "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + "\"ANIMAL_ID\" INTEGER," + "\"TAG_NUMBER\" TEXT," + "\"NAME\" TEXT," + "\"TYPE\" INTEGER," + "\"TIME_UPDATE\" TEXT," + "\"BIRTH_DATE\" TEXT," + "\"WEIGHT\" TEXT," + "\"INSEMINATION_DATE\" TEXT," + "\"CYCLE_DATE\" TEXT," + "\"IN_HEAT_DATE\" TEXT," + "\"DUE_DATE\" TEXT," + "\"LAST_CALVING_DATE\" TEXT," + "\"SENSOR\" TEXT," + "\"BREED\" TEXT," + "\"TEMPERAMENT\" TEXT," + "\"VENDOR\" TEXT," + "\"GESTATION\" INTEGER," + "\"LAST_STATE\" INTEGER," + "\"MOOCALL_TAG_NUMBER\" TEXT," + "\"STATUS\" INTEGER," + "\"SOLD\" INTEGER," + "\"SLAUGHTERED\" INTEGER," + "\"DIED\" INTEGER," + "\"DATE_SOLD\" TEXT," + "\"DATE_SLAUGHTERED\" TEXT," + "\"INSEMENATION_BULL\" TEXT," + "\"NEW_ANIMAL\" INTEGER," + "\"UPDATED_ANIMAL\" INTEGER," + "\"DELETED_ANIMAL\" INTEGER," + "\"BULL_SENSOR\" TEXT," + "\"STATE\" INTEGER," + "\"SERVER_STATE_CET_TIME\" TEXT);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_ANIMAL_DB_ANIMAL_ID ON ANIMAL_DB" + " (\"ANIMAL_ID\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_ANIMAL_DB_TAG_NUMBER ON ANIMAL_DB" + " (\"TAG_NUMBER\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_ANIMAL_DB_NAME ON ANIMAL_DB" + " (\"NAME\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_ANIMAL_DB_TYPE ON ANIMAL_DB" + " (\"TYPE\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_ANIMAL_DB_TIME_UPDATE ON ANIMAL_DB" + " (\"TIME_UPDATE\");");
    }

    public static void dropTable(Database db, boolean ifExists) {
        db.execSQL("DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ANIMAL_DB\"");
    }

    protected final void bindValues(DatabaseStatement stmt, AnimalDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        Integer animal_id = entity.getAnimal_id();
        if (animal_id != null) {
            stmt.bindLong(2, (long) animal_id.intValue());
        }
        String tag_number = entity.getTag_number();
        if (tag_number != null) {
            stmt.bindString(3, tag_number);
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(5, (long) type.intValue());
        }
        String time_update = entity.getTime_update();
        if (time_update != null) {
            stmt.bindString(6, time_update);
        }
        String birth_date = entity.getBirth_date();
        if (birth_date != null) {
            stmt.bindString(7, birth_date);
        }
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(8, weight);
        }
        String insemination_date = entity.getInsemination_date();
        if (insemination_date != null) {
            stmt.bindString(9, insemination_date);
        }
        String cycle_date = entity.getCycle_date();
        if (cycle_date != null) {
            stmt.bindString(10, cycle_date);
        }
        String in_heat_date = entity.getIn_heat_date();
        if (in_heat_date != null) {
            stmt.bindString(11, in_heat_date);
        }
        String due_date = entity.getDue_date();
        if (due_date != null) {
            stmt.bindString(12, due_date);
        }
        String last_calving_date = entity.getLast_calving_date();
        if (last_calving_date != null) {
            stmt.bindString(13, last_calving_date);
        }
        String sensor = entity.getSensor();
        if (sensor != null) {
            stmt.bindString(14, sensor);
        }
        String breed = entity.getBreed();
        if (breed != null) {
            stmt.bindString(15, breed);
        }
        String temperament = entity.getTemperament();
        if (temperament != null) {
            stmt.bindString(16, temperament);
        }
        String vendor = entity.getVendor();
        if (vendor != null) {
            stmt.bindString(17, vendor);
        }
        Integer gestation = entity.getGestation();
        if (gestation != null) {
            stmt.bindLong(18, (long) gestation.intValue());
        }
        Integer last_state = entity.getLast_state();
        if (last_state != null) {
            stmt.bindLong(19, (long) last_state.intValue());
        }
        String moocall_tag_number = entity.getMoocall_tag_number();
        if (moocall_tag_number != null) {
            stmt.bindString(20, moocall_tag_number);
        }
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(21, (long) status.intValue());
        }
        Integer sold = entity.getSold();
        if (sold != null) {
            stmt.bindLong(22, (long) sold.intValue());
        }
        Integer slaughtered = entity.getSlaughtered();
        if (slaughtered != null) {
            stmt.bindLong(23, (long) slaughtered.intValue());
        }
        Integer died = entity.getDied();
        if (died != null) {
            stmt.bindLong(24, (long) died.intValue());
        }
        String date_sold = entity.getDate_sold();
        if (date_sold != null) {
            stmt.bindString(25, date_sold);
        }
        String date_slaughtered = entity.getDate_slaughtered();
        if (date_slaughtered != null) {
            stmt.bindString(26, date_slaughtered);
        }
        String insemenation_bull = entity.getInsemenation_bull();
        if (insemenation_bull != null) {
            stmt.bindString(27, insemenation_bull);
        }
        Integer new_animal = entity.getNew_animal();
        if (new_animal != null) {
            stmt.bindLong(28, (long) new_animal.intValue());
        }
        Integer updated_animal = entity.getUpdated_animal();
        if (updated_animal != null) {
            stmt.bindLong(29, (long) updated_animal.intValue());
        }
        Integer deleted_animal = entity.getDeleted_animal();
        if (deleted_animal != null) {
            stmt.bindLong(30, (long) deleted_animal.intValue());
        }
        String bull_sensor = entity.getBull_sensor();
        if (bull_sensor != null) {
            stmt.bindString(31, bull_sensor);
        }
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(32, (long) state.intValue());
        }
        String server_state_cet_time = entity.getServer_state_cet_time();
        if (server_state_cet_time != null) {
            stmt.bindString(33, server_state_cet_time);
        }
    }

    protected final void bindValues(SQLiteStatement stmt, AnimalDb entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        Integer animal_id = entity.getAnimal_id();
        if (animal_id != null) {
            stmt.bindLong(2, (long) animal_id.intValue());
        }
        String tag_number = entity.getTag_number();
        if (tag_number != null) {
            stmt.bindString(3, tag_number);
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(5, (long) type.intValue());
        }
        String time_update = entity.getTime_update();
        if (time_update != null) {
            stmt.bindString(6, time_update);
        }
        String birth_date = entity.getBirth_date();
        if (birth_date != null) {
            stmt.bindString(7, birth_date);
        }
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(8, weight);
        }
        String insemination_date = entity.getInsemination_date();
        if (insemination_date != null) {
            stmt.bindString(9, insemination_date);
        }
        String cycle_date = entity.getCycle_date();
        if (cycle_date != null) {
            stmt.bindString(10, cycle_date);
        }
        String in_heat_date = entity.getIn_heat_date();
        if (in_heat_date != null) {
            stmt.bindString(11, in_heat_date);
        }
        String due_date = entity.getDue_date();
        if (due_date != null) {
            stmt.bindString(12, due_date);
        }
        String last_calving_date = entity.getLast_calving_date();
        if (last_calving_date != null) {
            stmt.bindString(13, last_calving_date);
        }
        String sensor = entity.getSensor();
        if (sensor != null) {
            stmt.bindString(14, sensor);
        }
        String breed = entity.getBreed();
        if (breed != null) {
            stmt.bindString(15, breed);
        }
        String temperament = entity.getTemperament();
        if (temperament != null) {
            stmt.bindString(16, temperament);
        }
        String vendor = entity.getVendor();
        if (vendor != null) {
            stmt.bindString(17, vendor);
        }
        Integer gestation = entity.getGestation();
        if (gestation != null) {
            stmt.bindLong(18, (long) gestation.intValue());
        }
        Integer last_state = entity.getLast_state();
        if (last_state != null) {
            stmt.bindLong(19, (long) last_state.intValue());
        }
        String moocall_tag_number = entity.getMoocall_tag_number();
        if (moocall_tag_number != null) {
            stmt.bindString(20, moocall_tag_number);
        }
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(21, (long) status.intValue());
        }
        Integer sold = entity.getSold();
        if (sold != null) {
            stmt.bindLong(22, (long) sold.intValue());
        }
        Integer slaughtered = entity.getSlaughtered();
        if (slaughtered != null) {
            stmt.bindLong(23, (long) slaughtered.intValue());
        }
        Integer died = entity.getDied();
        if (died != null) {
            stmt.bindLong(24, (long) died.intValue());
        }
        String date_sold = entity.getDate_sold();
        if (date_sold != null) {
            stmt.bindString(25, date_sold);
        }
        String date_slaughtered = entity.getDate_slaughtered();
        if (date_slaughtered != null) {
            stmt.bindString(26, date_slaughtered);
        }
        String insemenation_bull = entity.getInsemenation_bull();
        if (insemenation_bull != null) {
            stmt.bindString(27, insemenation_bull);
        }
        Integer new_animal = entity.getNew_animal();
        if (new_animal != null) {
            stmt.bindLong(28, (long) new_animal.intValue());
        }
        Integer updated_animal = entity.getUpdated_animal();
        if (updated_animal != null) {
            stmt.bindLong(29, (long) updated_animal.intValue());
        }
        Integer deleted_animal = entity.getDeleted_animal();
        if (deleted_animal != null) {
            stmt.bindLong(30, (long) deleted_animal.intValue());
        }
        String bull_sensor = entity.getBull_sensor();
        if (bull_sensor != null) {
            stmt.bindString(31, bull_sensor);
        }
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(32, (long) state.intValue());
        }
        String server_state_cet_time = entity.getServer_state_cet_time();
        if (server_state_cet_time != null) {
            stmt.bindString(33, server_state_cet_time);
        }
    }

    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0));
    }

    public AnimalDb readEntity(Cursor cursor, int offset) {
        return new AnimalDb(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)), cursor.isNull(offset + 1) ? null : Integer.valueOf(cursor.getInt(offset + 1)), cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), cursor.isNull(offset + 4) ? null : Integer.valueOf(cursor.getInt(offset + 4)), cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), cursor.isNull(offset + 17) ? null : Integer.valueOf(cursor.getInt(offset + 17)), cursor.isNull(offset + 18) ? null : Integer.valueOf(cursor.getInt(offset + 18)), cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), cursor.isNull(offset + 20) ? null : Integer.valueOf(cursor.getInt(offset + 20)), cursor.isNull(offset + 21) ? null : Integer.valueOf(cursor.getInt(offset + 21)), cursor.isNull(offset + 22) ? null : Integer.valueOf(cursor.getInt(offset + 22)), cursor.isNull(offset + 23) ? null : Integer.valueOf(cursor.getInt(offset + 23)), cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), cursor.isNull(offset + 27) ? null : Integer.valueOf(cursor.getInt(offset + 27)), cursor.isNull(offset + 28) ? null : Integer.valueOf(cursor.getInt(offset + 28)), cursor.isNull(offset + 29) ? null : Integer.valueOf(cursor.getInt(offset + 29)), cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), cursor.isNull(offset + 31) ? null : Integer.valueOf(cursor.getInt(offset + 31)), cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
    }

    public void readEntity(Cursor cursor, AnimalDb entity, int offset) {
        String str = null;
        entity.setId(cursor.isNull(offset + 0) ? null : Long.valueOf(cursor.getLong(offset + 0)));
        entity.setAnimal_id(cursor.isNull(offset + 1) ? null : Integer.valueOf(cursor.getInt(offset + 1)));
        entity.setTag_number(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : Integer.valueOf(cursor.getInt(offset + 4)));
        entity.setTime_update(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBirth_date(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWeight(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setInsemination_date(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCycle_date(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIn_heat_date(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setDue_date(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setLast_calving_date(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setSensor(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setBreed(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setTemperament(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setVendor(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setGestation(cursor.isNull(offset + 17) ? null : Integer.valueOf(cursor.getInt(offset + 17)));
        entity.setLast_state(cursor.isNull(offset + 18) ? null : Integer.valueOf(cursor.getInt(offset + 18)));
        entity.setMoocall_tag_number(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setStatus(cursor.isNull(offset + 20) ? null : Integer.valueOf(cursor.getInt(offset + 20)));
        entity.setSold(cursor.isNull(offset + 21) ? null : Integer.valueOf(cursor.getInt(offset + 21)));
        entity.setSlaughtered(cursor.isNull(offset + 22) ? null : Integer.valueOf(cursor.getInt(offset + 22)));
        entity.setDied(cursor.isNull(offset + 23) ? null : Integer.valueOf(cursor.getInt(offset + 23)));
        entity.setDate_sold(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setDate_slaughtered(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setInsemenation_bull(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setNew_animal(cursor.isNull(offset + 27) ? null : Integer.valueOf(cursor.getInt(offset + 27)));
        entity.setUpdated_animal(cursor.isNull(offset + 28) ? null : Integer.valueOf(cursor.getInt(offset + 28)));
        entity.setDeleted_animal(cursor.isNull(offset + 29) ? null : Integer.valueOf(cursor.getInt(offset + 29)));
        entity.setBull_sensor(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setState(cursor.isNull(offset + 31) ? null : Integer.valueOf(cursor.getInt(offset + 31)));
        if (!cursor.isNull(offset + 32)) {
            str = cursor.getString(offset + 32);
        }
        entity.setServer_state_cet_time(str);
    }

    protected final Long updateKeyAfterInsert(AnimalDb entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    public Long getKey(AnimalDb entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    public boolean hasKey(AnimalDb entity) {
        return entity.getId() != null;
    }

    protected final boolean isEntityUpdateable() {
        return true;
    }
}
