package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class PublicQuestionGroupDomain {

    @Id @GeneratedValue
    @Column(name="publicQuestionGroup_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum questionGroupType;

    @OneToMany(mappedBy = "publicQuestionGroup")
    private List<PublicQuestionDomain> questions = new ArrayList<>();
}
