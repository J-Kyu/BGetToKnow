package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoUserFoundException;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
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

    private final RoomService roomService;


    @Transactional
    public Long join(UserDomain user){
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Long createUser(UserDTO userDTO){

        UserDomain user = new UserDomain(userDTO);

        //check duplicated nickname
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
            UserDTO userDTO = new UserDTO();

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    public List<RoomTicketDTO> findRoomTicketDTOListByUserId(Long id){

        // Find User
        UserDomain user = userRepository.findOne(id);

        //Exception Check
        if (user == null){
            throw new NoneExistingRowException("There is no User id: "+id);
        }



        // Search Room Ticket
        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<>();
        for ( RoomTicketDomain ticket: user.getTickets() ) {
            RoomDTO roomDTO = new RoomDTO(roomService.findOne(ticket.getRoom().getId()));
            roomTicketDTOList.add(new RoomTicketDTO(ticket, roomDTO));
        }

        return roomTicketDTOList;
    }

    public UserDTO finUserDTO(Long userId) {
        UserDomain user = userRepository.findOne(userId);
        return new UserDTO(user);
    }

    public UserDTO finUserDTOByUUID(String uuid) {
        UserDomain user = userRepository.findByUUID(uuid);
        return new UserDTO(user);
    }

    public UserDomain finUserDomainByUUID(String uuid) {
        return userRepository.findByUUID(uuid);
    }
    public UserDomain findOne(Long userId) {

        //find User
        return userRepository.findOne(userId);

    }

    public List<RoomDTO> findRoomDTOByUserId(Long id){

        // Find User
        UserDomain user = userRepository.findOne(id);

        //Convert Room Domain into Room DTO
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for ( RoomDomain room: user.getRooms() ) {
            roomDTOList.add(new RoomDTO(room));
        }

        return roomDTOList;
    }

    @Transactional
    public void updateUserAccessToken(Long id, String accessToken){
        // find user domain
        UserDomain userDomain = this.findOne(id);

        if (userDomain == null){
            throw new NoUserFoundException("No given user found: "+id);
        }

        //update access token
        userDomain.setAccessToken(accessToken);;
    }

}
