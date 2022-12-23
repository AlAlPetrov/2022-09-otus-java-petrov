package ru.otus.crm.service.withCache;

import ru.otus.cachehw.HwCache;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DBServiceManager;

import java.util.List;
import java.util.Optional;

public class CachedDbServiceManager implements DBServiceManager {
    private final DBServiceManager dbServiceManager;
    private final HwCache<Long, Manager> hwCache;

    public CachedDbServiceManager(DBServiceManager dbServiceManager, HwCache<Long, Manager> hwCache) {
        this.dbServiceManager = dbServiceManager;
        this.hwCache = hwCache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        var savedManager = dbServiceManager.saveManager(manager);
        hwCache.put(savedManager.getNo(), savedManager);

        return savedManager;
    }

    @Override
    public Optional<Manager> getManager(long id) {
        var manager = hwCache.get(id);
        if (manager != null) {
            return Optional.of(manager);
        }

        manager = dbServiceManager.getManager(id).get();
        if (manager == null) {
            return Optional.empty();
        }

        hwCache.put(id, manager);
        return Optional.of(manager);
    }

    @Override
    public List<Manager> findAll() {
        return dbServiceManager.findAll();
    }
}
