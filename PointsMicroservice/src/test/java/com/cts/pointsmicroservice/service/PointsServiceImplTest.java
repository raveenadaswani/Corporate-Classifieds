package com.cts.pointsmicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cts.pointsmicroservice.client.AuthClient;
import com.cts.pointsmicroservice.client.EmployeeClient;
import com.cts.pointsmicroservice.client.OfferClient;
import com.cts.pointsmicroservice.exception.InvalidUserException;
import com.cts.pointsmicroservice.exception.MicroserviceException;
import com.cts.pointsmicroservice.model.AuthResponse;
import com.cts.pointsmicroservice.model.MessageResponse;
import com.cts.pointsmicroservice.model.Offer;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {PointsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class PointsServiceImplTest {
    @MockBean
    private AuthClient authClient;

    @MockBean
    private EmployeeClient employeeClient;

    @MockBean
    private MessageResponse messageResponse;

    @MockBean
    private OfferClient offerClient;

    @Autowired
    private PointsServiceImpl pointsServiceImpl;

    @Test
    void testGetPoints() throws InvalidUserException, MicroserviceException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.pointsServiceImpl.getPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPoints2() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getPointsById((String) any(), anyInt())).thenReturn(123);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertEquals(123, this.pointsServiceImpl.getPoints("ABC123", 1).intValue());
        verify(this.offerClient).getPointsById((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPoints3() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getPointsById((String) any(), anyInt())).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(MicroserviceException.class, () -> this.pointsServiceImpl.getPoints("ABC123", 1));
        verify(this.offerClient).getPointsById((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPoints4() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getPointsById((String) any(), anyInt())).thenReturn(123);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.pointsServiceImpl.getPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPoints5() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getPointsById((String) any(), anyInt())).thenReturn(123);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse(1, "janedoe", true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertEquals(123, this.pointsServiceImpl.getPoints("ABC123", 1).intValue());
        verify(this.offerClient).getPointsById((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetPoints6() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getPointsById((String) any(), anyInt())).thenReturn(123);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(MicroserviceException.class, () -> this.pointsServiceImpl.getPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testRefreshPoints() throws InvalidUserException, MicroserviceException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.pointsServiceImpl.refreshPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testRefreshPoints2() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        this.pointsServiceImpl.refreshPoints("ABC123", 1);
        verify(this.offerClient).getOfferByEmpId((String) any(), anyInt());
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((java.util.Date) any());
        verify(this.employeeClient).savePoints((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testRefreshPoints3() throws InvalidUserException, MicroserviceException {
        ArrayList<Offer> offerList = new ArrayList<>();
        Date openDate = new Date(1L);
        Date engagedDate = new Date(1L);
        offerList.add(new Offer(1, "Name", "The characteristics of someone or something", "Category", openDate, engagedDate,
                new Date(1L), 1));
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(offerList);
        doNothing().when(this.messageResponse).setTimeStamp((Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        this.pointsServiceImpl.refreshPoints("ABC123", 1);
        verify(this.offerClient).getOfferByEmpId((String) any(), anyInt());
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((Date) any());
        verify(this.employeeClient).savePoints((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testRefreshPoints4() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn((ResponseEntity<MessageResponse>) mock(ResponseEntity.class));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
     //   assertThrows(MicroserviceException.class, () -> this.pointsServiceImpl.refreshPoints("ABC123", 1));
     //   verify(this.offerClient).getOfferByEmpId((String) any(), anyInt());
    //    verify(this.employeeClient).savePoints((String) any(), anyInt());
    //    verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testRefreshPoints5() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(123, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.pointsServiceImpl.refreshPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testRefreshPoints6() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.pointsServiceImpl.refreshPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testRefreshPoints7() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        this.pointsServiceImpl.refreshPoints("ABC123", 1);
        verify(this.offerClient).getOfferByEmpId((String) any(), anyInt());
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((java.util.Date) any());
        verify(this.employeeClient).savePoints((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(authResponse).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testRefreshPoints8() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse(1, "janedoe", true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        this.pointsServiceImpl.refreshPoints("ABC123", 1);
        verify(this.offerClient).getOfferByEmpId((String) any(), anyInt());
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((java.util.Date) any());
        verify(this.employeeClient).savePoints((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testRefreshPoints9() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOfferByEmpId((String) any(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());
        when(this.employeeClient.savePoints((String) any(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(MicroserviceException.class, () -> this.pointsServiceImpl.refreshPoints("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }
}

