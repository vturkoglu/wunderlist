package com.example.wunderlist.controller;

import com.example.wunderlist.assembler.UserResourceAssembler;
import com.example.wunderlist.model.User;
import com.example.wunderlist.model.UserRequestDto;
import com.example.wunderlist.model.UserResponseDto;
import com.example.wunderlist.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserResourceAssembler userResourceAssembler;

    @Test
    public void shouldRegister() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();
        User user = User.builder().build();
        UserResponseDto userResponseDto = UserResponseDto.builder().build();

        when(userService.registerUser(userRequestDto)).thenReturn(user);
        when(userResourceAssembler.toModel(user)).thenReturn(userResponseDto);

        ResponseEntity<?> result = userController.register(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void shouldNotRegisterWhenAuthenticationExceptionOccurs() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();
        User user = User.builder().build();
        UserResponseDto userResponseDto = UserResponseDto.builder().build();

        when(userService.registerUser(userRequestDto)).thenThrow(AuthenticationServiceException.class);
        when(userResourceAssembler.toModel(user)).thenReturn(userResponseDto);

        ResponseEntity<?> result = userController.register(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldNotRegisterWhenAnyExceptionOccurs() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();
        User user = User.builder().build();
        UserResponseDto userResponseDto = UserResponseDto.builder().build();

        when(userService.registerUser(userRequestDto)).thenThrow(Exception.class);
        when(userResourceAssembler.toModel(user)).thenReturn(userResponseDto);

        ResponseEntity<?> result = userController.register(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldLogin() {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();
        String token = "token";

        when(userService.login(userRequestDto)).thenReturn(token);

        ResponseEntity<?> result = userController.login(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldNotLoginWhenExceptionOccurs() {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();

        when(userService.login(userRequestDto)).thenThrow(AuthenticationCredentialsNotFoundException.class);

        ResponseEntity<?> result = userController.login(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldLogout() {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();

        ResponseEntity<?> result = userController.logout(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldNotLogoutWhenExceptionOccurs() {
        UserRequestDto userRequestDto = UserRequestDto.builder().build();

        doThrow(AuthenticationCredentialsNotFoundException.class).when(userService).logout(userRequestDto);

        ResponseEntity<?> result = userController.logout(userRequestDto);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

}