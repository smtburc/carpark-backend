package tr.com.turksat.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.turksat.carpark.model.dto.MemberDto;
import tr.com.turksat.carpark.model.dto.UnitPriceDto;
import tr.com.turksat.carpark.model.dto.VehicleDto;
import tr.com.turksat.carpark.service.MemberService;
import tr.com.turksat.carpark.service.ParkingService;
import tr.com.turksat.carpark.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class CarParkController {

    private final ParkingService parkingService;

    private final MemberService memberService;

    private final PaymentService paymentService;

    @GetMapping(value = "/listVehicle")
    public ResponseEntity findVehicles() {
        return new ResponseEntity<>(parkingService.listParkingLot(), HttpStatus.OK);
    }

    @PostMapping(value = "/vehicleIn")
    public ResponseEntity vehicleIn(@RequestBody VehicleDto vehicleInDto) {
        return new ResponseEntity<>(parkingService.vehicleIn(vehicleInDto.getPlate(),vehicleInDto.getType()), HttpStatus.OK);
    }

    @PostMapping(value = "/vehicleOut")
    public ResponseEntity vehicleOut(@RequestBody VehicleDto vehicleInDto) {
        return new ResponseEntity<>(parkingService.vehicleOut(vehicleInDto.getPlate()), HttpStatus.OK);
    }

    @PostMapping(value = "/getPayment")
    public ResponseEntity getPayment(@RequestBody VehicleDto vehicleDto) {
        return new ResponseEntity<>(paymentService.getPayment(vehicleDto.getPlate()), HttpStatus.OK);
    }

    @PostMapping(value = "/doPay")
    public ResponseEntity doPay(@RequestBody VehicleDto vehicleDto) {
        return new ResponseEntity<>(paymentService.doPay(vehicleDto.getPlate()), HttpStatus.OK);
    }

    @GetMapping(value = "/listMember")
    public ResponseEntity listMember() {
        return new ResponseEntity<>(memberService.listMember(), HttpStatus.OK);
    }

    @PostMapping(value = "/saveMember")
    public ResponseEntity saveMember(@RequestBody MemberDto memberDto) {
        return new ResponseEntity<>(memberService.saveMember(memberDto.getPlate()), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteMember")
    public ResponseEntity deleteMember(@RequestBody MemberDto memberDto) {
        return new ResponseEntity<>(memberService.deleteMember(memberDto.getPlate()), HttpStatus.OK);
    }

    @GetMapping(value = "/getUnitPrice")
    public ResponseEntity getUnitPrice() {
        return new ResponseEntity<>(paymentService.getUnitPrice(), HttpStatus.OK);
    }

    @PostMapping(value = "/updateUnitPrice")
    public ResponseEntity updateUnitPrice(@RequestBody UnitPriceDto unitPriceDto) {
        return new ResponseEntity<>(paymentService.updateUnitPrice(unitPriceDto.getPrice()), HttpStatus.OK);
    }

}
