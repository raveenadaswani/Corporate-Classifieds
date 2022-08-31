package com.cts.pointsmicroservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.cts.pointsmicroservice.exception.InvalidUserException;
import com.cts.pointsmicroservice.exception.MicroserviceException;
import com.cts.pointsmicroservice.model.MessageResponse;
import feign.FeignException;

import java.net.ConnectException;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PointsExceptionHandlerTest {
    @Test
    void testHandleUserNotFoundException() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<?> actualHandleUserNotFoundExceptionResult = pointsExceptionHandler
                .handleUserNotFoundException(new NullPointerException("foo"));
        assertTrue(actualHandleUserNotFoundExceptionResult.hasBody());
        assertTrue(actualHandleUserNotFoundExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleUserNotFoundExceptionResult.getStatusCode());
        assertEquals("Employee Details not Found",
                ((MessageResponse) actualHandleUserNotFoundExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED,
                ((MessageResponse) actualHandleUserNotFoundExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleStringIndexOutOfBoundException() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<?> actualHandleStringIndexOutOfBoundExceptionResult = pointsExceptionHandler
                .handleStringIndexOutOfBoundException(new StringIndexOutOfBoundsException("foo"));
        assertTrue(actualHandleStringIndexOutOfBoundExceptionResult.hasBody());
        assertTrue(actualHandleStringIndexOutOfBoundExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleStringIndexOutOfBoundExceptionResult.getStatusCode());
        assertEquals("Not a valid token",
                ((MessageResponse) actualHandleStringIndexOutOfBoundExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED,
                ((MessageResponse) actualHandleStringIndexOutOfBoundExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleFeignException() {
        ResponseEntity<?> actualHandleFeignExceptionResult = (new PointsExceptionHandler())
                .handleFeignException(mock(FeignException.class));
        assertTrue(actualHandleFeignExceptionResult.hasBody());
        assertTrue(actualHandleFeignExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleFeignExceptionResult.getStatusCode());
        assertEquals("Service Unavailable", ((MessageResponse) actualHandleFeignExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                ((MessageResponse) actualHandleFeignExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleEmptyResultDataAccessException() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<?> actualHandleEmptyResultDataAccessExceptionResult = pointsExceptionHandler
                .handleEmptyResultDataAccessException(new EmptyResultDataAccessException(3));
        assertTrue(actualHandleEmptyResultDataAccessExceptionResult.hasBody());
        assertTrue(actualHandleEmptyResultDataAccessExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleEmptyResultDataAccessExceptionResult.getStatusCode());
        assertEquals("ID cannot exist",
                ((MessageResponse) actualHandleEmptyResultDataAccessExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                ((MessageResponse) actualHandleEmptyResultDataAccessExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleNoSuchElementException() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<?> actualHandleNoSuchElementExceptionResult = pointsExceptionHandler
                .handleNoSuchElementException(new NoSuchElementException("foo"));
        assertTrue(actualHandleNoSuchElementExceptionResult.hasBody());
        assertTrue(actualHandleNoSuchElementExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleNoSuchElementExceptionResult.getStatusCode());
        assertEquals("ID cannot exist",
                ((MessageResponse) actualHandleNoSuchElementExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED,
                ((MessageResponse) actualHandleNoSuchElementExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleServiceDownException() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<?> actualHandleServiceDownExceptionResult = pointsExceptionHandler
                .handleServiceDownException(new ConnectException("foo"));
        assertTrue(actualHandleServiceDownExceptionResult.hasBody());
        assertTrue(actualHandleServiceDownExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleServiceDownExceptionResult.getStatusCode());
        assertEquals("Check your Connection",
                ((MessageResponse) actualHandleServiceDownExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                ((MessageResponse) actualHandleServiceDownExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleInvalidUserException() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<?> actualHandleInvalidUserExceptionResult = pointsExceptionHandler
                .handleInvalidUserException(new InvalidUserException("Msg"));
        assertTrue(actualHandleInvalidUserExceptionResult.hasBody());
        assertTrue(actualHandleInvalidUserExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleInvalidUserExceptionResult.getStatusCode());
        assertEquals("Msg", ((MessageResponse) actualHandleInvalidUserExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED,
                ((MessageResponse) actualHandleInvalidUserExceptionResult.getBody()).getStatus());
    }

    @Test
    void testHandleMicorserviceError() {
        PointsExceptionHandler pointsExceptionHandler = new PointsExceptionHandler();
        ResponseEntity<MessageResponse> actualHandleMicorserviceErrorResult = pointsExceptionHandler
                .handleMicorserviceError(new MicroserviceException("An error occurred"));
        assertTrue(actualHandleMicorserviceErrorResult.getHeaders().isEmpty());
        assertTrue(actualHandleMicorserviceErrorResult.hasBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualHandleMicorserviceErrorResult.getStatusCode());
        MessageResponse body = actualHandleMicorserviceErrorResult.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, body.getStatus());
        assertEquals("An error occurred", body.getMessage());
    }
}

