package tr.com.turksat.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.turksat.carpark.model.VehicleClass;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.dto.PaymentDto;
import tr.com.turksat.carpark.model.dto.UnitPriceDto;
import tr.com.turksat.carpark.repository.UnitPriceRepository;
import tr.com.turksat.carpark.repository.VehicleParkRepository;
import tr.com.turksat.carpark.service.MemberService;
import tr.com.turksat.carpark.service.PaymentService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final VehicleParkRepository carParkRepository;

    private final MemberService memberService;

    private final UnitPriceRepository unitPriceRepository;

    @Override
    public BigDecimal calculatePayment(String plate) {
        if (carParkRepository.getByPlate(plate) == null) {
            throw new RuntimeException("Araç içeride değil");
        }
        VehiclePark vehiclePark = carParkRepository.getByPlate(plate);
        //Birim Fiyat
        BigDecimal cost = unitPriceRepository.getUnitPrice().getPrice();
        //Gün Farkı
        long days = TimeUnit.MILLISECONDS.toDays(new Date().getTime() - vehiclePark.getEntryDate().getTime()) + 1;
        // Günlük ücreti giriş çıkış gün farkı ile çarp
        cost = cost.multiply(BigDecimal.valueOf(days));
        //5 günden fazla kalmış ise bir alt kademe kaysayı ile çarp
        //5 gün veya daha az ise kendi katsayısı ile çarp
        if (days > 5) {
            switch (vehiclePark.getType().getVehicleClass()) {
                case CLASS_C -> cost = cost.multiply(VehicleClass.CLASS_B.getMultiple());
                case CLASS_B -> cost = cost.multiply(VehicleClass.CLASS_A.getMultiple());
                case CLASS_A -> cost = cost.multiply(VehicleClass.CLASS_A.getMultiple());
            }
        } else {
            cost = cost.multiply(vehiclePark.getType().getVehicleClass().getMultiple());
        }
        //Üye ise %20 indirim
        if (memberService.isMember(plate)) {
            cost = cost.multiply(BigDecimal.valueOf(0.8));
        }
        return cost;
    }

    @Override
    public PaymentDto getPayment(String plate) {
        plate = plate.strip();
        PaymentDto result = new PaymentDto();
        result.setPlate(plate);
        result.setCost(calculatePayment(plate));
        return result;
    }

    @Override
    public boolean doPay(String plate) {
        //TODO Bu Kısımda Ödeme işlemleri yapılacaktır...
        return true;
    }

    @Override
    public UnitPriceDto getUnitPrice() {
        return new UnitPriceDto(unitPriceRepository.getUnitPrice().getPrice());
    }

    @Override
    public boolean updateUnitPrice(BigDecimal unitPrice) {
        return unitPriceRepository.updatePrice(unitPrice);
    }
}
