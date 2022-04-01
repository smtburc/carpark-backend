package tr.com.turksat.carpark.repository.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import tr.com.turksat.carpark.model.Member;
import tr.com.turksat.carpark.model.VehiclePark;
import tr.com.turksat.carpark.model.VehicleType;
import tr.com.turksat.carpark.repository.MemberRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Repository
@ApplicationScope
public class MemberRepositoryImpl implements MemberRepository {

    List<Member> members;
    Long sequence;

    public MemberRepositoryImpl(){
        init();
    }

    @SneakyThrows
    private void init() {
        members = new ArrayList<>();
        members.add(new Member(1L,"06ABC06"));
        members.add(new Member(2L,"06ABC07"));
        members.add(new Member(3L,"06ABC08"));
        sequence = members.stream().mapToLong(x->x.getId()).max().getAsLong();
    }

    @Override
    public List<Member> findAll() {
        return members;
    }

    @Override
    public Member getByPlate(String plate) {
        Optional<Member> member =members.stream().filter(x->x.getPlate().equalsIgnoreCase(plate)).findFirst();
        if(member.isPresent()){
            return member.get();
        }
        return null;
    }

    @Override
    public Member save(Member member) {
        sequence++;
        member.setId(sequence);
        members.add(member);
        return member;
    }

    @Override
    public boolean deleteWithPlate(String plate) {
        Optional<Member> member = members.stream().filter(x->x.getPlate().equalsIgnoreCase(plate)).findFirst();
        if(member.isPresent()){
            members.remove(member.get());
            return true;
        }
        return false;
    }

}
