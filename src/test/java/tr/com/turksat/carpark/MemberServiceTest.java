package tr.com.turksat.carpark;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.model.dto.MemberDto;
import tr.com.turksat.carpark.repository.VehicleParkRepository;
import tr.com.turksat.carpark.service.MemberService;
import tr.com.turksat.carpark.service.ParkingService;

import java.util.List;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberServiceTest {

    @AfterEach
    public void endTest(){
        log.info("Test Ok");
    }

    @Autowired
    MemberService memberService;


    @Test
    @Order(1)
    void member() {

        List<MemberDto> list = memberService.listMember();

        Assert.notNull(list,"Üye Listesi Null Geldi");

        if(memberService.saveMember("38AAA38")){
            Assert.isTrue(memberService.isMember("38AAA38") , "Üyelik Kaydı oluşmasına rağmen sorgulamada hatalı geliyor");
        }

        if(memberService.deleteMember("38AAA38")){
            Assert.isTrue(!memberService.isMember("38AAA38") , "Üyelik Kaydı silinmesine rağmen sorgulamada hatalı geliyor");
        }


    }

}
