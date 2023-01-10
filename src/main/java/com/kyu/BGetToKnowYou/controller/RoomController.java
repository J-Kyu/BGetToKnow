package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.DTO.PublicQuestionDTO;
import com.kyu.BGetToKnowYou.DTO.QuestionResultDTO;
import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoRoomFoundException;
import com.kyu.BGetToKnowYou.exception.NoRoomTicketFoundException;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.response.BasicResponse;
import com.kyu.BGetToKnowYou.service.RoomService;
import com.kyu.BGetToKnowYou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://gettoknow.life/"}, allowCredentials = "true")
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    @PostMapping(value = "/room/new")
    public ResponseEntity<BasicResponse> create(@Valid RoomForm form, BindingResult result, HttpServletRequest request){

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

        // 0. Check Input Form
        if (result.hasErrors()){

            response = BasicResponse.builder()
                    .code(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Input Form 오류: "+result.toString())
                    .result(Collections.emptyList())
                    .build();
            return new ResponseEntity<>(response,response.getHttpStatus());
        }


        try{
            //create User DTO with Session Info
            UserDTO userDTO = (UserDTO) session.getAttribute("SESSION_ID");

            //create Room DTO
            RoomDTO roomDTO = new RoomDTO(form.getMaxNum(),form.getRoomType(),form.getReleaseDateTime());


            UserDomain userDomain = userService.findOne(userDTO.getId());
            String roomCode = roomService.CreateRoom(roomDTO, userDomain);

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room 생성 성공")
                    .result(Arrays.asList(roomCode))
                    .build();

        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Room 생성 실패. 존재하지 않는 회원 ID 입니다."+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }
//        catch (Exception e){
//            response = BasicResponse.builder()
//                    .code(400)
//                    .httpStatus(HttpStatus.BAD_REQUEST)
//                    .message("알수없는 Error 발생. "+e.getMessage())
//                    .result(Collections.emptyList())
//                    .build();
//        }


        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @GetMapping("/room/{code}/find")
    public ResponseEntity<BasicResponse> GetRoomByCode(@PathVariable("code")  String code){

        BasicResponse response = new BasicResponse();

        //check parameter
        if (code.length() == 0){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("올바르지 않는 Code 입니다.Code를 확인해주세요.")
                    .result(Collections.emptyList())
                    .build();

            return new ResponseEntity<>(response,response.getHttpStatus());
        }

        try{
            RoomDTO roomDTO = new RoomDTO(roomService.findRoomByCode(code));
            response = BasicResponse.builder()
                    .code(200)
                    .message("Room 조회 성공.")
                    .httpStatus(HttpStatus.OK)
                    .result(Arrays.asList(roomDTO))
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

    @GetMapping("/room/{code}/getPublicQuestions")
    public ResponseEntity<BasicResponse> GetPublicQuestions(@PathVariable("code")  String code, HttpServletRequest request){

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


        try {
            List<PublicQuestionDTO> publicQuestionDTOList = roomService.GetPublicQuestions(code);

            log.info("Get Public Questions-->");

            response = BasicResponse.builder()
                    .code(200)
                    .message("Public Questions 조회 성공")
                    .httpStatus(HttpStatus.OK)
                    .result(new ArrayList<>(publicQuestionDTOList))
                    .build();
        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(400)
                    .message("Public Questions 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .result(Collections.emptyList())
                    .build();
        }
        catch (NoRoomFoundException e){
            response = BasicResponse.builder()
                    .code(400)
                    .message("Room 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());

    }

    @GetMapping("/room/findAll")
    public ResponseEntity<BasicResponse> GetAllRoom(){

        BasicResponse response = new BasicResponse();


        try {
            List<RoomDTO>  roomDTOList = roomService.findAllRoomDTO();

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("전체 Room 조회 성공")
                    .result(Arrays.asList(roomDTOList))
                    .build();
        }
        catch (NullPointerException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("전체 Room 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("전체 Room 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());


    }


    @GetMapping("/room/{code}/getRoomResult")
    public ResponseEntity<BasicResponse> GetRoomResult(@PathVariable("code")  String roomCode, HttpServletRequest request){

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


        try{
            List<QuestionResultDTO> questionResultDTOList = new ArrayList<>();

            // 1.Get User Domain by session info
            UserDTO userDTO = (UserDTO) session.getAttribute("SESSION_ID");

            questionResultDTOList = roomService.GetRoomResult(roomCode, userDTO.getId());

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Result 정보 조회 성공")
                    .result(new ArrayList<>(questionResultDTOList))
                    .build();
        }
        catch (NoRoomTicketFoundException e){
            response = BasicResponse.builder()
                    .code(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("Room Ticket 및 Room code 불일치. " +e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }
        catch (NoRoomFoundException e){
            response = BasicResponse.builder()
                    .code(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());


    }
}
