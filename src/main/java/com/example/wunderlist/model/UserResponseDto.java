package com.example.wunderlist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto extends RepresentationModel<UserResponseDto> {

    private String username;

    private String token;

    private List<Wishlist> wishlist;

}
