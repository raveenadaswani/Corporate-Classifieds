package com.cts.employeemicroservice.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cts.employeemicroservice.client.AuthClient;
import com.cts.employeemicroservice.client.OfferClient;
import com.cts.employeemicroservice.exception.InvalidUserException;
import com.cts.employeemicroservice.exception.MicroserviceException;
import com.cts.employeemicroservice.model.AuthResponse;
import com.cts.employeemicroservice.model.Employee;
import com.cts.employeemicroservice.model.EmployeeOffers;
import com.cts.employeemicroservice.model.MessageResponse;
import com.cts.employeemicroservice.repository.EmployeeRepository;
import com.cts.employeemicroservice.repository.OfferRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
@ContextConfiguration(classes = {EmployeeServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmployeeServiceImplTest {
    @MockBean
    private AuthClient authClient;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private MessageResponse messageResponse;

    @MockBean
    private OfferClient offerClient;

    @MockBean
    private OfferRepository offerRepository;

    @Test
    void testViewEmpOffers() throws InvalidUserException, MicroserviceException {
        when(this.authClient.getValidity((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewEmpOffers("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmpOffers2() throws InvalidUserException, MicroserviceException {
        ArrayList<EmployeeOffers> employeeOffersList = new ArrayList<>();
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(employeeOffersList);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        List<EmployeeOffers> actualViewEmpOffersResult = this.employeeServiceImpl.viewEmpOffers("ABC123", 1);
        assertSame(employeeOffersList, actualViewEmpOffersResult);
        assertTrue(actualViewEmpOffersResult.isEmpty());
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmpOffers3() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewEmpOffers("ABC123", 1));
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmpOffers4() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(123, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.viewEmpOffers("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmpOffers5() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.viewEmpOffers("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmpOffers6() throws InvalidUserException, MicroserviceException {
        ArrayList<EmployeeOffers> employeeOffersList = new ArrayList<>();
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(employeeOffersList);
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        List<EmployeeOffers> actualViewEmpOffersResult = this.employeeServiceImpl.viewEmpOffers("ABC123", 1);
        assertSame(employeeOffersList, actualViewEmpOffersResult);
        assertTrue(actualViewEmpOffersResult.isEmpty());
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
        verify(authResponse).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testViewEmpOffers7() throws InvalidUserException, MicroserviceException {
        ArrayList<EmployeeOffers> employeeOffersList = new ArrayList<>();
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(employeeOffersList);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse(1, "janedoe", true));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        List<EmployeeOffers> actualViewEmpOffersResult = this.employeeServiceImpl.viewEmpOffers("ABC123", 1);
        assertSame(employeeOffersList, actualViewEmpOffersResult);
        assertTrue(actualViewEmpOffersResult.isEmpty());
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testViewEmpOffers8() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewEmpOffers("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testViewEmployee() throws InvalidUserException, MicroserviceException {
        when(this.authClient.getValidity((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmployee2() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertSame(employee, this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmployee3() throws InvalidUserException, MicroserviceException {
        when(this.employeeRepository.findById((Integer) any())).thenReturn(Optional.empty());
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(NoSuchElementException.class, () -> this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmployee4() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(123, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmployee5() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmployee6() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertSame(employee, this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
        verify(authResponse).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testViewEmployee7() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any())).thenReturn(null);
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewEmployee8() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse(1, "janedoe", true));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertSame(employee, this.employeeServiceImpl.viewEmployee("ABC123", 1));
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testViewTopOffers() throws InvalidUserException, MicroserviceException {
        when(this.authClient.getValidity((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewTopOffers("ABC123", 123));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewTopOffers2() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertTrue(this.employeeServiceImpl.viewTopOffers("ABC123", 123).isEmpty());
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewTopOffers3() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewTopOffers("ABC123", 123));
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewTopOffers4() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.viewTopOffers("ABC123", 123));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testViewTopOffers5() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse(1, "janedoe", true));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertTrue(this.employeeServiceImpl.viewTopOffers("ABC123", 123).isEmpty());
        verify(this.offerClient).getOffersById((String) any(), anyInt());
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testViewTopOffers6() throws InvalidUserException, MicroserviceException {
        when(this.offerClient.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.viewTopOffers("ABC123", 123));
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testSavePoints() throws InvalidUserException, MicroserviceException {
        when(this.authClient.getValidity((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.savePoints("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testSavePoints2() throws InvalidUserException, MicroserviceException {
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());

        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("Department");
        employee1.setGender("Gender");
        employee1.setId(1);
        employee1.setName("Name");
        employee1.setPointsGained(1);
        employee1.setAge(1);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(1L);
        employee1.setEngagedInOffers(new HashSet<>());
        when(this.employeeRepository.save((Employee) any())).thenReturn(employee1);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        this.employeeServiceImpl.savePoints("ABC123", 1);
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((java.util.Date) any());
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.employeeRepository).save((Employee) any());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testSavePoints3() throws InvalidUserException, MicroserviceException {
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());

        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("Department");
        employee1.setGender("Gender");
        employee1.setId(1);
        employee1.setName("Name");
        employee1.setPointsGained(1);
        employee1.setAge(1);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(1L);
        employee1.setEngagedInOffers(new HashSet<>());
        when(this.employeeRepository.save((Employee) any())).thenReturn(employee1);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.savePoints("ABC123", 1));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testSavePoints4() throws InvalidUserException, MicroserviceException {
        doNothing().when(this.messageResponse).setTimeStamp((java.util.Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());

        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("Department");
        employee1.setGender("Gender");
        employee1.setId(1);
        employee1.setName("Name");
        employee1.setPointsGained(1);
        employee1.setAge(1);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(1L);
        employee1.setEngagedInOffers(new HashSet<>());
        when(this.employeeRepository.save((Employee) any())).thenReturn(employee1);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        this.employeeServiceImpl.savePoints("ABC123", 1);
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((java.util.Date) any());
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.employeeRepository).save((Employee) any());
        verify(this.authClient).getValidity((String) any());
        verify(authResponse).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testLikeOffer() throws MicroserviceException {
        when(this.authClient.getValidity((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.likeOffer("ABC123", 123));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testLikeOffer2() throws MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("Department");
        employee1.setGender("Gender");
        employee1.setId(1);
        employee1.setName("Name");
        employee1.setPointsGained(1);
        employee1.setAge(1);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(1L);
        employee1.setEngagedInOffers(new HashSet<>());

        EmployeeOffers employeeOffers = new EmployeeOffers();
        employeeOffers.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        employeeOffers.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        employeeOffers.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        employeeOffers.setId(1);
        employeeOffers.setName("Name");
        employeeOffers.setCategory("Category");
        employeeOffers.setLikedByEmployees(new HashSet<>());
        employeeOffers.setEngagedEmp(employee);
        employeeOffers.setDescription("The characteristics of someone or something");
        employeeOffers.setEmp(employee1);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        employeeOffers.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        when(this.offerClient.updateLikes((String) any(), anyInt())).thenReturn(this.messageResponse);
        when(this.offerClient.getOfferDetailsById((String) any(), anyInt())).thenReturn(employeeOffers);
        doNothing().when(this.messageResponse).setTimeStamp((Date) any());
        doNothing().when(this.messageResponse).setStatus((HttpStatus) any());
        doNothing().when(this.messageResponse).setMessage((String) any());

        Employee employee2 = new Employee();
        employee2.setEmail("jane.doe@example.org");
        employee2.setOffers(new HashSet<>());
        employee2.setDepartment("Department");
        employee2.setGender("Gender");
        employee2.setId(1);
        employee2.setName("Name");
        employee2.setPointsGained(1);
        employee2.setAge(1);
        employee2.setLikedOffers(new HashSet<>());
        employee2.setContactNumber(1L);
        employee2.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee2);

        Employee employee3 = new Employee();
        employee3.setEmail("jane.doe@example.org");
        employee3.setOffers(new HashSet<>());
        employee3.setDepartment("Department");
        employee3.setGender("Gender");
        employee3.setId(1);
        employee3.setName("Name");
        employee3.setPointsGained(1);
        employee3.setAge(1);
        employee3.setLikedOffers(new HashSet<>());
        employee3.setContactNumber(1L);
        employee3.setEngagedInOffers(new HashSet<>());
        when(this.employeeRepository.save((Employee) any())).thenReturn(employee3);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        this.employeeServiceImpl.likeOffer("ABC123", 123);
        verify(this.offerClient).getOfferDetailsById((String) any(), anyInt());
        verify(this.offerClient).updateLikes((String) any(), anyInt());
        verify(this.messageResponse).setMessage((String) any());
        verify(this.messageResponse).setStatus((HttpStatus) any());
        verify(this.messageResponse).setTimeStamp((Date) any());
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.employeeRepository).save((Employee) any());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testGetLikedOffers() throws InvalidUserException, MicroserviceException {
        when(this.authClient.getValidity((String) any())).thenThrow(new InvalidUserException("Msg"));
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.getLikedOffers("ABC123"));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testGetLikedOffers2() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", true), HttpStatus.CONTINUE));
        assertTrue(this.employeeServiceImpl.getLikedOffers("ABC123").isEmpty());
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testGetLikedOffers3() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.getValidity((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse(1, "janedoe", false), HttpStatus.CONTINUE));
        assertThrows(InvalidUserException.class, () -> this.employeeServiceImpl.getLikedOffers("ABC123"));
        verify(this.authClient).getValidity((String) any());
    }

    @Test
    void testGetLikedOffers4() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertTrue(this.employeeServiceImpl.getLikedOffers("ABC123").isEmpty());
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
        verify(authResponse, atLeast(1)).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testGetLikedOffers5() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse(1, "janedoe", true));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertTrue(this.employeeServiceImpl.getLikedOffers("ABC123").isEmpty());
        verify(this.employeeRepository).findById((Integer) any());
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetLikedOffers6() throws InvalidUserException, MicroserviceException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("Department");
        employee.setGender("Gender");
        employee.setId(1);
        employee.setName("Name");
        employee.setPointsGained(1);
        employee.setAge(1);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(1L);
        employee.setEngagedInOffers(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(this.employeeRepository.findById((Integer) any())).thenReturn(ofResult);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(new InvalidUserException("Msg"));
        when(this.authClient.getValidity((String) any())).thenReturn(responseEntity);
        assertThrows(MicroserviceException.class, () -> this.employeeServiceImpl.getLikedOffers("ABC123"));
        verify(this.authClient).getValidity((String) any());
        verify(responseEntity).getBody();
    }
}

