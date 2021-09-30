package com.example.CrudCheckpointRound2.service;

import com.example.CrudCheckpointRound2.model.User;
import com.example.CrudCheckpointRound2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Object deleteByID(long id) {
        int count = 0;
        HashMap<String, Integer> countMap = new HashMap<>();

        this.userRepository.deleteById(id);
        Iterable<User> userList = this.userRepository.findAll();

        for(Object i : userList){
            count++;
        }
        countMap.put("count", count);
        return countMap;
    }

    public Object checkPassword(User user) {
        HashMap<String , Object> returnMap = new HashMap<>();
        if(this.userRepository.findByEmail(user.getEmail()).getPassword().equals(user.getPassword())){
            user.setId(this.userRepository.findByEmail(user.getEmail()).getId());
            returnMap.put("authenticated", true);
            returnMap.put("user", user);
        } else {
            returnMap.put("authenticated", false);
        }
        return returnMap;
    }
}
