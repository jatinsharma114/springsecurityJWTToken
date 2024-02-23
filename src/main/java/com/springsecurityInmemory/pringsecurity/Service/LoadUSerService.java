package com.springsecurityInmemory.pringsecurity.Service;


import com.springsecurityInmemory.pringsecurity.Dao.UserRepository;
import com.springsecurityInmemory.pringsecurity.Entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoadUSerService implements LoadUSerServiceInterface{
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserInfo addUser(UserInfo userInfo) {
        userInfo.setUserPassword(passwordEncoder.encode(userInfo.getUserPassword()));
        return userRepository.save(userInfo);
    }



}
