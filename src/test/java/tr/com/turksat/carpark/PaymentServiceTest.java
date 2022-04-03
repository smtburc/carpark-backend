package tr.com.turksat.carpark;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.model.dto.UnitPriceDto;
import tr.com.turksat.carpark.repository.VehicleParkRepository;
import tr.com.turksat.carpark.service.MemberService;
import tr.com.turksat.carpark.service.ParkingService;
import tr.com.turksat.carpark.service.PaymentService;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentServiceTest {

    @AfterEach
    public void endTest(){
        log.info("Test Ok");
    }

    @Autowired
    PaymentService paymentService;

    @Autowired
    ParkingService parkingService;

    @Autowired
    MemberService memberService;

    @Autowired
    VehicleParkRepository vehicleParkRepository;

    @Test
    @Order(1)
    void unitPrice(){
        BigDecimal unitPrice = paymentService.getUnitPrice().getPrice();

        Assert.notNull(unitPrice,"Birim Fiyat Gelmedi");

        BigDecimal newPrice=BigDecimal.valueOf(new Random(100).nextInt());

        paymentService.updateUnitPrice(newPrice);

        Assert.isTrue(newPrice.compareTo(paymentService.getUnitPrice().getPrice())==0,"Birim Fiyat Güncelleme Hatalı");
    }

    @Test
    @Order(2)
    void calculatePayment() {

        BigDecimal unitPrice = paymentService.getUnitPrice().getPrice();
        parkingService.vehicleIn("11AAA11",VehicleType.MOTORSIKLET.getId());
        parkingService.vehicleIn("11AAA12",VehicleType.JEEP.getId());
        parkingService.vehicleIn("11AAA13",VehicleType.KAMYONET.getId());

        Assert.isTrue(paymentService.calculatePayment("11AAA11").compareTo(unitPrice)==0,"1 Katsayılı Araçlarda Hesaplama Hatalı");
        Assert.isTrue(paymentService.calculatePayment("11AAA12").compareTo(unitPrice.multiply(BigDecimal.valueOf(1.2)))==0,"2 Katsayılı Araçlarda Hesaplama Hatalı");
        Assert.isTrue(paymentService.calculatePayment("11AAA13").compareTo(unitPrice.multiply(BigDecimal.valueOf(2)))==0,"3 Katsayılı Araçlarda Hesaplama Hatalı");

        memberService.saveMember("11AAA11");

        //Üye ise %20 indirim kontrolü
        Assert.isTrue(paymentService.calculatePayment("11AAA11").compareTo(unitPrice.multiply(BigDecimal.valueOf(0.8)))==0,"Üye İndirimi Hesaplama Hatalı");


    }




}
