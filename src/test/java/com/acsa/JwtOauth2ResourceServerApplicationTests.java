package com.acsa;

import com.acsa.dto.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class JwtOauth2ResourceServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Container
	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26");

	@Test
	void contextLoads() throws Exception{

		var registerRequest = """
                {
                    "username":"john",
                    "password":"doe",
                    "email":"user@doe.com"
                }
                """;

		//register
		mockMvc.perform(post("/api/auth/register")
					.contentType("application/json")
					.content(registerRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message",
						org.hamcrest.Matchers.is(Matchers.notNullValue())));

		var loginRequest = """
                {
                    "username":"john",
                    "password":"doe"
                }
                """;

		// Login with the registered user and get a token
		var responseString = mockMvc.perform(post("/api/auth/login")
						.contentType("application/json")
						.content(loginRequest))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token",
						org.hamcrest.Matchers.is(Matchers.notNullValue())))
				.andReturn()
				.getResponse().getContentAsString();

		var loginResponse = new ObjectMapper().readValue(responseString, LoginResponse.class);

		// Use the token to access a protected resource
		mockMvc.perform(get("/api/hello")
						.header("Authorization", "Bearer " + loginResponse.token()))
				.andExpect(status().isOk())
				.andExpect(content().string(Matchers.is("Hello World")));

	}

}
