package tr.com.turksat.carpark.service;

import tr.com.turksat.carpark.model.dto.ParkingLotDto;
import tr.com.turksat.carpark.model.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.Set;

public interface ParkingService {

    Boolean vehicleIn(String plate, int type);

    Boolean vehicleOut(String plate);

    Set<ParkingLotDto> listParkingLot();


}
