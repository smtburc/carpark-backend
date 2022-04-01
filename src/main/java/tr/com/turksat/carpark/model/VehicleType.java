package tr.com.turksat.carpark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum VehicleType {

    MOTORSIKLET(1,"Motorsiklet",VehicleClass.CLASS_A),
    SEDAN(2,"Sedan",VehicleClass.CLASS_A),
    HATCBACK(3,"Hatcback",VehicleClass.CLASS_A),
    JEEP(4,"Jeep",VehicleClass.CLASS_B),
    SUV(5,"Suv",VehicleClass.CLASS_B),
    MINIBUS(6,"Minibüs",VehicleClass.CLASS_C),
    KAMYONET(7,"Kamyonet",VehicleClass.CLASS_C);


    @Getter int id;
    @Getter String name;
    @Getter VehicleClass vehicleClass;

    public static VehicleType valueOfWithId(int id){
        return Arrays.stream(values()).filter(x->x.getId()==id).findFirst().get();
    }

}
