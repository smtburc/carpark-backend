package tr.com.turksat.carpark.service;

import tr.com.turksat.carpark.model.dto.MemberDto;

import java.util.List;

public interface MemberService {

    List<MemberDto> listMember();

    boolean isMember(String plate);

    boolean saveMember(String plate);

    boolean deleteMember(String plate);

}
