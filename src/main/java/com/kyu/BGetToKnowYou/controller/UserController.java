package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.response.BasicResponse;
import com.kyu.BGetToKnowYou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity test(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/user/new")
    public ResponseEntity<BasicResponse> create(@Valid UserForm form, BindingResult result){

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



        UserDTO userDTO = new UserDTO(form.getNickname(),OAuthTypeEnum.DEFAULT);

        try {
            userService.createUser(userDTO);

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("회원 생성 성공")
                    .result(Arrays.asList(userDTO))
                    .build();
        }
        catch (IllegalStateException e){

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("회원 생성 실패."+e.getMessage())
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());

    }

    @GetMapping("/user/findAll")
    public ResponseEntity<BasicResponse> GetUserList(){

        BasicResponse response = new BasicResponse();

        try {
            List<UserDTO> userDTOList = userService.findAllUsers();

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("회원 전체 조회 성공")
                    .result(Arrays.asList(userDTOList))
                    .build();
        }
        catch (NullPointerException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("회원이 존재하지 않습니다.")
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());


    }


    @GetMapping("/user/{userId}/find")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<BasicResponse> GetUserInfo(@PathVariable("userId") Long userId){

        BasicResponse response = new BasicResponse();

        try {
            UserDTO userDTO = userService.finUserDTO(userId);
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("회원 조회 성공")
                    .result(Arrays.asList(userDTO))
                    .build();
        }
        catch (NullPointerException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("회원 조회 실패. 존재하지 않는 회원입니다. "+e.getMessage() )
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @GetMapping("/user/{userId}/roomTickets/findAll")
    public ResponseEntity<BasicResponse> GetUserTickets(@PathVariable("userId") Long userId){

        BasicResponse response = new BasicResponse();
        try {
            List<RoomTicketDTO> roomTicketDTOList = userService.findRoomTicketDTOListByUserId(userId);

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room Ticket List 조회 성공")
                    .result(Arrays.asList(roomTicketDTOList))
                    .build();
        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("회원 조회 실패. 존재하지 않는 회원입니다." + e.getMessage())
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());

    }

    @GetMapping("/user/{userId}/createdRoom")
    public ResponseEntity<BasicResponse> GetCreatedRoomByUserId(@PathVariable("userId") Long userId){

        BasicResponse response = new BasicResponse();

        try {
            List<RoomDTO> roomDTOList = userService.findRoomDTOByUserId(userId);

            UserDTO userDTO = userService.finUserDTO(userId);
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Room 조회 성공")
                    .result(Arrays.asList(roomDTOList))
                    .build();
        }
        catch (NoneExistingRowException e){
            response = BasicResponse.builder()
                    .code(200)
                    .message("회원 조회 실패. 존재하지 않는 회원입니다.")
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());

    }

}
