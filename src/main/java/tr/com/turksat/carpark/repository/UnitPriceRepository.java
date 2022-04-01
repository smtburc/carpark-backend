package tr.com.turksat.carpark.repository;

import tr.com.turksat.carpark.model.UnitPrice;

import java.math.BigDecimal;

public interface UnitPriceRepository {

    UnitPrice getUnitPrice();

    boolean updatePrice(BigDecimal price);

}
