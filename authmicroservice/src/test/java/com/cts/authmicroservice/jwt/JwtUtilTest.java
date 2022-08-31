package com.cts.authmicroservice.jwt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JwtUtil.class})
@ExtendWith(SpringExtension.class)
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testGenerateToken() {
        JwtUtil jwtUtil = new JwtUtil();
        
    }

    @Test
    void testValidateToken() {
        assertFalse(this.jwtUtil.validateToken("ABC123"));
        assertFalse(this.jwtUtil.validateToken("${jwt.secret}"));
        assertFalse(this.jwtUtil.validateToken(""));
        assertFalse(this.jwtUtil.validateToken("${jwt.secret}${jwt.secret}"));
        assertFalse(this.jwtUtil.validateToken("${jwt.secret}${jwt.secret}${jwt.secret}"));
    }
}

