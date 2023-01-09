package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.*;
import com.kyu.BGetToKnowYou.domain.*;
import com.kyu.BGetToKnowYou.exception.NoRoomFoundException;
import com.kyu.BGetToKnowYou.exception.NoRoomTicketFoundException;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.response.BasicResponse;
import com.kyu.BGetToKnowYou.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.json.simple.parser.ParseException;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost","http://gettoknow.life/", "http://52.78.139.73:80","http://52.78.139.73"}, allowCredentials = "true")
public class RoomTicketController {

    private final RoomTicketService roomTicketService;
    private final RoomService roomService;

    private final PublicAnswerGroupService publicAnswerGroupService;
    private final UserService userService;

    private final CategoryResultService categoryResultService;


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
            RoomTicketDomain roomTicketDomain = roomTicketService.CreateRoomTicket(form.getRoomCode(), form.getUserId());
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 생성 성공.")
                    .result(Collections.emptyList())
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
                    .result(Arrays.asList(Collections.emptyList()))
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
            userService.findOne(userId);

            // 2.check Room id
            RoomDomain roomDomain = roomService.findRoomByCode(roomCode);

            // 3.Find Room Ticket
            RoomTicketDomain roomTicketDomain = roomTicketService.findRoomTicketByUserId(userId, roomDomain.getId());

            RoomTicketDTO roomTicketDTO = new RoomTicketDTO(roomTicketDomain, new RoomDTO(roomDomain));

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


