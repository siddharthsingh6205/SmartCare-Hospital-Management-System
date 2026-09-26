package org.ai.appointmentbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class AppointmentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentBackendApplication.class, args);
    }

}
