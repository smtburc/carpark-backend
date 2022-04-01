package tr.com.turksat.carpark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePark implements Comparable {

    private Long id;
    private String plate;
    private VehicleType type;

    //Otopark da Yerleştirilen Alan
    private BigDecimal firstPoint;
    private BigDecimal endPoint;

    private Date entryDate;

    @Override
    public int compareTo(Object o) {
        return this.getFirstPoint().compareTo(((VehiclePark)o).getFirstPoint());
    }
}
