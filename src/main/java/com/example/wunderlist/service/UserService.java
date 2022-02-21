package com.example.wunderlist.service;

import com.example.wunderlist.model.User;
import com.example.wunderlist.model.UserRequestDto;
import com.example.wunderlist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public User registerUser(UserRequestDto userRequestDto) throws Exception {
        String username = userRequestDto.getUsername();
        String passHash = BCrypt.hashpw(userRequestDto.getPassword(), BCrypt.gensalt());

        Optional<User> optional = userRepository.findById("_user::" + username);
        if (optional.isPresent()) {
            throw new Exception(username + "is exist! Please use another username!");
        }

        try {
            String jwt = tokenService.buildToken(username);
            User user = User.builder()
                    .id("_user::" + username)
                    .username(username)
                    .password(passHash)
                    .token(jwt)
                    .build();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new AuthenticationServiceException("There was an error creating account");
        }
    }

    public String login(UserRequestDto userRequestDto) {
        checkUsernameAndPassword(userRequestDto);
        Optional<User> optional = userRepository.findById("_user::" + userRequestDto.getUsername());
        if (!optional.isPresent()) {
            throw new AuthenticationCredentialsNotFoundException("Bad Username or Password");
        } else if (BCrypt.checkpw(userRequestDto.getPassword(), optional.get().getPassword())) {
            User user = optional.get();
            String token = tokenService.buildToken(userRequestDto.getUsername());
            if (ObjectUtils.isEmpty(user.getToken())) {
                user.setToken(token);
                userRepository.save(user);
            }
            return token;
        } else {
            throw new AuthenticationCredentialsNotFoundException("Bad Username or Password");
        }
    }

    public void logout(UserRequestDto userRequestDto) {
        checkUsernameAndPassword(userRequestDto);
        Optional<User> optional = userRepository.findById("_user::" + userRequestDto.getUsername());
        if (!optional.isPresent()) {
            throw new AuthenticationCredentialsNotFoundException("Bad Username or Password");
        }

        User user = optional.get();
        user.setToken("");
        userRepository.save(user);
    }

    private void checkUsernameAndPassword(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        if (username == null || password == null) {
            throw new BadCredentialsException("Username or Password required for login!");
        }
    }

}