package com.example.wunderlist.service;

import com.example.wunderlist.model.User;
import com.example.wunderlist.model.Wishlist;
import com.example.wunderlist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class WishlistService {

    private final UserRepository userRepository;

    public Set<Wishlist> createWishlistForUser(String username, Wishlist wishlist, String authentication) throws Exception {
        User user = getUser(username, authentication);
        Set<Wishlist> wishlistList = new HashSet<>();
        wishlistList.add(wishlist);

        if (Objects.nonNull(user.getWishlist())) {
            wishlistList.addAll(user.getWishlist());
        }

        user.setWishlist(wishlistList);
        userRepository.save(user);
        return wishlistList;
    }

    public Set<Wishlist> getWishlistsForUser(String username, String authentication) throws Exception {
        User user = getUser(username, authentication);
        return user.getWishlist();
    }

    private User getUser(String username, String authentication) throws Exception {
        Optional<User> optional = userRepository.findById("_user::" + username);
        if (!optional.isPresent()) {
            throw new IllegalStateException();
        }

        if (!optional.get().getToken().equals(authentication.replaceFirst("Bearer ", ""))) {
            throw new Exception("You must login!");
        }
        return optional.get();
    }

}
