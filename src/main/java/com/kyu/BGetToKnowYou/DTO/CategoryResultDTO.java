package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.QuestionCategoryEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class CategoryResultDTO {

    private float averageScore;

    @Enumerated(EnumType.STRING)
    private QuestionCategoryEnum questionCategory;
}
