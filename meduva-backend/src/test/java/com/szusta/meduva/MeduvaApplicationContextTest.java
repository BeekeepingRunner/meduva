package com.szusta.meduva;

import com.szusta.meduva.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MeduvaApplicationContextTest {

    @Autowired
    private AuthController sampleController;

    @Test
    public void contextLoads() {
        assertNotNull(sampleController);
    }
}
