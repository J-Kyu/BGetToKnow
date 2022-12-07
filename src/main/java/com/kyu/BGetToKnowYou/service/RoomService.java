package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.respository.RoomRepository;
import com.kyu.BGetToKnowYou.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {


    private final RoomRepository roomRepository;

    @Transactional
    public String join(RoomDomain room){

        //generate room code
        String roomCode = GenerateRoomCode(6);
        room.setCode(roomCode);

        roomRepository.save(room);
        return room.getCode();
    }

    public List<RoomDomain> findRooms(){return roomRepository.findAll();}

    public RoomDomain findOne(Long roomId) {return roomRepository.findOne(roomId);}

    public RoomDomain findRoomByCode(String roomCode) {return roomRepository.findRoomByCode(roomCode);}



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

}
