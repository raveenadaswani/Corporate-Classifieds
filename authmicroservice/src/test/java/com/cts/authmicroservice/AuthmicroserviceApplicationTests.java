package com.cts.authmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import springfox.documentation.spring.web.plugins.Docket;

@SpringBootTest
class AuthmicroserviceApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
    void testProductApi() {
        Docket actualProductApiResult = (new AuthmicroserviceApplication()).productApi();
        assertTrue(actualProductApiResult.isEnabled());
        assertEquals("default", actualProductApiResult.getGroupName());
    }

}
