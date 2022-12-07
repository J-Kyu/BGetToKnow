package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(UserDomain user){
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }


    private void validateDuplicateUser(UserDomain user){
        //EXCEPTION
        List<UserDomain> findMembers = userRepository.findByNickname(user.getNickname());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<UserDTO> findAllUsers(){
        List<UserDTO> userDTOList = new ArrayList<>();

        List<UserDomain> userDomainsList =  userRepository.findAll();
        for (UserDomain user: userDomainsList ) {
            UserDTO userDTO = new UserDTO(user);
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    public List<RoomTicketDTO> findRoomTicketsByUserId(Long id){

        UserDomain user = userRepository.findOne(id);
        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<>();
        for ( RoomTicketDomain ticket: user.getTickets() ) {
            roomTicketDTOList.add(new RoomTicketDTO(ticket));
        }

        return roomTicketDTOList;
    }

    public UserDTO findOne(Long userId) {
        UserDomain user = userRepository.findOne(userId);
        return new UserDTO(user);
    }

}
