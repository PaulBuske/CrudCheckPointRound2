package com.example.CrudCheckpointRound2.controller;

import com.example.CrudCheckpointRound2.Views;
import com.example.CrudCheckpointRound2.model.User;
import com.example.CrudCheckpointRound2.repository.UserRepository;
import com.example.CrudCheckpointRound2.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserRepository userRepository;
    UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public Iterable<User> returnsAllUsersAsAnIterableList(){
        return this.userRepository.findAll();
    }

    @PostMapping("")
    @JsonView(Views.LimitedView.class)
    public Iterable<User> postAIterableOfUsers(@RequestBody Iterable<User> users){
        return this.userRepository.saveAll(users);
    }

    @GetMapping("{id}")
    @JsonView(Views.LimitedView.class)
    public Object getWithIdPathVariableReturnsSpecifiedUser(@PathVariable long id){
        if(this.userRepository.findById(id).isEmpty()){
            return id + " not found. Please contact your administrator";
        } else return this.userRepository.findById(id);
    }

    @DeleteMapping("{id}")
    public Object deleteMessageReturnsASpecificRowAndReturnsRemainingRows(@PathVariable long id){
        if(this.userRepository.findById(id).isEmpty()){
            return id + " not in database.";
        } else return userService.deleteByID(id);
    }

    @PostMapping("/authenticate")
    @JsonView(Views.LimitedView.class)
    public Object checkToSeeIfPasswordsMatch(@RequestBody User user){
        if(this.userRepository.existsAllByEmail(user.getEmail())){
           return userService.checkPassword(user);
        } else return user.getEmail().toString() + "does not exist in the databse";
    }
}
