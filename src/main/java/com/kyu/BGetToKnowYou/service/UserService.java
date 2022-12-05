package com.kyu.BGetToKnowYou.service;

import com.kyu.BGetToKnowYou.domain.UserDomain;
import com.kyu.BGetToKnowYou.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(UserDomain user){
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }


    private void validateDuplicateUser(UserDomain user){
        //EXCEPTION
        List<UserDomain> findMembers = userRepository.findByNickname(user.getNickname());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<UserDomain> findMembers(){return userRepository.findAll();}

    public UserDomain findOne(Long userId) {return userRepository.findOne(userId);}

}
