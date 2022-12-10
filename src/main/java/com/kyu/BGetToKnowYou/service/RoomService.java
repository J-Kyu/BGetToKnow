package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.DTO.PublicQuestionDTO;
import com.kyu.BGetToKnowYou.DTO.PublicQuestionGroupDTO;
import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.PublicQuestionDomain;
import com.kyu.BGetToKnowYou.domain.PublicQuestionGroupDomain;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.respository.RoomRepository;
import com.kyu.BGetToKnowYou.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {


    private final RoomRepository roomRepository;
    private final PublicQuestionGroupService publicQuestionGroupService;

    @Transactional
    public String join(RoomDTO room){

        //generate room code
        String roomCode = GenerateRoomCode(6);
        room.setCode(roomCode);

        roomRepository.save(new RoomDomain(room));
        return room.getCode();
    }

    public List<RoomDomain> findAllRoomDomain(){
        return roomRepository.findAll();
    }

    public List<RoomDTO> findAllRoomDTO(){
        List<RoomDomain> roomDomainList = roomRepository.findAll();

        //Exception Check
        if (roomDomainList == null){
            throw new NoneExistingRowException("There is no Exist");
        }

        List<RoomDTO> roomDTOList = new ArrayList<>();

        for (RoomDomain room: roomDomainList ) {
            roomDTOList.add(new RoomDTO(room));
        }
        return roomDTOList;
    }

    public RoomDomain findOne(Long roomId) {return roomRepository.findOne(roomId);}

    public RoomDomain findRoomByCode(String roomCode) {
        return roomRepository.findRoomByCode(roomCode);
    }

    private String GenerateRoomCode(int codeLength){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = codeLength;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;

    }

    public List<PublicQuestionDTO> GetPublicQuestions(String code){

        RoomDomain room = this.findRoomByCode(code);
        //Exception Check
        if (room == null){
            throw new NoneExistingRowException("There is no Room Code: "+code);
        }

        PublicQuestionGroupDomain publicQuestionGroup = room.getQuestionGroup();
        List<PublicQuestionDTO> publicQuestionDTOList = new ArrayList<>();

        for (PublicQuestionDomain question: publicQuestionGroup.getQuestions() ) {
            publicQuestionDTOList.add(new PublicQuestionDTO(question));
        }

        return publicQuestionDTOList;

    }

    @Transactional
    public String CreateRoom(RoomDTO roomDTO, UserDomain userDomain ){
        /*
            Since we already have room DTO and userDomain,
            we need to generate Question Group to consist question list
            before creating Room Domain.
        */

        // 1.New Room Domain
        RoomDomain roomDomain = new RoomDomain(roomDTO);
        String roomCode = GenerateRoomCode(6);
        roomDomain.setCode(roomCode);

        // 2. Register User Domain
        roomDomain.setAdminUser(userDomain);

        // 3. Register Question Group
        PublicQuestionGroupDomain publicQuestionGroupDomain = publicQuestionGroupService.findPublicQuestionGroupByTypeRandomly(roomDomain.getRoomType());
        roomDomain.setQuestionGroup(publicQuestionGroupDomain);


        roomRepository.save(roomDomain);

        return  roomCode;
    }

}
