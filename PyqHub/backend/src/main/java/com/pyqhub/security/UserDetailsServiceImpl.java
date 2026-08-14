package com.pyqhub.security;

import com.pyqhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String collegeId) throws UsernameNotFoundException {
        return userRepository.findByCollegeId(collegeId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with college ID: " + collegeId));
    }
}
