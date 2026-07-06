package com.campus.trade;

import com.campus.trade.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class CampusTradeApplicationTests {

    @Test
    void passwordEncoderMatchesRawPassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("123456");
        assertThat(encoder.matches("123456", encoded)).isTrue();
    }

    @Test
    void contextRunnerCanCreateJwtProvider() {
        new ApplicationContextRunner()
                .withBean(JwtTokenProvider.class,
                        () -> new JwtTokenProvider("CampusSecondHandTradingPlatformJwtSecretKeyForLocalDevelopmentOnly2026", 60))
                .run(context -> assertThat(context).hasSingleBean(JwtTokenProvider.class));
    }
}
