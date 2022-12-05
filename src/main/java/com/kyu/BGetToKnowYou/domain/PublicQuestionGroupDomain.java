package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PublicQuestionGroupDomain {

    @Id @GeneratedValue
    @Column(name="publicQuestionGroup_id")
    private Long id;
}
