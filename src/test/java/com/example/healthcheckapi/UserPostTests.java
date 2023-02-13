package com.example.healthcheckapi;

import com.example.healthcheckapi.config.AmazonDynamoDbClient;
import com.example.healthcheckapi.config.AmazonSNSClient;
import com.example.healthcheckapi.controller.UserController;
import com.example.healthcheckapi.repository.ImageRepository;
import com.example.healthcheckapi.repository.UserRepository;
import com.example.healthcheckapi.service.ImageStorageService;
import com.timgroup.statsd.StatsDClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserPostTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataSource dataSource;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageStorageService service;

    @MockBean
    private StatsDClient statsDClient;

    @MockBean
    private AmazonSNSClient amazonSNSClient;

    @MockBean
    private AmazonDynamoDbClient amazonDynamoDbClient;

    @Test
    public void testCreateUser() throws Exception
    {
        String json = "{\"first_name\":\"First\",\"last_name\":\"Last\",\"password\":\"password\",\"username\":\"email@mail.com\"}";
        System.out.println(json);

//        mockMvc.perform(post("/v1/user").content(json)
//                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isCreated());
    }

}


