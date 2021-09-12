package com.example.dogeville.service;

import com.example.dogeville.model.Wingman;
import com.example.dogeville.repository.wingmanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private wingmanRepository repository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Wingman wm = repository.findByWmmail(mail);
        return new org.springframework.security.core.userdetails.User(wm.getWmmail(), wm.getWmpw(), new ArrayList<>());
    }
}
