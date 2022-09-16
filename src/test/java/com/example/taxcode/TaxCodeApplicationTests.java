package com.example.taxcode;

import com.example.taxcode.application.TaxCodeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TaxCodeApplication.class)
class TaxCodeApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
