package com.jagdeep.princeurbanknot.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.jagdeep.princeurbanknot.model.User;
import com.jagdeep.princeurbanknot.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
