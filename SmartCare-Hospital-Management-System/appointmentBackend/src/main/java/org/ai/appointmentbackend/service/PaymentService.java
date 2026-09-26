package org.ai.appointmentbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.ai.appointmentbackend.dto.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


public interface PaymentService {
    Response verifyStripePayment(Map<String, String> payload);
    Response createStripeSession( Map<String, String> payload,
                                  HttpServletRequest request);
}
