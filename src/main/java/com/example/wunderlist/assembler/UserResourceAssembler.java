package com.example.wunderlist.assembler;

import com.example.wunderlist.model.User;
import com.example.wunderlist.model.UserResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;


@Component
public class UserResourceAssembler extends RepresentationModelAssemblerSupport<User, UserResponseDto> {

    private final ModelMapper modelMapper;

    public UserResourceAssembler(ModelMapper modelMapper) {
        super(User.class, UserResponseDto.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto toModel(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
