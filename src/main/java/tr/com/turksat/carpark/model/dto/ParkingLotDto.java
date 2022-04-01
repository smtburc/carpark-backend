package tr.com.turksat.carpark.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkingLotDto implements Comparable{

    String plate;
    String value;
    String vehicleClass;
    BigDecimal firstPoint;

    @Override
    public int compareTo(Object o) {
        return this.getFirstPoint().compareTo(((ParkingLotDto)o).getFirstPoint());
    }

}
