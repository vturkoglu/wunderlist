package com.example.wunderlist.service;

import com.example.wunderlist.model.User;
import com.example.wunderlist.model.UserRequestDto;
import com.example.wunderlist.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldRegister() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        User user = User.builder()
                .token("token")
                .username("username")
                .build();

        when(userRepository.findById("_user::" + "username")).thenReturn(Optional.empty());
        when(tokenService.buildToken("username")).thenReturn("token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(userRequestDto);

        assertThat(result.getUsername(), equalTo(userRequestDto.getUsername()));
    }

    @Test(expected = Exception.class)
    public void shouldNotRegisterWhenUserIsExist() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        User user = User.builder()
                .token("token")
                .username("username")
                .build();

        when(userRepository.findById("_user::" + "username")).thenReturn(Optional.of(user));

        userService.registerUser(userRequestDto);
    }

    @Test
    public void shouldLogin() {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        User user = User.builder()
                .username("username")
                .password("$2a$10$tPJnYdq9kUbzZ6wORLoPA.Rnb6LklVtKGdt7hgdf8V6ZSxrpI6Hiy")
                .build();

        when(userRepository.findById("_user::" + "username")).thenReturn(Optional.of(user));
        when(tokenService.buildToken("username")).thenReturn("token");

        String result = userService.login(userRequestDto);

        assertThat(result, equalTo(user.getToken()));
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldNotLoginWhenUserOrPassIsNull() {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username("username")
                .build();
        User user = User.builder()
                .username("username")
                .password("$2a$10$tPJnYdq9kUbzZ6wORLoPA.Rnb6LklVtKGdt7hgdf8V6ZSxrpI6Hiy")
                .build();

        when(userRepository.findById("_user::" + "username")).thenReturn(Optional.of(user));

        userService.login(userRequestDto);
    }

    @Test
    public void shouldLogout() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        User user = User.builder()
                .username("username")
                .password("$2a$10$tPJnYdq9kUbzZ6wORLoPA.Rnb6LklVtKGdt7hgdf8V6ZSxrpI6Hiy")
                .build();

        when(userRepository.findById("_user::" + "username")).thenReturn(Optional.of(user));

        userService.logout(userRequestDto);
    }

}