package com.kyu.BGetToKnowYou.DTO;

import com.kyu.BGetToKnowYou.domain.QuestionResultType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionResultDTO {


    private String userNickname;
    @Enumerated(EnumType.STRING)
    private QuestionResultType questionResultType;

    private List<CategoryResultDTO> categoryResultDTOList = new ArrayList<>();


}
