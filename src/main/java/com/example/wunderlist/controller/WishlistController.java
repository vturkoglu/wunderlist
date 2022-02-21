package com.example.wunderlist.controller;

import com.example.wunderlist.model.Wishlist;
import com.example.wunderlist.service.TokenService;
import com.example.wunderlist.service.WishlistService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(WishlistController.ENDPOINT)
public class WishlistController {

    public static final String ENDPOINT = "/api/wishlist";

    private final WishlistService wishlistService;
    private final TokenService jwtService;

    @PostMapping("/{username}/add")
    @ApiOperation(
            value = "For creating user's wishlist",
            nickname = "create user's wishlist",
            notes = "You can create user's wishlist by using this method"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's wishlist created"),
            @ApiResponse(code = 403, message = "Forbidden to access this resource"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<?> addWishlist(
            @PathVariable String username,
            @RequestBody Wishlist wishlist,
            @RequestHeader("Authentication") String authentication
    ) {
        if (authentication == null || !authentication.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bearer Authentication must be used");
        }

        try {
            jwtService.verifyAuthenticationHeader(authentication, username);
            Set<Wishlist> result = wishlistService.createWishlistForUser(username, wishlist, authentication);
            return ResponseEntity.ok().body(result);
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden, you can't create wishlist for this user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{username}/get")
    @ApiOperation(
            value = "For getting user's wishlist",
            nickname = "get user's wishlist",
            notes = "You can get user's wishlist by using this method"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's wishlist get"),
            @ApiResponse(code = 403, message = "Forbidden to access this resource"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<?> getWishlist(
            @PathVariable String username,
            @RequestHeader("Authentication") String authentication
    ) {
        if (authentication == null || !authentication.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bearer Authentication must be used");
        }

        try {
            jwtService.verifyAuthenticationHeader(authentication, username);
            Set<Wishlist> wishlists = wishlistService.getWishlistsForUser(username, authentication);
            return ResponseEntity.ok(wishlists);
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden, you don't have access to this wishlist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}