        return new ResponseEntity<>(response,response.getHttpStatus());
    }


    @GetMapping(value="/roomTicket/{roomCode}/find")
    public ResponseEntity<BasicResponse> GetRoomTicket2(@PathVariable("roomCode")  String roomCode, HttpServletRequest request){

        BasicResponse response = new BasicResponse();

        // 0. check available session
        HttpSession session = request.getSession(false);
        if (session == null){
            // no available session
            log.info("Session does not exist");

            response = BasicResponse.builder()
                    .code(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("Session 정보가 없습니다")
                    .result(Collections.emptyList())
                    .build();

            return new ResponseEntity<>(response,response.getHttpStatus());
        }

        //Variables
        UserDTO userDTO = null;
        RoomDTO roomDTO = null;
        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<RoomTicketDTO>();
        RoomTicketDomain roomTicketDomain = null;
        RoomTicketDTO roomTicketDTO = null;



        try{

            // 1.Get User Domain by session info
            userDTO = (UserDTO) session.getAttribute("SESSION_ID");


            // 2.check Room id
            RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
            roomDTO = new RoomDTO(roomDomain);

            // 2-2. Collect Ticket data
            for (RoomTicketDomain ticket: roomDomain.getRoomTickets()) {
                roomTicketDTOList.add(new RoomTicketDTO(ticket, roomDTO));
            }

            // 3.Find Room Ticket
            roomTicketDomain = roomTicketService.findRoomTicketByUserId(userDTO.getId(), roomDomain.getId());


            //return room ticket
            roomTicketDTO = new RoomTicketDTO(roomTicketDomain, roomDTO);
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 조회 성공")
                    .result(Arrays.asList(roomTicketDTO))
                    .build();


            return new ResponseEntity<>(response,response.getHttpStatus());


        }
        catch(NoRoomTicketFoundException e){

            //if no room ticket exist, generate ticket
            // but first let's check if room is occupied
            if(roomTicketDTOList.size() < roomDTO.getMaxNum() ){
                //Create Room Ticket
                roomTicketDomain= roomTicketService.CreateRoomTicket(roomDTO.getCode(), userDTO.getId());
                roomTicketDTO = new RoomTicketDTO(roomTicketDomain, roomDTO);

                response = BasicResponse.builder()
                        .code(200)
                        .httpStatus(HttpStatus.OK)
                        .message("Room Ticket 생성 성공")
                        .result(Arrays.asList(roomTicketDTO))
                        .build();


            }
            else{
                //response with max ticket reached
                response = BasicResponse.builder()
                        .code(400)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Room Ticket 발행 실패. Room 에 대한 발행된 Ticket의 수가 최대치를 도달했습니다.")
                        .result(Collections.emptyList())
                        .build();

            }

        }

        catch (NoneExistingRowException e){

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket 조회 실패. "+e.getMessage())
                    .result(Collections.emptyList())
                    .build();

        }
        catch (NoRoomFoundException | NullPointerException e){
            response = BasicResponse.builder()
                    .code(400)
                    .message("Room 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @GetMapping(value="/roomTicketList/findAll")
    public ResponseEntity<BasicResponse> GetAllRoomTickets(HttpServletRequest request){

        BasicResponse response = new BasicResponse();

        // 0. check available session
        HttpSession session = request.getSession(false);
        if (session == null){
            // no available session
            log.info("Session does not exist");

            response = BasicResponse.builder()
                    .code(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("Session 정보가 없습니다")
                    .result(Collections.emptyList())
                    .build();

            return new ResponseEntity<>(response,response.getHttpStatus());
        }

        //Variables
        UserDTO userDTO = null;

        try{

            // 1.Get User Domain by session info
            userDTO = (UserDTO) session.getAttribute("SESSION_ID");

            List<RoomTicketDTO> roomTicketDTOList = roomTicketService.findAllRoomTicketsByUserId(userDTO.getId());


            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket과 Room Info 조회 성공")
                    .result(new ArrayList<Object>(roomTicketDTOList))
                    .build();


            return new ResponseEntity<>(response,response.getHttpStatus());


        }
        catch(NoRoomTicketFoundException e){


                //response with max ticket reached
                response = BasicResponse.builder()
                        .code(400)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("존재하지 않는 Room Ticket 입니다.")
                        .result(Collections.emptyList())
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
        catch (NoRoomFoundException | NullPointerException e){
            response = BasicResponse.builder()
                    .code(400)
                    .message("Room 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());
    }



    @PostMapping(value="/roomTicket/{roomCode}/updateAnswersScore")
    public ResponseEntity<BasicResponse> UpdateAnswersScore( @PathVariable("roomCode")  String roomCode, HttpServletRequest request, @RequestBody String body){
        BasicResponse response = new BasicResponse();

        // 0. check available session
        HttpSession session = request.getSession(false);
        if (session == null){
            // no available session
            log.info("Session does not exist");

            response = BasicResponse.builder()
                    .code(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("Session 정보가 없습니다")
                    .result(Collections.emptyList())
                    .build();

            return new ResponseEntity<>(response,response.getHttpStatus());
        }

        //Variables
        UserDTO userDTO = null;
        RoomDTO roomDTO = null;
        List<RoomTicketDTO> roomTicketDTOList = new ArrayList<RoomTicketDTO>();
        RoomTicketDomain roomTicketDomain = null;
        RoomTicketDTO roomTicketDTO = null;

        try{

            // 1.Get User Domain by session info
            userDTO = (UserDTO) session.getAttribute("SESSION_ID");

            // 2.check Room id
            RoomDomain roomDomain = roomService.findRoomByCode(roomCode);
            roomDTO = new RoomDTO(roomDomain);

            // 3. Update Room Ticket and Answers
            roomTicketDomain = roomTicketService.updateAnswerScore(userDTO, roomDTO, body);

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Update 가 잘 되었습니다.")
                    .result(Arrays.asList(new RoomTicketDTO(roomTicketDomain,roomDTO)))
                    .build();
        }
        catch (ParseException e) {
            response = BasicResponse.builder()
                    .code(500)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Parsing 실패")
                    .result(Collections.emptyList())
                    .build();
        }
//        catch (Exception e){
//            response = BasicResponse.builder()
//                    .code(500)
//                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .message("Internal Error. "+e.getMessage())
//                    .result(Collections.emptyList())
//                    .build();
//        }


        return new ResponseEntity<>(response,response.getHttpStatus());

    }

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicAnswers")
    public ResponseEntity<BasicResponse> GetPublicAnswers(@PathVariable("userId")  Long userId,@PathVariable("roomCode")  String roomCode){

        BasicResponse response = new BasicResponse();

        try{

            // 0. Check User Id
            userService.findOne(userId);

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

    @GetMapping(value="/roomTicket/{userId}/{roomCode}/getPublicQuestions")
    public ResponseEntity<BasicResponse>  GetPublicQuestions(@PathVariable("userId")  Long userId, @PathVariable("roomCode")  String roomCode){

        BasicResponse response = new BasicResponse();

        try{

            // 0. Check User Id
            userService.findOne(userId);

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
