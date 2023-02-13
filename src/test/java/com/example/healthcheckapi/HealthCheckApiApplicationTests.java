package com.example.healthcheckapi;

import com.example.healthcheckapi.health.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(HealthController.class)
class HealthCheckApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHealthCheck() throws Exception
    {
        mockMvc.perform(get("/healthz")).andExpect(status().isOk());
    }

}
