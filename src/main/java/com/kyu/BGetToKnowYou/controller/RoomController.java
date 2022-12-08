package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.DTO.PublicQuestionDTO;
import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.service.RoomService;
import com.kyu.BGetToKnowYou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final UserService userService;

    @PostMapping(value = "/room/new")
    public String create(@Valid RoomForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        RoomDTO roomDTO = new RoomDTO(form.getMaxNum());

        UserDomain userDomain = userService.findUserDomain(form.getUserId());

        String roomCode = roomService.CreateRoom(roomDTO, userDomain);

        return roomCode;
    }

    @GetMapping("/room/{code}/find")
    public RoomDTO GetRoomByCode(@PathVariable("code")  String code){
        return new RoomDTO(roomService.findRoomByCode(code));
    }

    @GetMapping("/room/{code}/getPublicQuestions")
    public List<PublicQuestionDTO> GetPublicQuestions(@PathVariable("code")  String code){
        return roomService.GetPublicQuestions(code);
    }


    @GetMapping("/room/findAll")
    public List<RoomDTO> GetAllRoom(){
        return roomService.findAllRoomDTO();
    }




}
