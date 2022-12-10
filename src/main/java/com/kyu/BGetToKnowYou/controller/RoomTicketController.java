package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.PublicAnswerDTO;
import com.kyu.BGetToKnowYou.DTO.PublicQuestionDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.domain.*;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.response.BasicResponse;
import com.kyu.BGetToKnowYou.service.RoomService;
import com.kyu.BGetToKnowYou.service.RoomTicketService;
import com.kyu.BGetToKnowYou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomTicketController {

    private final RoomTicketService roomTicketService;
    private final RoomService roomService;

    private final UserService userService;

    @PostMapping(value="/roomTicket/new")
    public ResponseEntity<BasicResponse> create(@Valid RoomTicketForm form, BindingResult result){

        BasicResponse response = new BasicResponse();

        if (result.hasErrors()){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Input Form 오류: "+result.toString())
                    .result(Collections.emptyList())
                    .build();
            return new ResponseEntity<>(response,response.getHttpStatus());
        }

        try {
            Long roomTicketId = roomTicketService.CreateRoomTicket(form.getRoomCode(), form.getUserId());
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 생성 성공.")
                    .result(Arrays.asList(roomTicketId))
                    .build();
        }
        catch(NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 생성 실패. "+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());

    }

    @GetMapping(value="/roomTicket/findAll")
    public ResponseEntity<BasicResponse> GetAllRoomTickets(){

        BasicResponse response = new BasicResponse();

        try {
            List<RoomTicketDTO> roomTicketDTOList = roomTicketService.findAllRoomTickets();
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("모든 Room Tickets 조회 성공.")
                    .result(Arrays.asList(roomTicketDTOList))
                    .build();
        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Tickets 이 존재하지 않습니다."+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/find")
    public ResponseEntity<BasicResponse> GetRoomTicket(@PathVariable("userId")  Long userId,@PathVariable("roomCode")  String roomCode){

        BasicResponse response = new BasicResponse();

        try{
            // 1.check User id
            userService.findUserDomain(userId);

            // 2.check Room id
            RoomDomain roomDomain = roomService.findRoomByCode(roomCode);

            // 3.Find Room Ticket
            RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());

            RoomTicketDTO roomTicketDTO = new RoomTicketDTO(roomTicketDomain);

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 조회 성공")
                    .result(Arrays.asList(roomTicketDTO))
                    .build();

        }
        catch (NoneExistingRowException e){

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 조회 실패. "+e.getMessage())
                    .result(Collections.emptyList())
                    .build();

        }
//        catch (Exception e){
//
//            response = BasicResponse.builder()
//                    .code(200)
//                    .httpStatus(HttpStatus.OK)
//                    .message("Room Ticket 조회 실패. "+e.getMessage())
//                    .result(Collections.emptyList())
//                    .build();
//
//        }

        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicAnswers")
    public ResponseEntity<BasicResponse> GetPublicAnswers(@PathVariable("userId")  Long userId,@PathVariable("roomCode")  String roomCode){

        BasicResponse response = new BasicResponse();

        try{

            // 0. Check User Id
            userService.findUserDomain(userId);

            // 1. Find Room Ticket with user id and Room Code
            RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
            RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());

            // 2. Get Public Answer Group from Room Ticket Domain
            PublicAnswerGroupDomain publicAnswerGroupDomain = roomTicketDomain.getPublicAnswerGroup();

            // 3. Get All Answer Domain from Public Answer Group Domain
            List<PublicAnswerDTO> publicAnswerDTOList = new ArrayList<>();
            for (PublicAnswerDomain answer : publicAnswerGroupDomain.getAnswers() ) {
                publicAnswerDTOList.add(new PublicAnswerDTO(answer));
            }

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Public Answers 조회 성공. ")
                    .result(Arrays.asList(publicAnswerDTOList))
                    .build();


        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Public Answers 조회 실패. "+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());
    }


//    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicQuestions")
//    public List<PublicQuestionDTO> GetPublicQuestions(@PathVariable("userId")  Long userId, @PathVariable("roomCode")  String roomCode){
//
//        // 1. Find Room Ticket with user Id and Room Code
//        RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
//        RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());
//
//        // 2. Get Public Question Group from PA-Group Domain (which is from room ticket domain)
//        PublicQuestionGroupDomain publicQuestionGroupDomain = roomTicketDomain.getPublicAnswerGroup().getPublicQuestionGroup();
//
//        // 3. Get Questions from Question Group
//        List<PublicQuestionDTO> publicQuestionDTOList = new ArrayList<>();
//        for (PublicQuestionDomain question: publicQuestionGroupDomain.getQuestions() ) {
//            publicQuestionDTOList.add(new PublicQuestionDTO(question));
//        }
//
//        return publicQuestionDTOList;
//
//    }

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicQuestions")
    public ResponseEntity<BasicResponse>  GetPublicQuestions(@PathVariable("userId")  Long userId, @PathVariable("roomCode")  String roomCode){

        BasicResponse response = new BasicResponse();

        try{

            // 0. Check User Id
            userService.findUserDomain(userId);

            // 1. Find Room Ticket with user Id and Room Code
            RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
            RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());

            // 2. Get Public Question Group from PA-Group Domain (which is from room ticket domain)
            PublicQuestionGroupDomain publicQuestionGroupDomain = roomTicketDomain.getPublicAnswerGroup().getPublicQuestionGroup();

            // 3. Get Questions from Question Group
            List<PublicQuestionDTO> publicQuestionDTOList = new ArrayList<>();
            for (PublicQuestionDomain question: publicQuestionGroupDomain.getQuestions() ) {
                publicQuestionDTOList.add(new PublicQuestionDTO(question));
            }

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Public Questions 조회 성공. ")
                    .result(Arrays.asList(publicQuestionDTOList))
                    .build();

        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Public Questions 조회 실패. "+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());
    }


}
