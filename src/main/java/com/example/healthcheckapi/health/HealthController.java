package com.example.healthcheckapi.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping("/healthz")
    public void healthCheck()
    {
    }
}
