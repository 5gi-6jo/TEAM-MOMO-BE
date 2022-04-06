package com.sparta.team6.momo.security.auth;

import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.DefaultException;
import com.sparta.team6.momo.model.User;
import com.sparta.team6.momo.model.UserRole;
import com.sparta.team6.momo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.sparta.team6.momo.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.sparta.team6.momo.model.UserRole.Role.USER;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       return  userRepository.findByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    private MoMoUser createUser(User user) {
        return new MoMoUser(user.getId(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(USER)));
    }
}
