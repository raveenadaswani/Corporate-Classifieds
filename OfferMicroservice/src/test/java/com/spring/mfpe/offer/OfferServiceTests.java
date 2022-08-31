package com.spring.mfpe.offer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spring.mfpe.offer.clients.AuthClient;
import com.spring.mfpe.offer.clients.EmployeeClient;
import com.spring.mfpe.offer.entities.Employee;
import com.spring.mfpe.offer.entities.Offer;
import com.spring.mfpe.offer.exceptions.EmployeeNotFoundException;
import com.spring.mfpe.offer.exceptions.ImproperDateException;
import com.spring.mfpe.offer.exceptions.InvalidTokenException;
import com.spring.mfpe.offer.exceptions.MicroserviceException;
import com.spring.mfpe.offer.exceptions.OfferAlreadyEngagedException;
import com.spring.mfpe.offer.exceptions.OfferNotFoundException;
import com.spring.mfpe.offer.model.AuthResponse;
import com.spring.mfpe.offer.model.SuccessResponse;
import com.spring.mfpe.offer.repositories.EmployeeRepository;
import com.spring.mfpe.offer.repositories.OfferRepository;
import com.spring.mfpe.offer.services.OfferService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
@ContextConfiguration(classes = {OfferService.class})
@ExtendWith(SpringExtension.class)
class OfferServiceTests {
    @MockBean
    private AuthClient authClient;

    @MockBean
    private EmployeeClient employeeClient;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    @MockBean
    private SuccessResponse successResponse;

