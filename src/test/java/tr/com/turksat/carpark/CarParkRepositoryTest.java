package tr.com.turksat.carpark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.repository.VehicleParkRepository;

import java.math.BigDecimal;
import java.util.TreeSet;

@SpringBootTest
public class CarParkRepositoryTest {

    @Autowired
    VehicleParkRepository carParkRepository;

    @Test
    void findAll(){
        TreeSet list=carParkRepository.findAll();

        Assert.notNull(list,"List Null Geldi");
        Assert.notEmpty(list,"List Boş Geldi");

        System.out.println(list);
    }


    @Test
    void getByPlate(){
        VehiclePark obj=carParkRepository.getByPlate("06ABC07");

        Assert.notNull(obj,"Obje Null Geldi");

        System.out.println(obj);
    }

    @Test
    void save(){
        VehiclePark obj=new VehiclePark();
        obj.setFirstPoint(BigDecimal.valueOf(19.0));
        obj.setEndPoint(BigDecimal.valueOf(20.0));
        obj.setType(VehicleType.SEDAN);
        obj.setPlate("07ABC07");

        carParkRepository.save(obj);

        VehiclePark vehicle = carParkRepository.getByPlate("07ABC07");

        Assert.notNull(vehicle,"Vehicle Null Geldi");
        Assert.notNull(vehicle.getId(),"Vehicle ID Null Geldi");

        System.out.println(vehicle);
    }

    @Test
    void delete(){

        carParkRepository.deleteWithPlate("06ABC06");

        VehiclePark vehicle = carParkRepository.getByPlate("06ABC06");

        Assert.isNull(vehicle,"Kayıt Silinemedi");

    }

}
