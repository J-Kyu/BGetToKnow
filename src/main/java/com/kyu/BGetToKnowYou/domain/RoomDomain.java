package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class RoomDomain {

    @Id @GeneratedValue
    @Column(name="room_id")
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private RoomStateEnum roomState;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    private int maxNum;


}
