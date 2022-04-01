package tr.com.turksat.carpark.repository;

import tr.com.turksat.carpark.model.VehiclePark;

import java.util.TreeSet;

public interface VehicleParkRepository {

    TreeSet<VehiclePark> findAll();

    VehiclePark getByPlate(String plate);

    VehiclePark save(VehiclePark vehicle);

    boolean deleteWithPlate(String plate);

}
