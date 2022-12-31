package com.kyu.BGetToKnowYou.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class QuestionResultDomain {

    @Id @GeneratedValue
    @Column(name="question_result_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionResultType questionResultType;

    @OneToMany(mappedBy = "questionResultDomain")
    List<CategoryResultDomain> categoryResultList = new ArrayList<>();


}
