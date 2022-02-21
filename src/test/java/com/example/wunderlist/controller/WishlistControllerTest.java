package com.example.wunderlist.controller;

import com.example.wunderlist.model.Wishlist;
import com.example.wunderlist.service.TokenService;
import com.example.wunderlist.service.WishlistService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WishlistControllerTest {

    @InjectMocks
    private WishlistController wishlistController;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private TokenService jwtService;

    @Test
    public void shouldAddWishlist() throws Exception {
        String username = "username";
        Wishlist wishlist = Wishlist.builder().build();
        String authentication = "Bearer token";
        Set<Wishlist> wishlistSet = Collections.singleton(wishlist);

        when(wishlistService.createWishlistForUser(username, wishlist, authentication)).thenReturn(wishlistSet);

        ResponseEntity<?> result = wishlistController.addWishlist(username, wishlist, authentication);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldNotAddWishlistWhenAuthIsMissing() {
        String username = "username";
        Wishlist wishlist = Wishlist.builder().build();

        ResponseEntity<?> result = wishlistController.addWishlist(username, wishlist, null);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldNotAddWishlistWhenJwtNotValid() {
        String username = "username";
        String authentication = "Bearer token";
        Wishlist wishlist = Wishlist.builder().build();

        doThrow(IllegalStateException.class).when(jwtService).verifyAuthenticationHeader(authentication, username);

        ResponseEntity<?> result = wishlistController.addWishlist(username, wishlist, authentication);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldGetWishlist() throws Exception {
        String username = "username";
        Wishlist wishlist = Wishlist.builder().build();
        String authentication = "Bearer token";
        Set<Wishlist> wishlistSet = Collections.singleton(wishlist);

        when(wishlistService.getWishlistsForUser(username, authentication)).thenReturn(wishlistSet);

        ResponseEntity<?> result = wishlistController.getWishlist(username, authentication);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldNotGetWishlistWhenJwtNotValid() {
        String username = "username";
        String authentication = "Bearer token";

        doThrow(IllegalStateException.class).when(jwtService).verifyAuthenticationHeader(authentication, username);

        ResponseEntity<?> result = wishlistController.getWishlist(username, authentication);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

}