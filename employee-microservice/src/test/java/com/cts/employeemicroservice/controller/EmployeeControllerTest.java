

package com.cts.employeemicroservice.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.cts.employeemicroservice.model.Employee;
import com.cts.employeemicroservice.service.EmployeeService;

import java.util.ArrayList;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {EmployeeController.class})
@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {
    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void testViewTopOffers() throws Exception {
        when(this.employeeService.viewTopOffers((String) any(), anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/viewMostLikedOffers/{id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetLikedOffers() throws Exception {
        when(this.employeeService.getLikedOffers((String) any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/recentlyLiked")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testViewEmployeeOffers() throws Exception {
        when(this.employeeService.viewEmpOffers((String) any(), anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/viewEmployeeOffers/{id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testViewProfile() throws Exception {
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
        when(this.employeeService.viewEmployee((String) any(), anyInt())).thenReturn(employee);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/viewProfile/{id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"department\":\"Department\",\"gender\":\"Gender\",\"age\":1,\"contactNumber\":1,\"email\":"
                                        + "\"jane.doe@example.org\",\"pointsGained\":1,\"offers\":[],\"engagedInOffers\":[],\"likedOffers\":[]}"));
    }
}






/*import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeControllerTest {

	EmployeeController employeeController = new EmployeeController();

    @Test
    @DisplayName("Checking for EmployeeController - if it is loading or not for @GetMapping")
    void employeeControllerNotNullTest(){
        assertThat(employeeController).isNotNull();
    }
	
}*/