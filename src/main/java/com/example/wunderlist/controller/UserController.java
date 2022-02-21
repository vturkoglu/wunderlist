package com.example.wunderlist.controller;

import com.example.wunderlist.assembler.UserResourceAssembler;
import com.example.wunderlist.model.User;
import com.example.wunderlist.model.UserRequestDto;
import com.example.wunderlist.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(UserController.ENDPOINT)
public class UserController {

    public static final String ENDPOINT = "/api/user";

    private final UserService userService;
    private final UserResourceAssembler userResourceAssembler;

    @PostMapping("/signup")
    @ApiOperation(
            value = "For creating user",
            nickname = "create user",
            notes = "You can create a user by using this method"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 409, message = "User exist"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) {
        try {
            User user = userService.registerUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResourceAssembler.toModel(user));
        } catch (AuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @ApiOperation(
            value = "For user to login",
            nickname = "login user",
            notes = "You can login by using this method"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User logged in"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        try {
            return ResponseEntity.ok(userService.login(userRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @ApiOperation(
            value = "For user to logout",
            nickname = "logout user",
            notes = "You can logout by using this method"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User logged out"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<?> logout(@RequestBody UserRequestDto userRequestDto) {
        try {
            userService.logout(userRequestDto);
            return ResponseEntity.ok(userRequestDto.getUsername() + "logged out!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}