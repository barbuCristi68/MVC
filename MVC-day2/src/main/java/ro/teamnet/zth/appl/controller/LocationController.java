package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.Z2HController;
import ro.teamnet.zth.api.annotations.Z2HRequestMethod;
import ro.teamnet.zth.api.annotations.Z2HRequestObject;
import ro.teamnet.zth.api.annotations.Z2HRequestParam;
import ro.teamnet.zth.appl.domain.Location;
import ro.teamnet.zth.appl.service.LocationService;
import ro.teamnet.zth.appl.service.LocationServiceImpl;
import ro.teamnet.zth.fmk.domain.HttpMethod;

import java.util.List;

@Z2HController(urlPath = "/locations")
public class LocationController {
    private LocationService locationService;

    public LocationController(){
        locationService = new LocationServiceImpl();
    }

    @Z2HRequestMethod(urlPath = "/all")
    public List<Location> getAll() {
        return locationService.findAll();
    }

    @Z2HRequestMethod(urlPath = "/one")
    public Location getOne(@Z2HRequestParam(name = "locationId") Long locationId) {
        return locationService.findOne(locationId);
    }

    @Z2HRequestMethod(urlPath = "/one", methodType = HttpMethod.DELETE)
    public Boolean deleteOneEmployee(@Z2HRequestParam(name = "locationId") Long locationId) {
        return locationService.delete(locationId);
    }

    @Z2HRequestMethod(urlPath = "/create", methodType = HttpMethod.POST)
    public Location saveEmployee(@Z2HRequestObject Location location) {
        return locationService.save(location);
    }

    @Z2HRequestMethod(urlPath = "/edit", methodType = HttpMethod.PUT)
    public Location updateEmployee(@Z2HRequestObject Location location) {
        return locationService.update(location);
    }
}
