package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "RoomTickets")
public class RoomTicketDomain {

    @Id@GeneratedValue
    @Column(name="roomTicket_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomTicketStateEnum ticketState;

    @OneToOne
    @JoinColumn(name = "question_result_id")
    private QuestionResultDomain questionResultDomain;

    @ManyToOne
    @JoinColumn(name = "user_id") //column table name
    private UserDomain user;

    @OneToOne
    @JoinColumn(name = "public_answer_group_id")
    private PublicAnswerGroupDomain publicAnswerGroup;


    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomDomain room;

    public List<PublicAnswerDomain> GetPublicAnswerDomain(){
        return publicAnswerGroup.getAnswers();
    }

}
