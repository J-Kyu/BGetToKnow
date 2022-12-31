package com.kyu.BGetToKnowYou.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CategoryResultDomain {

    @Id @GeneratedValue
    @Column(name="category_result_id")
    private long id;

    private float averageScore;

    @Enumerated(EnumType.STRING)
    private QuestionCategoryEnum questionCategory;

    @ManyToOne
    @JoinColumn(name = "question_result_id")
    private QuestionResultDomain questionResultDomain;

}
