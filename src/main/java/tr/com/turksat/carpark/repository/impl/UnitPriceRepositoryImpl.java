package tr.com.turksat.carpark.repository.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import tr.com.turksat.carpark.model.UnitPrice;
import tr.com.turksat.carpark.repository.UnitPriceRepository;

import java.math.BigDecimal;

@Repository
@ApplicationScope
public class UnitPriceRepositoryImpl implements UnitPriceRepository {

    UnitPrice unitPrice;

    public UnitPriceRepositoryImpl(){
        init();
    }

    @SneakyThrows
    private void init() {
        unitPrice = new UnitPrice(BigDecimal.valueOf(10));
    }

    @Override
    public UnitPrice getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean updatePrice(BigDecimal price) {
        unitPrice.setPrice(price);
        return true;
    }

}
