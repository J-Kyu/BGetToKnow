package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.component.SessionManager;
import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.exception.NoneExistingRowException;
import com.kyu.BGetToKnowYou.response.BasicResponse;
import com.kyu.BGetToKnowYou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Basic;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://192.168.35.57:3000"}, allowCredentials = "true")
public class UserController {

    private final UserService userService;

    private int expireTime = 60*30;

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



        UserDTO userDTO = new UserDTO(form.getNickname(),form.getOAuthType(),form.getUuid(), form.getAccessToken());

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


    @GetMapping("/user/load")
    public ResponseEntity<BasicResponse> LoadUserInfo(HttpServletRequest request){

        BasicResponse response = new BasicResponse();

        // 0. Check Session Exist
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
        }

        // 1. If Exist, check is valid one
        else if (session.getAttribute("SESSION_ID") != null){
            log.info("Session exist");
            // 0.Check Cookie
            UserDTO userDTO = (UserDTO) session.getAttribute("SESSION_ID");
            log.info("Already Signed In-"+userDTO.getNickname());

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Session 조회 성공")
                    .result(Arrays.asList(userDTO))
                    .build();
        }
        //invalid session
        else{
            log.info("Invalid Session");

            response = BasicResponse.builder()
                    .code(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("유효 하지 않는 Session 입니다")
                    .result(Collections.emptyList())
                    .build();
        }


        return new ResponseEntity<>(response,response.getHttpStatus());
    }


    @PostMapping("/user/logIn")
    public ResponseEntity<BasicResponse> UserLogIn(UserForm form, BindingResult result, HttpServletRequest request){
        BasicResponse response = new BasicResponse();

        String uuid = "";

        //Incorrect form data
        if (result.hasErrors()){
            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("Input Form 오류: "+result.toString())
                    .result(Collections.emptyList())
                    .build();
            return new ResponseEntity<>(response,response.getHttpStatus());
        }

        //Sign In
        try {
            // 1. get uuid and userDTO
            uuid = form.getOAuthType().toString() + "_" + form.getUuid();
            log.info(uuid);
            UserDomain userDomain = userService.finUserDomainByUUID(uuid);

            //update user access token, if it's either kakao or google
            if (form.getOAuthType() !=  OAuthTypeEnum.DEFAULT) {
                userService.updateUserAccessToken(userDomain.getId(), form.getAccessToken());
            }

            UserDTO userDTO = new UserDTO(userDomain);

            //create session
            HttpSession session = request.getSession(true);
            session.setAttribute("SESSION_ID",userDTO);
            session.setMaxInactiveInterval(expireTime); //30 seconds
            log.info("First Sign In-"+userDTO.getNickname());
            log.info("Session ID: "+session.getId());


            response = BasicResponse.builder()
                        .code(200)
                        .httpStatus(HttpStatus.OK)
                        .message("로그인 성공")
                        .result(Arrays.asList(userDTO))
                        .build();
        }
        catch (NoneExistingRowException e){
            // 2. If given uuid not existing, let's SIGN UP

            uuid = form.getOAuthType().toString()+"_"+form.getUuid();
            UserDTO userDTO = new UserDTO(form.getNickname(),form.getOAuthType(),uuid,form.getAccessToken());
            userService.createUser(userDTO);

            //create session id
            HttpSession session = request.getSession(true);
            session.setAttribute("SESSION_ID",userDTO);
            session.setMaxInactiveInterval(expireTime); //30 seconds

            log.info("New Sign Up-"+userDTO.getNickname());


            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("회원 생성 성공")
                    .result(Arrays.asList(userDTO))
                    .build();

        }
        catch (NullPointerException e){

            //ERROR
            response = BasicResponse.builder()
                    .code(200)
                    .message("로그인 실패. "+e.getMessage() )
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());


   }

    @GetMapping("/user/logOut")
    public ResponseEntity<BasicResponse> UserLogOut(HttpServletRequest request){
        BasicResponse response = new BasicResponse();

        //Sign Out
        try {
            UserDTO userDTO = new UserDTO();
            HttpSession session = request.getSession(false);

            if (session != null) {
                // If Session Info Exist
                userDTO= (UserDTO) session.getAttribute("SESSION_ID");

                //invalidate session info
                session.invalidate();

                log.info("Sign Out from "+userDTO.getNickname());
            }

            response = BasicResponse.builder()
                    .code(200)
                    .httpStatus(HttpStatus.OK)
                    .message("로그아웃 성공")
                    .result(Collections.emptyList())
                    .build();
        }
        catch (NullPointerException e){

            //ERROR
            response = BasicResponse.builder()
                    .code(200)
                    .message("로그아웃 실패. "+e.getMessage() )
                    .httpStatus(HttpStatus.OK)
                    .result(Collections.emptyList())
                    .build();
        }

        return new ResponseEntity<>(response,response.getHttpStatus());


    }


    @GetMapping("/cookie/save")
    public ResponseEntity<BasicResponse> SaveCookie(HttpServletResponse response,  HttpServletRequest request){

        BasicResponse responseBody = new BasicResponse();

        HttpSession session = request.getSession();
        session.setAttribute("SESSION_ID","119");
        session.setMaxInactiveInterval(expireTime); //30 seconds


        log.info("### Save Cookie ###");
        log.info(session.getId());
        log.info(session.getAttribute("SESSION_ID").toString());
        log.info("###################");

        return new ResponseEntity<>(responseBody,HttpStatus.OK);


    }

    @GetMapping("/cookie/load")
    public ResponseEntity<BasicResponse> LoadCookie(HttpServletResponse response, HttpServletRequest request){

        BasicResponse responseBody = new BasicResponse();
        HttpSession session = request.getSession(false);


        if (session == null){
            log.info("No Session exist....");
            return new ResponseEntity<>(responseBody,HttpStatus.OK);

        }

        log.info("#### Cookie Load ####");
        log.info(session.getId());
        String userId = session.getAttribute("SESSION_ID").toString();
        log.info(userId);
        log.info("#####################");

        if (request.getCookies() == null){
            log.info("No Cookie exist....");
            return new ResponseEntity<>(responseBody,HttpStatus.OK);

        }
        List<Cookie> list = List.of(request.getCookies());

        for (Cookie cookie: list ) {
            log.info(cookie.getName()+ " : "+ cookie.getValue());
        }

        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }


    @GetMapping("/cookie/remove")
    public ResponseEntity<BasicResponse> RemoveCookie(HttpServletResponse response, HttpServletRequest request){

        BasicResponse responseBody = new BasicResponse();
        HttpSession session = request.getSession(false);
        if (session != null){

            //invalidate session info
            session.invalidate();
            log.info("Remove Session");
        }

        //Remove from client: set cookie to null
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);



        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }

    @GetMapping("/test")
    public String Test(){
        return "redirect:/";
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