    @Test
    void testGetOfferDetails() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.getOfferDetails("ABC123", 123));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferDetails2() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertSame(offer, this.offerService.getOfferDetails("ABC123", 123));
        verify(this.offerRepository).findById((Integer) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferDetails3() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findById((Integer) any())).thenReturn(Optional.empty());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferDetails("ABC123", 123));
        verify(this.offerRepository).findById((Integer) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferDetails4() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, false), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getOfferDetails("ABC123", 123));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferDetails5() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertSame(offer, this.offerService.getOfferDetails("ABC123", 123));
        verify(this.offerRepository).findById((Integer) any());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetOfferByCategory() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.getOfferByCategory("ABC123", "Category"));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByCategory2() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findByCategory((String) any())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferByCategory("ABC123", "Category"));
        verify(this.offerRepository).findByCategory((String) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByCategory3() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("no offers found");
        employee.setGender("no offers found");
        employee.setId(1);
        employee.setName("no offers found");
        employee.setPointsGained(0);
        employee.setAge(0);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(0L);
        employee.setEngagedInOffers(new HashSet<>());

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("no offers found");
        employee1.setGender("no offers found");
        employee1.setId(1);
        employee1.setName("no offers found");
        employee1.setPointsGained(0);
        employee1.setAge(0);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(0L);
        employee1.setEngagedInOffers(new HashSet<>());

        Offer offer = new Offer();
        offer.setLikes(0);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("no offers found");
        offer.setCategory("no offers found");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Offer> offerList = new ArrayList<>();
        offerList.add(offer);
        when(this.offerRepository.findByCategory((String) any())).thenReturn(offerList);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        List<Offer> actualOfferByCategory = this.offerService.getOfferByCategory("ABC123", "Category");
        assertSame(offerList, actualOfferByCategory);
        assertEquals(1, actualOfferByCategory.size());
        verify(this.offerRepository).findByCategory((String) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByCategory4() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findByCategory((String) any())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, false), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getOfferByCategory("ABC123", "Category"));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByCategory5() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findByCategory((String) any())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferByCategory("ABC123", "Category"));
        verify(this.offerRepository).findByCategory((String) any());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetOfferByTopLikes() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.getOfferByTopLikes("ABC123"));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByTopLikes2() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferByTopLikes("ABC123"));
        verify(this.offerRepository).findAll((org.springframework.data.domain.Pageable) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByTopLikes3() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("likes");
        employee.setGender("likes");
        employee.setId(1);
        employee.setName("likes");
        employee.setPointsGained(0);
        employee.setAge(0);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(0L);
        employee.setEngagedInOffers(new HashSet<>());

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("likes");
        employee1.setGender("likes");
        employee1.setId(1);
        employee1.setName("likes");
        employee1.setPointsGained(0);
        employee1.setAge(0);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(0L);
        employee1.setEngagedInOffers(new HashSet<>());

        Offer offer = new Offer();
        offer.setLikes(0);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("likes");
        offer.setCategory("likes");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Offer> offerList = new ArrayList<>();
        offerList.add(offer);
        when(this.offerRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(offerList);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        List<Offer> actualOfferByTopLikes = this.offerService.getOfferByTopLikes("ABC123");
        assertSame(offerList, actualOfferByTopLikes);
        assertEquals(1, actualOfferByTopLikes.size());
        verify(this.offerRepository).findAll((org.springframework.data.domain.Pageable) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByTopLikes4() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, false), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getOfferByTopLikes("ABC123"));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByTopLikes5() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferByTopLikes("ABC123"));
        verify(this.offerRepository).findAll((org.springframework.data.domain.Pageable) any());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetOfferByPostedDate()
            throws ImproperDateException, InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.getOfferByPostedDate("ABC123", "2020-03-01"));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByPostedDate2()
            throws ImproperDateException, InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByPostedDate(anyInt(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferByPostedDate("ABC123", "2020-03-01"));
        verify(this.offerRepository).getByPostedDate(anyInt(), anyInt(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByPostedDate3()
            throws ImproperDateException, InvalidTokenException, MicroserviceException, OfferNotFoundException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("no offers found");
        employee.setGender("no offers found");
        employee.setId(1);
        employee.setName("no offers found");
        employee.setPointsGained(0);
        employee.setAge(0);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(0L);
        employee.setEngagedInOffers(new HashSet<>());

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("no offers found");
        employee1.setGender("no offers found");
        employee1.setId(1);
        employee1.setName("no offers found");
        employee1.setPointsGained(0);
        employee1.setAge(0);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(0L);
        employee1.setEngagedInOffers(new HashSet<>());

        Offer offer = new Offer();
        offer.setLikes(0);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("no offers found");
        offer.setCategory("no offers found");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Offer> offerList = new ArrayList<>();
        offerList.add(offer);
        when(this.offerRepository.getByPostedDate(anyInt(), anyInt(), anyInt())).thenReturn(offerList);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        List<Offer> actualOfferByPostedDate = this.offerService.getOfferByPostedDate("ABC123", "2020-03-01");
        assertSame(offerList, actualOfferByPostedDate);
        assertEquals(1, actualOfferByPostedDate.size());
        verify(this.offerRepository).getByPostedDate(anyInt(), anyInt(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByPostedDate4()
            throws ImproperDateException, InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByPostedDate(anyInt(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, false), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getOfferByPostedDate("ABC123", "2020-03-01"));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOfferByPostedDate5()
            throws ImproperDateException, InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByPostedDate(anyInt(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOfferByPostedDate("ABC123", "2020-03-01"));
        verify(this.offerRepository).getByPostedDate(anyInt(), anyInt(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetOfferByPostedDate6()
            throws ImproperDateException, InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByPostedDate(anyInt(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(ImproperDateException.class, () -> this.offerService.getOfferByPostedDate("ABC123", "2020/03/01"));
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testEngageOffer() throws EmployeeNotFoundException, InvalidTokenException, MicroserviceException,
            OfferAlreadyEngagedException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.engageOffer("ABC123", 123, 123));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testEngageOffer2() throws EmployeeNotFoundException, InvalidTokenException, MicroserviceException,
            OfferAlreadyEngagedException, OfferNotFoundException {
        doNothing().when(this.successResponse).setTimestamp((java.util.Date) any());
        doNothing().when(this.successResponse).setStatus((HttpStatus) any());
        doNothing().when(this.successResponse).setMessage((String) any());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        this.offerService.engageOffer("ABC123", 123, 123);
        verify(this.successResponse).setMessage((String) any());
        verify(this.successResponse).setStatus((HttpStatus) any());
        verify(this.successResponse).setTimestamp((java.util.Date) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testEngageOffer3() throws EmployeeNotFoundException, InvalidTokenException, MicroserviceException,
            OfferAlreadyEngagedException, OfferNotFoundException {
        doNothing().when(this.successResponse).setTimestamp((Date) any());
        doNothing().when(this.successResponse).setStatus((HttpStatus) any());
        doNothing().when(this.successResponse).setMessage((String) any());

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 123, true), HttpStatus.CONTINUE));
        this.offerService.engageOffer("ABC123", 123, 123);
        verify(this.successResponse).setMessage((String) any());
        verify(this.successResponse).setStatus((HttpStatus) any());
        verify(this.successResponse).setTimestamp((Date) any());
        verify(this.offerRepository).findById((Integer) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testEngageOffer4() throws EmployeeNotFoundException, InvalidTokenException, MicroserviceException,
            OfferAlreadyEngagedException, OfferNotFoundException {
        doNothing().when(this.successResponse).setTimestamp((Date) any());
        doNothing().when(this.successResponse).setStatus((HttpStatus) any());
        doNothing().when(this.successResponse).setMessage((String) any());

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        offer.setClosedDate(null);
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 123, true), HttpStatus.CONTINUE));
        assertThrows(OfferAlreadyEngagedException.class, () -> this.offerService.engageOffer("ABC123", 123, 123));
        verify(this.offerRepository).findById((Integer) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testEditOffer() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        assertThrows(MicroserviceException.class, () -> this.offerService.editOffer("ABC123", offer));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testEditOffer2() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        doNothing().when(this.successResponse).setTimestamp((Date) any());
        doNothing().when(this.successResponse).setStatus((HttpStatus) any());
        doNothing().when(this.successResponse).setMessage((String) any());

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);

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

        Offer offer1 = new Offer();
        offer1.setLikes(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setEngagedDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        offer1.setEmp(employee2);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setClosedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        offer1.setId(1);
        offer1.setEngagedEmp(employee3);
        offer1.setName("Name");
        offer1.setCategory("Category");
        offer1.setLikedByEmployees(new HashSet<>());
        offer1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setOpenDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        when(this.offerRepository.save((Offer) any())).thenReturn(offer1);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));

        Employee employee4 = new Employee();
        employee4.setEmail("jane.doe@example.org");
        employee4.setOffers(new HashSet<>());
        employee4.setDepartment("Department");
        employee4.setGender("Gender");
        employee4.setId(1);
        employee4.setName("Name");
        employee4.setPointsGained(1);
        employee4.setAge(1);
        employee4.setLikedOffers(new HashSet<>());
        employee4.setContactNumber(1L);
        employee4.setEngagedInOffers(new HashSet<>());

        Employee employee5 = new Employee();
        employee5.setEmail("jane.doe@example.org");
        employee5.setOffers(new HashSet<>());
        employee5.setDepartment("Department");
        employee5.setGender("Gender");
        employee5.setId(1);
        employee5.setName("Name");
        employee5.setPointsGained(1);
        employee5.setAge(1);
        employee5.setLikedOffers(new HashSet<>());
        employee5.setContactNumber(1L);
        employee5.setEngagedInOffers(new HashSet<>());

        Offer offer2 = new Offer();
        offer2.setLikes(1);
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer2.setEngagedDate(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        offer2.setEmp(employee4);
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer2.setClosedDate(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));
        offer2.setId(1);
        offer2.setEngagedEmp(employee5);
        offer2.setName("Name");
        offer2.setCategory("Category");
        offer2.setLikedByEmployees(new HashSet<>());
        offer2.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer2.setOpenDate(Date.from(atStartOfDayResult8.atZone(ZoneId.of("UTC")).toInstant()));
        this.offerService.editOffer("ABC123", offer2);
        verify(this.successResponse).setMessage((String) any());
        verify(this.successResponse).setStatus((HttpStatus) any());
        verify(this.successResponse).setTimestamp((Date) any());
        verify(this.offerRepository).findById((Integer) any());
        verify(this.offerRepository).save((Offer) any());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testAddOffer() throws EmployeeNotFoundException, InvalidTokenException, MicroserviceException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        assertThrows(MicroserviceException.class, () -> this.offerService.addOffer("ABC123", offer));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testAddOffer2() throws EmployeeNotFoundException, InvalidTokenException, MicroserviceException {
        doNothing().when(this.successResponse).setTimestamp((Date) any());
        doNothing().when(this.successResponse).setStatus((HttpStatus) any());
        doNothing().when(this.successResponse).setMessage((String) any());

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        when(this.offerRepository.save((Offer) any())).thenReturn(offer);

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
        when(this.employeeClient.getEmployee((String) any(), anyInt())).thenReturn(employee2);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));

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

        Employee employee4 = new Employee();
        employee4.setEmail("jane.doe@example.org");
        employee4.setOffers(new HashSet<>());
        employee4.setDepartment("Department");
        employee4.setGender("Gender");
        employee4.setId(1);
        employee4.setName("Name");
        employee4.setPointsGained(1);
        employee4.setAge(1);
        employee4.setLikedOffers(new HashSet<>());
        employee4.setContactNumber(1L);
        employee4.setEngagedInOffers(new HashSet<>());

        Offer offer1 = new Offer();
        offer1.setLikes(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setEngagedDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        offer1.setEmp(employee3);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setClosedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        offer1.setId(1);
        offer1.setEngagedEmp(employee4);
        offer1.setName("Name");
        offer1.setCategory("Category");
        offer1.setLikedByEmployees(new HashSet<>());
        offer1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setOpenDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        this.offerService.addOffer("ABC123", offer1);
        verify(this.successResponse).setMessage((String) any());
        verify(this.successResponse).setStatus((HttpStatus) any());
        verify(this.successResponse).setTimestamp((Date) any());
        verify(this.offerRepository).save((Offer) any());
        verify(this.employeeClient).getEmployee((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        assertNull(offer1.getEngagedDate());
        assertNull(offer1.getClosedDate());
        assertSame(employee2, offer1.getEmp());
    }

    @Test
    void testGetOffersById() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.getOffersById("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOffersById2() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByEmpId(anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOffersById("ABC123", 1));
        verify(this.offerRepository).getByEmpId(anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOffersById3() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setOffers(new HashSet<>());
        employee.setDepartment("no offers found");
        employee.setGender("no offers found");
        employee.setId(1);
        employee.setName("no offers found");
        employee.setPointsGained(0);
        employee.setAge(0);
        employee.setLikedOffers(new HashSet<>());
        employee.setContactNumber(0L);
        employee.setEngagedInOffers(new HashSet<>());

        Employee employee1 = new Employee();
        employee1.setEmail("jane.doe@example.org");
        employee1.setOffers(new HashSet<>());
        employee1.setDepartment("no offers found");
        employee1.setGender("no offers found");
        employee1.setId(1);
        employee1.setName("no offers found");
        employee1.setPointsGained(0);
        employee1.setAge(0);
        employee1.setLikedOffers(new HashSet<>());
        employee1.setContactNumber(0L);
        employee1.setEngagedInOffers(new HashSet<>());

        Offer offer = new Offer();
        offer.setLikes(0);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("no offers found");
        offer.setCategory("no offers found");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Offer> offerList = new ArrayList<>();
        offerList.add(offer);
        when(this.offerRepository.getByEmpId(anyInt())).thenReturn(offerList);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        List<Offer> actualOffersById = this.offerService.getOffersById("ABC123", 1);
        assertSame(offerList, actualOffersById);
        assertEquals(1, actualOffersById.size());
        verify(this.offerRepository).getByEmpId(anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOffersById4() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByEmpId(anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 123, true), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getOffersById("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOffersById5() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByEmpId(anyInt())).thenReturn(new ArrayList<>());
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, false), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getOffersById("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetOffersById6() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByEmpId(anyInt())).thenReturn(new ArrayList<>());
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOffersById("ABC123", 1));
        verify(this.offerRepository).getByEmpId(anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(authResponse).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testGetOffersById7() throws InvalidTokenException, MicroserviceException, OfferNotFoundException {
        when(this.offerRepository.getByEmpId(anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertThrows(OfferNotFoundException.class, () -> this.offerService.getOffersById("ABC123", 1));
        verify(this.offerRepository).getByEmpId(anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity, atLeast(1)).getBody();
    }

    @Test
    void testGetPointsById() throws InvalidTokenException, MicroserviceException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.getPointsById("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPointsById2() throws InvalidTokenException, MicroserviceException {
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
        when(this.employeeClient.getEmployee((String) any(), anyInt())).thenReturn(employee);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertEquals(1, this.offerService.getPointsById("ABC123", 1));
        verify(this.employeeClient).getEmployee((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPointsById3() throws InvalidTokenException, MicroserviceException {
        when(this.employeeClient.getEmployee((String) any(), anyInt()))
                .thenThrow(new InvalidTokenException("An error occurred"));
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        assertThrows(MicroserviceException.class, () -> this.offerService.getPointsById("ABC123", 1));
        verify(this.employeeClient).getEmployee((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPointsById4() throws InvalidTokenException, MicroserviceException {
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
        when(this.employeeClient.getEmployee((String) any(), anyInt())).thenReturn(employee);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 123, true), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getPointsById("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPointsById5() throws InvalidTokenException, MicroserviceException {
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
        when(this.employeeClient.getEmployee((String) any(), anyInt())).thenReturn(employee);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, false), HttpStatus.CONTINUE));
        assertThrows(InvalidTokenException.class, () -> this.offerService.getPointsById("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testGetPointsById6() throws InvalidTokenException, MicroserviceException {
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
        when(this.employeeClient.getEmployee((String) any(), anyInt())).thenReturn(employee);
        AuthResponse authResponse = mock(AuthResponse.class);
        when(authResponse.getEmpid()).thenReturn(1);
        when(authResponse.isValid()).thenReturn(true);
        ResponseEntity<AuthResponse> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.CONTINUE);

        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertEquals(1, this.offerService.getPointsById("ABC123", 1));
        verify(this.employeeClient).getEmployee((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(authResponse).getEmpid();
        verify(authResponse).isValid();
    }

    @Test
    void testGetPointsById7() throws InvalidTokenException, MicroserviceException {
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
        when(this.employeeClient.getEmployee((String) any(), anyInt())).thenReturn(employee);
        ResponseEntity<AuthResponse> responseEntity = (ResponseEntity<AuthResponse>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new AuthResponse("janedoe", 1, true));
        when(this.authClient.verifyToken((String) any())).thenReturn(responseEntity);
        assertEquals(1, this.offerService.getPointsById("ABC123", 1));
        verify(this.employeeClient).getEmployee((String) any(), anyInt());
        verify(this.authClient).verifyToken((String) any());
        verify(responseEntity, atLeast(1)).getBody();
    }

    @Test
    void testUpdateLikes() throws MicroserviceException, OfferNotFoundException {
        when(this.authClient.verifyToken((String) any())).thenThrow(new InvalidTokenException("An error occurred"));
        assertThrows(MicroserviceException.class, () -> this.offerService.updateLikes("ABC123", 1));
        verify(this.authClient).verifyToken((String) any());
    }

    @Test
    void testUpdateLikes2() throws MicroserviceException, OfferNotFoundException {
        doNothing().when(this.successResponse).setTimestamp((Date) any());
        doNothing().when(this.successResponse).setStatus((HttpStatus) any());
        doNothing().when(this.successResponse).setMessage((String) any());

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

        Offer offer = new Offer();
        offer.setLikes(1);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setEngagedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setEmp(employee);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setClosedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        offer.setId(1);
        offer.setEngagedEmp(employee1);
        offer.setName("Name");
        offer.setCategory("Category");
        offer.setLikedByEmployees(new HashSet<>());
        offer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer.setOpenDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Offer> ofResult = Optional.of(offer);

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

        Offer offer1 = new Offer();
        offer1.setLikes(1);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setEngagedDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        offer1.setEmp(employee2);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setClosedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        offer1.setId(1);
        offer1.setEngagedEmp(employee3);
        offer1.setName("Name");
        offer1.setCategory("Category");
        offer1.setLikedByEmployees(new HashSet<>());
        offer1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        offer1.setOpenDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        when(this.offerRepository.save((Offer) any())).thenReturn(offer1);
        when(this.offerRepository.findById((Integer) any())).thenReturn(ofResult);
        when(this.authClient.verifyToken((String) any()))
                .thenReturn(new ResponseEntity<>(new AuthResponse("janedoe", 1, true), HttpStatus.CONTINUE));
        this.offerService.updateLikes("ABC123", 1);
        verify(this.successResponse).setMessage((String) any());
        verify(this.successResponse).setStatus((HttpStatus) any());
        verify(this.successResponse).setTimestamp((Date) any());
        verify(this.offerRepository).findById((Integer) any());
        verify(this.offerRepository).save((Offer) any());
        verify(this.authClient).verifyToken((String) any());
    }
}

