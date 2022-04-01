package tr.com.turksat.carpark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
public enum VehicleClass {

    CLASS_A(BigDecimal.ONE),
    CLASS_B(BigDecimal.valueOf(1.2)),
    CLASS_C(BigDecimal.valueOf(2));

    @Getter BigDecimal multiple;

}
