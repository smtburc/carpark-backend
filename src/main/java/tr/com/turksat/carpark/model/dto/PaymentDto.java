package tr.com.turksat.carpark.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {

    String plate;
    BigDecimal cost;


}
