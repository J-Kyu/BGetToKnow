package com.kyu.BGetToKnowYou.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kyu.BGetToKnowYou.domain.RoomDomain;
import com.kyu.BGetToKnowYou.domain.RoomStateEnum;
import com.kyu.BGetToKnowYou.domain.RoomTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RoomDTO {

    public RoomDTO(int maxNum, RoomTypeEnum roomType, String releaseDateTime){
        this.roomState = RoomStateEnum.PRE_MEETING;
        this.roomType = roomType;
        this.maxNum = maxNum;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        this.releaseDateTime = LocalDateTime.parse(releaseDateTime, formatter);
    }

    public RoomDTO(){
        this.roomState = RoomStateEnum.PRE_MEETING;
        this.roomType = RoomTypeEnum.TYPE_A;
        this.maxNum = 10;
    }

    public RoomDTO(RoomDomain room){
        this.id = room.getId();
        this.code = room.getCode();
        this.roomState = room.getRoomState();
        this.roomType = room.getRoomType();
        this.maxNum = room.getMaxNum();
        this.releaseDateTime =  room.getReleaseDateTime();
    }

   @JsonIgnore
   private Long id;
    private String code;

    @Enumerated(EnumType.STRING)
    private RoomStateEnum roomState;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    private LocalDateTime releaseDateTime;

    private int maxNum;


}
