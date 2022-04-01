package tr.com.turksat.carpark.repository.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.repository.VehicleParkRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TreeSet;

@Repository
@ApplicationScope
public class VehicleParkRepositoryImpl implements VehicleParkRepository {

    TreeSet<VehiclePark> vehicles;
    Long sequence;

    public VehicleParkRepositoryImpl(){
        init();
    }

    @SneakyThrows
    private void init() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy");
        vehicles = new TreeSet<>();
        vehicles.add(new VehiclePark(1L,"06ABC06", VehicleType.MOTORSIKLET, BigDecimal.valueOf(0.0),BigDecimal.valueOf(1.0),simpleDateFormat.parse("20.03.2022")));
        vehicles.add(new VehiclePark(2L,"06ABC07", VehicleType.SEDAN,BigDecimal.valueOf(1.0),BigDecimal.valueOf(2.0),simpleDateFormat.parse("21.03.2022")));
        vehicles.add(new VehiclePark(3L,"06ABC08", VehicleType.HATCBACK,BigDecimal.valueOf(2.0),BigDecimal.valueOf(3.0),simpleDateFormat.parse("22.03.2022")));
        vehicles.add(new VehiclePark(4L,"06ABC09", VehicleType.JEEP,BigDecimal.valueOf(3.0),BigDecimal.valueOf(4.2),simpleDateFormat.parse("23.03.2022")));
        vehicles.add(new VehiclePark(5L,"06ABC10", VehicleType.SUV,BigDecimal.valueOf(4.2),BigDecimal.valueOf(5.4),simpleDateFormat.parse("24.03.2022")));
        vehicles.add(new VehiclePark(6L,"06ABC11", VehicleType.MINIBUS,BigDecimal.valueOf(5.4),BigDecimal.valueOf(7.4),simpleDateFormat.parse("25.03.2022")));
        vehicles.add(new VehiclePark(7L,"06ABC12", VehicleType.KAMYONET,BigDecimal.valueOf(7.4),BigDecimal.valueOf(9.4),simpleDateFormat.parse("19.03.2022")));
        sequence = vehicles.stream().mapToLong(x->x.getId()).max().getAsLong();
    }

    @Override
    public TreeSet<VehiclePark> findAll() {
        return vehicles;
    }

    @Override
    public VehiclePark getByPlate(String plate) {
        Optional<VehiclePark> vehicle =vehicles.stream().filter(x->x.getPlate().equalsIgnoreCase(plate)).findFirst();
        if(vehicle.isPresent()){
            return vehicle.get();
        }
        return null;
    }

    @Override
    public VehiclePark save(VehiclePark vehicle) {
        sequence++;
        vehicle.setId(sequence);
        vehicles.add(vehicle);
        return vehicle;
    }

    @Override
    public boolean deleteWithPlate(String plate) {
        Optional<VehiclePark> vehicle = vehicles.stream().filter(x->x.getPlate().equalsIgnoreCase(plate)).findFirst();
        if(vehicle.isPresent()){
            vehicles.remove(vehicle.get());
            return true;
        }
        return false;
    }
}
