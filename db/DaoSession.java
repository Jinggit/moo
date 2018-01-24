package com.moocall.moocall.db;

import java.util.Map;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

public class DaoSession extends AbstractDaoSession {
    private final AnimalActionDbDao animalActionDbDao = new AnimalActionDbDao(this.animalActionDbDaoConfig, this);
    private final DaoConfig animalActionDbDaoConfig;
    private final AnimalDbDao animalDbDao = new AnimalDbDao(this.animalDbDaoConfig, this);
    private final DaoConfig animalDbDaoConfig;
    private final CalvingDbDao calvingDbDao = new CalvingDbDao(this.calvingDbDaoConfig, this);
    private final DaoConfig calvingDbDaoConfig;
    private final SensorDbDao sensorDbDao = new SensorDbDao(this.sensorDbDaoConfig, this);
    private final DaoConfig sensorDbDaoConfig;
    private final StateHistoryDbDao stateHistoryDbDao = new StateHistoryDbDao(this.stateHistoryDbDaoConfig, this);
    private final DaoConfig stateHistoryDbDaoConfig;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);
        this.animalDbDaoConfig = ((DaoConfig) daoConfigMap.get(AnimalDbDao.class)).clone();
        this.animalDbDaoConfig.initIdentityScope(type);
        this.animalActionDbDaoConfig = ((DaoConfig) daoConfigMap.get(AnimalActionDbDao.class)).clone();
        this.animalActionDbDaoConfig.initIdentityScope(type);
        this.calvingDbDaoConfig = ((DaoConfig) daoConfigMap.get(CalvingDbDao.class)).clone();
        this.calvingDbDaoConfig.initIdentityScope(type);
        this.sensorDbDaoConfig = ((DaoConfig) daoConfigMap.get(SensorDbDao.class)).clone();
        this.sensorDbDaoConfig.initIdentityScope(type);
        this.stateHistoryDbDaoConfig = ((DaoConfig) daoConfigMap.get(StateHistoryDbDao.class)).clone();
        this.stateHistoryDbDaoConfig.initIdentityScope(type);
        registerDao(AnimalDb.class, this.animalDbDao);
        registerDao(AnimalActionDb.class, this.animalActionDbDao);
        registerDao(CalvingDb.class, this.calvingDbDao);
        registerDao(SensorDb.class, this.sensorDbDao);
        registerDao(StateHistoryDb.class, this.stateHistoryDbDao);
    }

    public void clear() {
        this.animalDbDaoConfig.clearIdentityScope();
        this.animalActionDbDaoConfig.clearIdentityScope();
        this.calvingDbDaoConfig.clearIdentityScope();
        this.sensorDbDaoConfig.clearIdentityScope();
        this.stateHistoryDbDaoConfig.clearIdentityScope();
    }

    public AnimalDbDao getAnimalDbDao() {
        return this.animalDbDao;
    }

    public AnimalActionDbDao getAnimalActionDbDao() {
        return this.animalActionDbDao;
    }

    public CalvingDbDao getCalvingDbDao() {
        return this.calvingDbDao;
    }

    public SensorDbDao getSensorDbDao() {
        return this.sensorDbDao;
    }

    public StateHistoryDbDao getStateHistoryDbDao() {
        return this.stateHistoryDbDao;
    }
}
