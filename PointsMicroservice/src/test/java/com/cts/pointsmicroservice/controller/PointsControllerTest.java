package com.cts.pointsmicroservice.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.cts.pointsmicroservice.model.MessageResponse;
import com.cts.pointsmicroservice.service.PointsService;
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

@ContextConfiguration(classes = {PointsController.class})
@ExtendWith(SpringExtension.class)
class PointsControllerTest {
    @Autowired
    private PointsController pointsController;

    @MockBean
    private PointsService pointsService;

    @Test
    void testGetPointsByEmpId() throws Exception {
        when(this.pointsService.getPoints((String) any(), anyInt())).thenReturn(1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getpointsbyemp/{id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.pointsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }

    @Test
    void testRefreshPointsByEmpId() throws Exception {
        when(this.pointsService.refreshPoints((String) any(), anyInt())).thenReturn(new MessageResponse());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/refreshpointsbyemp/{id}", 1)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(this.pointsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"timeStamp\":null,\"message\":null,\"status\":null}"));
    }
}

