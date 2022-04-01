package tr.com.turksat.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.turksat.carpark.model.Member;
import tr.com.turksat.carpark.model.dto.MemberDto;
import tr.com.turksat.carpark.repository.MemberRepository;
import tr.com.turksat.carpark.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDto> listMember() {
        return memberRepository.findAll().stream().map(x->{
                MemberDto dto=new MemberDto();
                dto.setId(x.getId());
                dto.setPlate(x.getPlate());
                return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isMember(String plate) {
        plate = plate.strip();
        return memberRepository.getByPlate(plate)!=null;
    }

    @Override
    public boolean saveMember(String plate) {
        plate = plate.strip();
        if(memberRepository.getByPlate(plate)!=null){
            throw new RuntimeException("Bu Plakaya üyelik zaten kayıtlı");
        }
        Member member = new Member();
        member.setPlate(plate);
        return memberRepository.save(member).getId()!=null;
    }

    @Override
    public boolean deleteMember(String plate) {
        plate = plate.strip();
        return memberRepository.deleteWithPlate(plate);
    }
}
