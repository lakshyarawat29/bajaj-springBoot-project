package com.bajaj.bajajapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bajaj.bajajapp.service.WebhookClient;

@SpringBootTest
class BajajappApplicationTests {

	@MockBean
	private WebhookClient webhookClient;

	@Test
	void contextLoads() {
	}

}
