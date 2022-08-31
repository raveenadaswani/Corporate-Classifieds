package com.cts.pointsmicroservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.pointsmicroservice.controller.PointsController;

import springfox.documentation.spring.web.plugins.Docket;

@SpringBootTest
class PointsMicroserviceApplicationTests {

	@Autowired
	private PointsController pointsController;
	
	@Test
	void contextLoads() {
		assertThat(pointsController).isNotNull();
	}
	@Test
    void testProductApi() {
        Docket actualProductApiResult = (new PointsMicroserviceApplication()).productApi();
        assertTrue(actualProductApiResult.isEnabled());
        assertEquals("default", actualProductApiResult.getGroupName());
    }
}
