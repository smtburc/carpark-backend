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
    public void endTest() {
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

    @Test
    @Order(3)
    void testCase1() {

        //tüm parkı boşalt
        parkingService.listParkingLot().forEach(x -> {
            if(x.getPlate()!=null){
                parkingService.vehicleOut(x.getPlate());
            }
        });

        String basePlate = "06BBB";
        for (int i = 0; i < 50; i++) {
            parkingService.vehicleIn(basePlate + i, VehicleType.MOTORSIKLET.getId());
        }

        Exception exception=null;
        try{
            parkingService.vehicleIn(basePlate + 51, VehicleType.MOTORSIKLET.getId());
        }catch (Exception e){
            exception=e;
        }
        Assert.notNull(exception,"51 tane normal araç parkı yapılabildi");

        //tüm parkı boşalt
        parkingService.listParkingLot().forEach(x -> {
            if(x.getPlate()!=null){
                parkingService.vehicleOut(x.getPlate());
            }
        });

        for (int i = 0; i < 41; i++) {
            parkingService.vehicleIn(basePlate + i, VehicleType.JEEP.getId());
        }

        exception=null;
        try{
            parkingService.vehicleIn(basePlate + 42, VehicleType.JEEP.getId());
        }catch (Exception e){
            exception=e;
        }
        Assert.notNull(exception,"42 tane 1.2 lik araç parkı yapılabildi");

        //tüm parkı boşalt
        parkingService.listParkingLot().forEach(x -> {
            if(x.getPlate()!=null){
                parkingService.vehicleOut(x.getPlate());
            }
        });

        for (int i = 0; i < 25; i++) {
            parkingService.vehicleIn(basePlate + i, VehicleType.KAMYONET.getId());
        }

        exception=null;
        try{
            parkingService.vehicleIn(basePlate + 26, VehicleType.KAMYONET.getId());
        }catch (Exception e){
            exception=e;
        }
        Assert.notNull(exception,"26 tane Kamyonet lik araç parkı yapılabildi");

    }

    @Test
    @Order(4)
    void testCase2() {

        //tüm parkı boşalt
        parkingService.listParkingLot().forEach(x -> {
            if(x.getPlate()!=null){
                parkingService.vehicleOut(x.getPlate());
            }
        });

        String basePlate = "06BBB";
        for (int i = 0; i < 49; i++) {
            parkingService.vehicleIn(basePlate + i, VehicleType.MOTORSIKLET.getId());
        }

        Exception exception=null;
        try{
            parkingService.vehicleIn(basePlate + 50, VehicleType.JEEP.getId());
        }catch (Exception e){
            exception=e;
        }
        Assert.notNull(exception,"1 Birimlik Boşluk varken 1.2 Birimlik Araç Eklenebildi");

        parkingService.vehicleOut(basePlate+48);

        Assert.isTrue(parkingService.vehicleIn(basePlate+51,VehicleType.KAMYONET.getId()),"2.2 birimlik boşluk varken 2 birimlik araç eklenemedi");

    }

}

