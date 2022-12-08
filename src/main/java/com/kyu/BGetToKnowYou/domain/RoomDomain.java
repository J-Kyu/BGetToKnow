package com.kyu.BGetToKnowYou.domain;

import com.kyu.BGetToKnowYou.DTO.RoomDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class RoomDomain {
    public RoomDomain(RoomDTO room){
        this.code = room.getCode();
        this.roomState = room.getRoomState();
        this.roomType = room.getRoomType();
        this.maxNum = room.getMaxNum();
    }

    public RoomDomain(){

    }

    @Id @GeneratedValue
    @Column(name="room_id")
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private RoomStateEnum roomState;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    private int maxNum;

    @ManyToOne
    @JoinColumn(name = "admin_user_id")
    private UserDomain adminUser;

    @OneToOne
    @JoinColumn( name = "question_group_id")
    private PublicQuestionGroupDomain questionGroup;


    @OneToMany(mappedBy = "room")
    private List<RoomTicketDomain> roomTickets = new ArrayList<>();

}
