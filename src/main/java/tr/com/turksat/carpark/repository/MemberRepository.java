package tr.com.turksat.carpark.repository;

import tr.com.turksat.carpark.model.Member;
import tr.com.turksat.carpark.model.VehiclePark;

import java.util.List;
import java.util.TreeSet;

public interface MemberRepository {

    List<Member> findAll();

    Member getByPlate(String plate);

    Member save(Member plate);

    boolean deleteWithPlate(String plate);


}
