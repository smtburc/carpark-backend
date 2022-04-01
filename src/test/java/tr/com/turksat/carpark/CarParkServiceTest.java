package tr.com.turksat.carpark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.repository.VehicleParkRepository;
import tr.com.turksat.carpark.service.ParkingService;
import tr.com.turksat.carpark.service.PaymentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CarParkServiceTest {

    @Autowired
    ParkingService carParkService;

    @Autowired
    VehicleParkRepository carParkRepository;

    @Autowired
    PaymentService paymentService;

    @Test
    void vehicleIn() {

        carParkRepository.deleteWithPlate("06ABC06");
        carParkRepository.deleteWithPlate("06ABC08");

        carParkService.vehicleIn("08ABC88", 5);

        VehiclePark vehicle = carParkRepository.getByPlate("08ABC88");

        Assert.notNull(vehicle, "Eklenen veri bulunamadı");

    }


    @Test
    void vehicleOut() {

        carParkService.vehicleIn("08ABC89", 5);

        carParkService.vehicleOut("08ABC89");

        VehiclePark vehicle = carParkRepository.getByPlate("08ABC89");

        Assert.isNull(vehicle, "Çıkış yapılan veriye hala erişibilebilir");

    }


    @Test
    void calculatePayment() {
        System.out.println(paymentService.calculatePayment("06ABC11"));
    }

    @Test
    void test() {
        List<BigDecimal> sizes=List.of(BigDecimal.ONE,BigDecimal.valueOf(1.2),BigDecimal.valueOf(2));
        System.out.println(isOptimum(sizes,BigDecimal.valueOf(4.8).subtract(BigDecimal.valueOf(1.2))));
        System.out.println(calculateLost(sizes,BigDecimal.valueOf(2.8).subtract(BigDecimal.valueOf(1))));
    }

    private boolean isOptimum(List<BigDecimal> sizes, BigDecimal size) {
        if (size.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        } else if (size.compareTo(BigDecimal.ZERO) == -1) {
            return false;
        }
        for (BigDecimal s : sizes) {
            BigDecimal newSize = size.subtract(s);
            if(isOptimum(sizes, newSize)){
                return true;
            }
        }
        return false;
    }

    private BigDecimal calculateLost(List<BigDecimal> sizes, BigDecimal size) {
        if (size.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else if (size.compareTo(BigDecimal.ZERO) == -1) {
            return BigDecimal.TEN;
        }
        BigDecimal min=size;
        for (BigDecimal s : sizes) {
            BigDecimal newSize = size.subtract(s);
            BigDecimal kalan = calculateLost(sizes, newSize);
            if(kalan.compareTo(BigDecimal.ZERO)==0){
                return BigDecimal.ZERO;
            }else{
                if(kalan.compareTo(min)==-1){
                    min=kalan;
                }
            }
        }
        return min;
    }

}
