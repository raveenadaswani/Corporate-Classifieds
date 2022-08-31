package com.spring.mfpe.offer;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mfpe.offer.controller.OfferController;
import com.spring.mfpe.offer.entities.Employee;
import com.spring.mfpe.offer.entities.Offer;
import com.spring.mfpe.offer.model.SuccessResponse;
import com.spring.mfpe.offer.services.OfferService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {OfferController.class})
@ExtendWith(SpringExtension.class)
class OfferControllerTest {
    @Autowired
    private OfferController offerController;

    @MockBean
    private OfferService offerService;

    @Test
    void testGetOfferDetails() throws Exception {
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
        when(this.offerService.getOfferDetails((String) any(), anyInt())).thenReturn(offer);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/offer/getOfferDetails/{offerId}", 123)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"category\":\"Category"
                                        + "\",\"openDate\":0,\"engagedDate\":0,\"closedDate\":0,\"likes\":1}"));
    }

    @Test
    void testGetOfferByCategory() throws Exception {
        when(this.offerService.getOfferByCategory((String) any(), (String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/offer/getOfferByCategory/{category}", "Category")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetOfferByTopLikes() throws Exception {
        when(this.offerService.getOfferByTopLikes((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/offer/getOfferByTopLikes")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetOfferByPostedDate() throws Exception {
        when(this.offerService.getOfferByPostedDate((String) any(), (String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/offer/getOfferByPostedDate/{date}", "2020-03-01")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testEngageOffer() throws Exception {
        when(this.offerService.engageOffer((String) any(), anyInt(), anyInt())).thenReturn(new SuccessResponse());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/offer/engageOffer");
        MockHttpServletRequestBuilder paramResult = postResult.param("employeeId", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("offerId", String.valueOf(1))
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":null,\"status\":null,\"timestamp\":null}"));
    }

    @Test
    void testEditOffer() throws Exception {
        when(this.offerService.editOffer((String) any(), (Offer) any())).thenReturn(new SuccessResponse());

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
        String content = (new ObjectMapper()).writeValueAsString(offer);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/offer/editOffer")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":null,\"status\":null,\"timestamp\":null}"));
    }

    @Test
    void testAddOffer() throws Exception {
        when(this.offerService.addOffer((String) any(), (Offer) any())).thenReturn(new SuccessResponse());

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
        String content = (new ObjectMapper()).writeValueAsString(offer);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/offer/addOffer")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":null,\"status\":null,\"timestamp\":null}"));
    }

    @Test
    void testGetOffersById() throws Exception {
        when(this.offerService.getOffersById((String) any(), anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/offer/getOffers/{emp_id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetPointsById() throws Exception {
        when(this.offerService.getPointsById((String) any(), anyInt())).thenReturn(123);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/offer/getPoints/{emp_id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("123"));
    }

    @Test
    void testUpdateLikes() throws Exception {
        when(this.offerService.updateLikes((String) any(), anyInt())).thenReturn(new SuccessResponse());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/offer/updateLikes/{offer_id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.offerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":null,\"status\":null,\"timestamp\":null}"));
    }
}

