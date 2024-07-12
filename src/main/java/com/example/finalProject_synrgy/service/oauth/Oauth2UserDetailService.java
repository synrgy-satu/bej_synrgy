package com.example.finalProject_synrgy.service.oauth;

import com.example.finalProject_synrgy.entity.oauth2.User;
import com.example.finalProject_synrgy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class Oauth2UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("Username %s is not found", s));
        }

        return user;
    }

}
