package tr.com.turksat.carpark.service;

import tr.com.turksat.carpark.model.dto.PaymentDto;
import tr.com.turksat.carpark.model.dto.UnitPriceDto;

import java.math.BigDecimal;

public interface PaymentService {


    BigDecimal calculatePayment(String plate);

    PaymentDto getPayment(String plate);

    boolean doPay(String plate);

    UnitPriceDto getUnitPrice();

    boolean updateUnitPrice(BigDecimal unitPrice);

}
