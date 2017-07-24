package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.dao.LocationDao;
import ro.teamnet.zth.appl.domain.Location;

import java.util.List;

/**
 * Created by Cristian.Barbu on 7/24/2017.
 */
public class LocationServiceImpl implements LocationService {

    private final LocationDao locationDao = new LocationDao();

    @Override
    public List<Location> findAll() {
        return locationDao.getAllLocations();
    }

    @Override
    public Location findOne(Long locationId) {
        return locationDao.getLocationById(locationId);
    }

    @Override
    public Boolean delete(Long locationId) {
        Location locationToDelete = locationDao.getLocationById(locationId);
        if (locationToDelete == null) {
            return false;
        }
        locationDao.deleteLocation(locationToDelete);
        return true;
    }

    @Override
    public Location save(Location location) {
        return locationDao.insertLocation(location);
    }

    @Override
    public Location update(Location location) {
        return locationDao.updateLocation(location);
    }
}
