package tr.com.turksat.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tr.com.turksat.carpark.model.VehicleClass;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.model.dto.ParkingLotDto;
import tr.com.turksat.carpark.repository.VehicleParkRepository;
import tr.com.turksat.carpark.service.ParkingService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final VehicleParkRepository carParkRepository;

    //Aynı anda aynı alana iki kayıt gelmesini engellemek için synchronized yapıldı
    @Override
    public synchronized Boolean vehicleIn(String plate, int type) {
        plate = plate.strip();
        if (carParkRepository.getByPlate(plate) != null) {
            throw new RuntimeException("Araç zaten içeride");
        }
        BigDecimal firstPoint = choosePlace(VehicleType.valueOfWithId(type).getVehicleClass());
        if (firstPoint == null) {
            throw new RuntimeException("Park Edilecek Yer Yok");
        }

        VehiclePark vehicle = new VehiclePark();
        vehicle.setPlate(plate);
        vehicle.setType(VehicleType.valueOfWithId(type));
        vehicle.setFirstPoint(firstPoint);
        vehicle.setEndPoint(vehicle.getFirstPoint().add(VehicleType.valueOfWithId(type).getVehicleClass().getMultiple()));
        vehicle.setEntryDate(new Date());

        return carParkRepository.save(vehicle).getId() != null;

    }

    @Override
    public Boolean vehicleOut(String plate) {
        plate = plate.strip();
        if (carParkRepository.getByPlate(plate) == null) {
            throw new RuntimeException("Araç içeride değil");
        }
        return carParkRepository.deleteWithPlate(plate);
    }

    @Override
    public Set<ParkingLotDto> listParkingLot() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Set<ParkingLotDto> result = carParkRepository.findAll().stream().map(x -> {
            ParkingLotDto dto = new ParkingLotDto();
            dto.setPlate(x.getPlate());
            dto.setValue(x.getPlate() + " (" + x.getType().getName() + ") " + simpleDateFormat.format(x.getEntryDate()));
            dto.setVehicleClass(x.getType().getVehicleClass().name());
            dto.setFirstPoint(x.getFirstPoint());
            return dto;
        }).collect(Collectors.toCollection(TreeSet::new));
        Map<BigDecimal, BigDecimal> emptyPlaces = findEmptyPlaces();
        Set<ParkingLotDto> emptyPlacesDto = emptyPlaces.keySet().stream().map(x -> {
            ParkingLotDto dto = new ParkingLotDto();
            dto.setValue("Boş Park Alanı (" + emptyPlaces.get(x) + " Birim)");
            dto.setFirstPoint(x);
            return dto;
        }).collect(Collectors.toCollection(TreeSet::new));
        result.addAll(emptyPlacesDto);
        return result;
    }



    // En Verimli Olarak Alan seçimi yapılacaktır
    // Alan seçme algoritması
    // 1. Boş alanlar getirilir
    // 2. En küçük alandan itibaren kayıplar hesaplanır
    // 3. Kayıp olmayan denk gelirse direk seçilik
    // 4. Her türlü kayıp varsa en düşük kayıp seçilir
    private BigDecimal choosePlace(VehicleClass vehicleClass) {
        //Otoparkdaki boş alanları bul
        Map<BigDecimal, BigDecimal> emptyPlaces = findEmptyPlaces();

        //Eğer boş alan yoksa veya boş alanların hiçbiri araç uzunluğuna eşit veya büyük değilse
        if(emptyPlaces.isEmpty() || emptyPlaces.values().stream().allMatch(x->x.compareTo(vehicleClass.getMultiple())==-1)){
            return null;
        }
        //Alan uzunlukları key, alan konumları value olacak şekilde ters çevriliyor
        TreeMap<BigDecimal, BigDecimal> emptyPlacesTreeMap = emptyPlaces.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,(oldValue, newValue) -> newValue, TreeMap::new));

        //Kayıp olmayan alan varsa onu yoksa en küçük kayıp olan alanı bul
        BigDecimal min = null;
        BigDecimal choose = null;
        for (BigDecimal emptySize : emptyPlacesTreeMap.keySet()) {
            //Eğer alan araç büyüklüğünden küçük ise dikkate alma
            if(emptySize.compareTo(vehicleClass.getMultiple())==-1){
                continue;
            }
            if (choose == null) {
                choose = emptySize;
                BigDecimal calc = calculateLost(List.of(BigDecimal.ONE, BigDecimal.valueOf(1.2), BigDecimal.valueOf(2)), emptySize.subtract(vehicleClass.getMultiple()));
                if (calc.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                } else {
                    min = calc;
                }
            } else {
                BigDecimal calc = calculateLost(List.of(BigDecimal.ONE, BigDecimal.valueOf(1.2), BigDecimal.valueOf(2)), emptySize.subtract(vehicleClass.getMultiple()));
                if (calc.compareTo(BigDecimal.ZERO) == 0) {
                    choose = emptySize;
                    break;
                } else if (calc.compareTo(min) == -1) {
                    choose = emptySize;
                    min = calc;
                }
            }
        }
        return emptyPlacesTreeMap.get(choose);
    }

    //en düşük kaybı hesaplayan recursive metod
    //Örn: 1.4 uzunluğu için 1.2 lik araç gelebilir ve 0.2 kayıp oluşur
    //Örn: 1.6 uzunluğu için 1.2 lik araç gelebilir ve 0.4 kayıp oluşur
    //Örn: 1.8 uzunluğu için 1.2 lik araç gelebilir ve 0.8 kayıp oluşur
    //Örn: 2 uzunluğu için kayıp yoktur. (2 veya 1x2 gelebilir)
    //Örn: 2.2 uzunluğu için kayıp yoktur. (1 ve 1.2 gelebilir)
    //Örn: 2.4 uzunluğu için kayıp yoktur. (2x1.2 gelebilir)
    //Örn: 2.6 uzunluğu için 2x1.2 lik araç gelebilir ve 0.2 kayıp oluşur
    private BigDecimal calculateLost(List<BigDecimal> sizes, BigDecimal size) {
        if (size.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else if (size.compareTo(BigDecimal.ZERO) == -1) {
            return BigDecimal.TEN;
        }
        BigDecimal min = size;
        for (BigDecimal s : sizes) {
            BigDecimal newSize = size.subtract(s);
            BigDecimal kalan = calculateLost(sizes, newSize);
            if (kalan.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            } else {
                if (kalan.compareTo(min) == -1) {
                    min = kalan;
                }
            }
        }
        return min;
    }

    //Otopark daki boş alanları veren metod. Key: Başlangıç noktası, Value: Uzunluk
    private Map<BigDecimal, BigDecimal> findEmptyPlaces() {
        //otoparktaki araçları getir
        TreeSet<VehiclePark> vehicles = carParkRepository.findAll();
        //Map key=başlangıç noktası; value=size
        Map<BigDecimal, BigDecimal> emptyPlace = new HashMap<>();

        if (vehicles.isEmpty()) {
            //Eğer Otopark Tamamen Boş ise
            emptyPlace.put(BigDecimal.ZERO, BigDecimal.valueOf(50));
            return emptyPlace;
        }


        Iterator<VehiclePark> vehicleIterator = vehicles.iterator();
        VehiclePark before = vehicleIterator.next();
        //Eğer başta boşluk varsa
        if (before.getFirstPoint().compareTo(BigDecimal.ZERO) == 1) {
            emptyPlace.put(BigDecimal.ZERO, before.getFirstPoint());
        }
        //Aradaki Boşlukları bul
        while (vehicleIterator.hasNext()) {
            VehiclePark after = vehicleIterator.next();
            BigDecimal size = after.getFirstPoint().subtract(before.getEndPoint());
            if (size.doubleValue() > 0) {
                emptyPlace.put(before.getEndPoint(), size);
            }
            before = after;
        }
        //Sonda Kalan Kısmı Boşluk Olarak Ekle
        if (before.getEndPoint().compareTo(BigDecimal.valueOf(50.0)) == -1) {
            emptyPlace.put(before.getEndPoint(), BigDecimal.valueOf(50.0).subtract(before.getEndPoint()));
        }

        return emptyPlace;
    }




}
