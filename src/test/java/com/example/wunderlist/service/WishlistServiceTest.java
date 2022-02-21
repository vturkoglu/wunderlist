package com.example.wunderlist.service;

import com.example.wunderlist.model.User;
import com.example.wunderlist.model.Wishlist;
import com.example.wunderlist.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldCreateWishlists() throws Exception {
        String username = "username";
        String authentication = "token";
        Wishlist wishlist1 = Wishlist.builder().build();
        Wishlist wishlist2 = Wishlist.builder().build();
        Set<Wishlist> wishlistSet = new HashSet<>();
        wishlistSet.add(wishlist1);
        wishlistSet.add(wishlist2);
        User user = User.builder()
                .token("token")
                .wishlist(wishlistSet)
                .build();
        Optional<User> optional = Optional.of(user);

        when(userRepository.findById("_user::" + "username")).thenReturn(optional);

        Set<Wishlist> result = wishlistService.createWishlistForUser(username, wishlist2, authentication);

        assertThat(result, equalTo(wishlistSet));
    }

    @Test
    public void shouldGetWishlists() throws Exception {
        Wishlist wishlist = Wishlist.builder().build();
        Set<Wishlist> wishlistSet = Collections.singleton(wishlist);
        User user = User.builder()
                .token("token")
                .wishlist(wishlistSet)
                .build();
        Optional<User> optional = Optional.of(user);

        when(userRepository.findById("_user::" + "username")).thenReturn(optional);

        Set<Wishlist> result = wishlistService.getWishlistsForUser("username", "token");

        assertThat(result, equalTo(wishlistSet));
    }

    @Test(expected = Exception.class)
    public void shouldNotGetWishlistsWhenUserNotLoggedIn() throws Exception {
        Wishlist wishlist = Wishlist.builder().build();
        Set<Wishlist> wishlistSet = Collections.singleton(wishlist);
        User user = User.builder()
                .token("token")
                .wishlist(wishlistSet)
                .build();
        Optional<User> optional = Optional.of(user);

        when(userRepository.findById("_user::" + "username")).thenReturn(optional);

        wishlistService.getWishlistsForUser("username", "token2");
    }
}