package tr.com.turksat.carpark;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.repository.VehicleParkRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TreeSet;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehicleParkRepositoryTest {

    @AfterEach
    public void endTest(){
        log.info("Test Ok");
    }

    @Autowired
    VehicleParkRepository vehicleParkRepository;

    @Test
    @Order(1)
    void findAll(){
        TreeSet list=vehicleParkRepository.findAll();

        Assert.notNull(list,"List Null Geldi");
        Assert.notEmpty(list,"List Boş Geldi");

        System.out.println(list);
    }


    @Test
    @Order(2)
    void save(){
        VehiclePark obj=new VehiclePark();
        obj.setFirstPoint(BigDecimal.valueOf(19.0));
        obj.setEndPoint(BigDecimal.valueOf(20.0));
        obj.setType(VehicleType.SEDAN);
        obj.setPlate("07ABC07");
        obj.setEntryDate(new Date());

        vehicleParkRepository.save(obj);

        VehiclePark vehicle = vehicleParkRepository.getByPlate("07ABC07");

        Assert.notNull(vehicle,"Vehicle Null Geldi");
        Assert.notNull(vehicle.getId(),"Vehicle ID Null Geldi");

        System.out.println(vehicle);
    }

    @Test
    @Order(3)
    void getByPlate(){

        VehiclePark obj=vehicleParkRepository.getByPlate("06ABC07");

        Assert.notNull(obj,"Obje Null Geldi");

        System.out.println(obj);
    }


    @Test
    @Order(4)
    void delete(){

        vehicleParkRepository.deleteWithPlate("06ABC06");

        VehiclePark vehicle = vehicleParkRepository.getByPlate("06ABC06");

        Assert.isNull(vehicle,"Kayıt Silinemedi");

    }

}
