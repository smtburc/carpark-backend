package tr.com.turksat.carpark;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.repository.VehicleParkRepository;
import tr.com.turksat.carpark.service.ParkingService;
import tr.com.turksat.carpark.service.PaymentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParkingServiceTest {

    @AfterEach
    public void endTest(){
        log.info("Test Ok");
    }

    @Autowired
    ParkingService parkingService;

    @Autowired
    VehicleParkRepository vehicleParkRepository;


    @Test
    @Order(1)
    void vehicleIn() {

        parkingService.vehicleIn("08ABC88", VehicleType.SUV.getId());

        VehiclePark vehicle = vehicleParkRepository.getByPlate("08ABC88");

        Assert.notNull(vehicle, "Eklenen veri bulunamadı");

    }


    @Test
    @Order(2)
    void vehicleOut() {

        parkingService.vehicleIn("08ABC89", VehicleType.SUV.getId());

        parkingService.vehicleOut("08ABC89");

        VehiclePark vehicle = vehicleParkRepository.getByPlate("08ABC89");

        Assert.isNull(vehicle, "Çıkış yapılan veriye hala erişibilebilir");

    }

}
