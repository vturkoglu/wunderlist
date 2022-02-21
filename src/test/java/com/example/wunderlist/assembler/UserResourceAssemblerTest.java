package com.example.wunderlist.assembler;

import com.example.wunderlist.model.User;
import com.example.wunderlist.model.UserResponseDto;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceAssemblerTest {

    @InjectMocks
    private UserResourceAssembler userResourceAssembler;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void shouldConvertToModel() {
        User user = User.builder()
                .id("id")
                .token("token")
                .username("username")
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .token("token")
                .username("username")
                .build();
        Mockito.when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);
        UserResponseDto result = userResourceAssembler.toModel(user);

        MatcherAssert.assertThat(result.getUsername(), CoreMatchers.equalTo(user.getUsername()));
        MatcherAssert.assertThat(result.getToken(), CoreMatchers.equalTo(user.getToken()));
    }
}