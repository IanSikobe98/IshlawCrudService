package com.ishlaw.crudservice.service;

import com.ishlaw.crudservice.entity.StaffDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    CrudTransactionsService crudTransactionsService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("GETTING USER PASSED ########" + username);
        StaffDetails user = crudTransactionsService.findStaffByMsisdn(username);
        LOGGER.info("User : " + user);
        if (user == null) {
            LOGGER.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassword(),new ArrayList<>());
    }
}