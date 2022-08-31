package com.cts.authmicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cts.authmicroservice.exception.UnauthorizedException;
import com.cts.authmicroservice.jwt.JwtUtil;
import com.cts.authmicroservice.model.AuthResponse;
import com.cts.authmicroservice.model.UserModel;
import com.cts.authmicroservice.model.UserToken;
import com.cts.authmicroservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    void testLoadUserByUsername() {
        UserModel userModel = new UserModel();
        userModel.setEmpid(1);
        userModel.setEmpUsername("janedoe");
        userModel.setEmpPassword("iloveyou");
        when(this.userRepository.findByEmpUsername((String) any())).thenReturn(userModel);
        UserDetails actualLoadUserByUsernameResult = this.userServiceImpl.loadUserByUsername("janedoe");
        assertTrue(actualLoadUserByUsernameResult.getAuthorities().isEmpty());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertTrue(actualLoadUserByUsernameResult.isCredentialsNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonLocked());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonExpired());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        verify(this.userRepository).findByEmpUsername((String) any());
    }

    @Test
    void testLogin() {
        UserModel userModel = new UserModel();
        userModel.setEmpid(1);
        userModel.setEmpUsername("janedoe");
        userModel.setEmpPassword("iloveyou");
        when(this.userRepository.findByEmpUsername((String) any())).thenReturn(userModel);
        when(this.jwtUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) any()))
                .thenReturn("ABC123");

        UserModel userModel1 = new UserModel();
        userModel1.setEmpid(1);
        userModel1.setEmpUsername("janedoe");
        userModel1.setEmpPassword("iloveyou");
        UserToken actualLoginResult = this.userServiceImpl.login(userModel1);
        assertEquals("ABC123", actualLoginResult.getAuthToken());
        assertEquals("janedoe", actualLoginResult.getUsername());
        assertEquals(1, actualLoginResult.getEmpid());
        verify(this.userRepository, atLeast(1)).findByEmpUsername((String) any());
        verify(this.jwtUtil).generateToken((org.springframework.security.core.userdetails.UserDetails) any());
    }

    @Test
    void testLogin2() {
        UserModel userModel = new UserModel();
        userModel.setEmpid(1);
        userModel.setEmpUsername("janedoe");
        userModel.setEmpPassword("Inside loadbyusername");
        when(this.userRepository.findByEmpUsername((String) any())).thenReturn(userModel);
        when(this.jwtUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) any()))
                .thenReturn("ABC123");

        UserModel userModel1 = new UserModel();
        userModel1.setEmpid(1);
        userModel1.setEmpUsername("janedoe");
        userModel1.setEmpPassword("iloveyou");
        assertThrows(UnauthorizedException.class, () -> this.userServiceImpl.login(userModel1));
        verify(this.userRepository).findByEmpUsername((String) any());
    }

    @Test
    void testGetValidity() {
        UserModel userModel = new UserModel();
        userModel.setEmpid(1);
        userModel.setEmpUsername("janedoe");
        userModel.setEmpPassword("iloveyou");
        when(this.userRepository.findByEmpUsername((String) any())).thenReturn(userModel);
        when(this.jwtUtil.extractUsername((String) any())).thenReturn("janedoe");
        when(this.jwtUtil.validateToken((String) any())).thenReturn(true);
        AuthResponse actualValidity = this.userServiceImpl.getValidity("com.cts.service.UserServiceImpl");
        assertEquals(1, actualValidity.getEmpid());
        assertTrue(actualValidity.isValid());
        assertEquals("janedoe", actualValidity.getUsername());
        verify(this.userRepository).findByEmpUsername((String) any());
        verify(this.jwtUtil).extractUsername((String) any());
        verify(this.jwtUtil).validateToken((String) any());
    }

    @Test
    void testGetValidity2() {
        UserModel userModel = new UserModel();
        userModel.setEmpid(1);
        userModel.setEmpUsername("janedoe");
        userModel.setEmpPassword("iloveyou");
        when(this.userRepository.findByEmpUsername((String) any())).thenReturn(userModel);
        when(this.jwtUtil.extractUsername((String) any())).thenReturn("janedoe");
        when(this.jwtUtil.validateToken((String) any())).thenReturn(false);
        assertFalse(this.userServiceImpl.getValidity("com.cts.service.UserServiceImpl").isValid());
        verify(this.jwtUtil).validateToken((String) any());
    }
}

