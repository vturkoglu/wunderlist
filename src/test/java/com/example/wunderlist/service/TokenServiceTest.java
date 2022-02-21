package com.example.wunderlist.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", "UNSECURE_SECRET_TOKEN");
    }

    @Test
    public void shouldBuildToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoidXNlcm5hbWUifQ.meoZeht5KyDSk_L7IfU7nqq3exxV3UO06V8P_ktm6ZRkGTMXy0Nsc4co2WJoCGMhV5Zuvncm86UyqPbCFEaS8w";
        String result = tokenService.buildToken("username");
        assertThat(result, equalTo(token));
    }

    @Test
    public void shouldVerifyAuthenticationHeader() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoidXNlcm5hbWUifQ.meoZeht5KyDSk_L7IfU7nqq3exxV3UO06V8P_ktm6ZRkGTMXy0Nsc4co2WJoCGMhV5Zuvncm86UyqPbCFEaS8w";
        tokenService.verifyAuthenticationHeader("Bearer " + token, "username");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotVerifyAuthenticationHeader() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjoidXNlcm5hbWUifQ.";
        tokenService.verifyAuthenticationHeader("Bearer " + token, "username");
    }

}