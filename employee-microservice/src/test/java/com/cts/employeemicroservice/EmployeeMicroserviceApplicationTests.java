package com.cts.employeemicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import springfox.documentation.spring.web.plugins.Docket;

@SpringBootTest
class EmployeeMicroserviceApplicationTests {

	@Test
	void contextLoads() {
	
	}
	@Test
    void testProductApi() {
        Docket actualProductApiResult = (new EmployeeMicroserviceApplication()).productApi();
        assertTrue(actualProductApiResult.isEnabled());
        assertEquals("default", actualProductApiResult.getGroupName());
    }

}
