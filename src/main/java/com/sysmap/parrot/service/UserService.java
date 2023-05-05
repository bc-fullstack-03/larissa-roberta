package com.sysmap.parrot.service;

import com.sysmap.parrot.dto.request.UserRequest;
import com.sysmap.parrot.model.User;
import com.sysmap.parrot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;
    public String createUser(UserRequest request){
        var user = new User(request.getName(), request.getEmail(), request.getPassword());
        repository.save(user);
        return user.getId().toString();
    }
}
