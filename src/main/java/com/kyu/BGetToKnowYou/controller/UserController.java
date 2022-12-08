package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import com.kyu.BGetToKnowYou.DTO.RoomTicketDTO;
import com.kyu.BGetToKnowYou.DTO.UserDTO;
import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.RoomTicketDomain;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.service.RoomTicketService;
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
public class UserController {

    private final UserService userService;

    @PostMapping("/user/new")
    public String create(@Valid UserForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        UserDomain user = new UserDomain();
        user.setOAuthType(OAuthTypeEnum.DEFAULT);
        user.setNickname(form.getNickname());

        userService.join(user);



        return "redirect:/";
    }

    @GetMapping("/user/findAll")
    public List<UserDTO> GetUserList(){
        return userService.findAllUsers();
    }

    @GetMapping("/user/{userId}/find")
    public UserDTO GetUserInfo(@PathVariable("userId") Long userId){
        return userService.finUserDTO(userId);

    }

    @GetMapping("/user/{userId}/roomTickets/findAll")
    public List<RoomTicketDTO> GetUserTickets(@PathVariable("userId") Long userId){

        return userService.findRoomTicketsByUserId(userId);
    }

    @GetMapping("/user/{userId}/createdRoom")
    public List<RoomDTO> GetCreatedRoomByUserId(@PathVariable("userId") Long userId){
        return userService.findRoomByUserId(userId);
    }

}
