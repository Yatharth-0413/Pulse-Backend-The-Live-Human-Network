package com.pulse.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest {

    @PostConstruct
    public void test() {
        System.out.println(
                "PostgreSQL Connected Successfully"
        );
    }
}