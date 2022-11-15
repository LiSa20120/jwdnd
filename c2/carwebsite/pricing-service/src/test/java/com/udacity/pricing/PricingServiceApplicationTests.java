package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetAllPrices() {
		var response = testRestTemplate.getForEntity("http://localhost:" + port + "/prices", Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testGetPriceById() {
		var response = testRestTemplate.getForEntity("http://localhost:" + port + "/prices/1", Price.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}
