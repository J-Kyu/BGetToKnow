package com.kyu.BGetToKnowYou.controller;

import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.service.RoomService;
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
public class RoomController {

    private final RoomService roomService;

    @PostMapping(value = "/room/new")
    public String create(@Valid RoomForm form, BindingResult result){

        if (result.hasErrors()){
            return "redirect:/";
        }

        RoomDomain room = new RoomDomain();
        room.setRoomState(RoomStateEnum.PRE_MEETING);
        room.setRoomType(form.getRoomType());
        room.setMaxNum(form.getMaxNum());

        String roomCode = roomService.join(room);

        return roomCode;
    }

    @GetMapping("/room/find")
    public RoomDomain GetRoomByCode(String roomCode){
        RoomDomain room = roomService.findRoomByCode(roomCode);
        return room;
    }

    @GetMapping("/room/findAll")
    public List<RoomDomain> GetAllRoom(){
        return roomService.findRooms();
    }




}
