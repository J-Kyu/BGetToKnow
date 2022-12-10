package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.DTO.PublicQuestionDTO;
import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.response.BasicResponse;
import com.kyu.BGetToKnowYou.service.RoomService;
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

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    @PostMapping(value = "/room/new")
    public ResponseEntity<BasicResponse> create(@Valid RoomForm form, BindingResult result){

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


        RoomDTO roomDTO = new RoomDTO(form.getMaxNum());
        try{
            UserDomain userDomain = userService.findUserDomain(form.getUserId());
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
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room 생성 실패. 존재하지 않는 회원 ID 입니다."+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }
        catch (Exception e){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("알수없는 Error 발생. "+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }


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
        catch(NullPointerException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("Room 조회 실패. 존재하지 않는 Room입니다.")
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());

        }

    @GetMapping("/room/{code}/getPublicQuestions")
    public ResponseEntity<BasicResponse> GetPublicQuestions(@PathVariable("code")  String code){

        BasicResponse response = new BasicResponse();

        try {
            List<PublicQuestionDTO> publicQuestionDTOList = roomService.GetPublicQuestions(code);
            response = BasicResponse.builder()
                    .code(200)
                    .message("Public Questions 조회 성공")
                    .httpStatus(HttpStatus.OK)
                    .result(Arrays.asList(publicQuestionDTOList))
                    .build();
        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("Public Questions 조회 실패. "+e.getMessage())
                    .httpStatus(HttpStatus.OK)
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

}
