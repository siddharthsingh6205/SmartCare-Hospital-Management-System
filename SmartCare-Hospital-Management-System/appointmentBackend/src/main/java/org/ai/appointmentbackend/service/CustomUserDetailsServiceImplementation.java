package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImplementation implements UserDetailsService {

@Autowired
private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
UserDetails userDetails= (UserDetails) userRepository.findByEmail(username);
if (userDetails==null){
throw new UsernameNotFoundException("The user does not exist"+username);
}
return userDetails;

    }

    };

