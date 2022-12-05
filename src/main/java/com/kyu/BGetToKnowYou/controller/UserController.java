package com.kyu.BGetToKnowYou.controller;


import com.kyu.BGetToKnowYou.domain.OAuthTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
        log.info(user.getNickname());
        userService.join(user);

        return "redirect:/";
    }

    @GetMapping("/users")
    public List<UserDomain> GetUserList(){
        List<UserDomain> users = userService.findMembers();

        return users;
    }


}
