package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.*;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.respository.RoomTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomTicketService {

    private final RoomTicketRepository roomTicketRepository;

    private final RoomService roomService;

    private final UserService userService;

    private final PublicAnswerGroupService publicAnswerGroupService;

    private final PublicAnswerService publicAnswerService;

    private final QuestionResultService questionResultService;



    @Transactional
    public Long join(RoomTicketDomain ticket){

        roomTicketRepository.save(ticket);
        return ticket.getId();
    }

    public List<RoomTicketDTO> findAllRoomTickets(){

        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<>();

        List<RoomTicketDomain> roomTicketDomainList = roomTicketRepository.findAllRoomTickets();
        //Exception Check
        if (roomTicketDomainList == null){
            throw new NoneExistingRowException("There is no Room Ticket Registered.");
        }

        for (RoomTicketDomain ticket: roomTicketDomainList) {

            RoomDTO roomDTO = new RoomDTO(roomService.findOne(ticket.getRoom().getId()));
            roomTicketDTOList.add(new RoomTicketDTO(ticket, roomDTO));
        }

        return roomTicketDTOList;
    }

    public List<RoomTicketDTO> findAllRoomTicketsByUserId(Long id){

        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<>();

        List<RoomTicketDomain> roomTicketDomainList = roomTicketRepository.findAllRoomTicketsByUserId(id);
        //Exception Check
        if (roomTicketDomainList == null){
            throw new NoneExistingRowException("There is no Room Ticket Registered.");
        }

        for (RoomTicketDomain ticket: roomTicketDomainList) {

            RoomDTO roomDTO = new RoomDTO(roomService.findOne(ticket.getRoom().getId()));
            roomTicketDTOList.add(new RoomTicketDTO(ticket, roomDTO));
        }

        return roomTicketDTOList;
    }


    public RoomTicketDomain findRoomTicket(Long id){
        return roomTicketRepository.findRoomTicket(id);
    }

    @Transactional
    public RoomTicketDomain CreateRoomTicket(String roomCode, Long userId){

        //1. Create Room Ticket Domain
        RoomTicketDomain roomTicketDomain = new RoomTicketDomain();

        //2. Search Room
        RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
        //Exception Check
        if (roomDomain == null){
            throw new NoneExistingRowException("There is no such Room code: "+roomCode);
        }

        //3. Register Room to RoomTicket Domain
        roomTicketDomain.setRoom(roomDomain);

        //4. Search User Domain
        UserDomain userDomain = userService.findUserDomain(userId);
        //Exception Check
        if (userDomain == null){
            throw new NoneExistingRowException("There is no User id: "+userId);
        }

        //5. Register User Domain to Room Ticket Domain
        roomTicketDomain.setUser(userDomain);

        //6. Create Public Answer Group
        PublicAnswerGroupDomain answerGroupDomain =  publicAnswerGroupService.CreatePublicAnswerGroup(roomDomain.getQuestionGroup());

        //7. Register Public Answer Group
       roomTicketDomain.setPublicAnswerGroup(answerGroupDomain);

        // 8. Set Ticket State
        roomTicketDomain.setTicketState(RoomTicketStateEnum.READY);

        // 9. Create Question Result Domain
        QuestionResultDomain questionResultDomain = questionResultService.CreateQuestionResult(QuestionResultType.NONE);

        // 10. Register to Room Ticket Domain
        roomTicketDomain.setQuestionResultDomain(questionResultDomain);

        // 11. Register Room Ticket Domain
        roomTicketRepository.save(roomTicketDomain);

        return roomTicketDomain;
    }

    public RoomTicketDomain findRoomTicketByUserId(Long userId, Long roomId){
        return roomTicketRepository.findRoomTicketWithUserIdAndRoomId(userId,roomId);
    }

    public RoomTicketDomain updateAnswerScore(UserDTO userDTO, RoomDTO roomDTO, String body) throws ParseException {

        // 1. Find Room Ticket
        RoomTicketDomain roomTicketDomain = this.findRoomTicketByUserId(userDTO.getId(), roomDTO.getId());
        // 2. Get PublicAnswer Group Domain
        PublicAnswerGroupDomain publicAnswerGroupDomain = roomTicketDomain.getPublicAnswerGroup();

        // 3. Parse Body String
        JSONParser jsonParse = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParse.parse(body);

        // 4. Get Answer Domain from Public Answer Group Domain
        List<PublicAnswerDomain> publicAnswerDomainList = publicAnswerService.findAnswerByAnswerGroupId(publicAnswerGroupDomain.getId());

        // 5. Update Answers Score
        for (Object o : jsonArray) {
            JSONObject questionAndAnswer = (JSONObject) o;

            Long targetQuestionId = Long.valueOf(String.valueOf(questionAndAnswer.get("questionId")));
            int score = Integer.parseInt(String.valueOf(questionAndAnswer.get("answerScore")));

            for (PublicAnswerDomain answerDomain : publicAnswerDomainList) {

                log.info(answerDomain.getQuestion().getId().toString() + " <---> " + targetQuestionId.toString());

                if (answerDomain.getQuestion().getId() == targetQuestionId) {
                    answerDomain.setScore(score);
                    log.info("Updated Score: " + answerDomain.getId());
                    log.info("New Value: " + answerDomain.getScore());
                    break;
                }

            }
        }

        // 6. Update Room Ticket State
        roomTicketDomain.setTicketState(RoomTicketStateEnum.DONE);

        return roomTicketDomain;

    }

}
