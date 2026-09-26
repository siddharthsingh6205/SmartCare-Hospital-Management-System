package org.ai.appointmentbackend.dto;

public class PaymentStatus {

    private Long paid;
    private Long pending;

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }

    public Long getPending() {
        return pending;
    }

    public void setPending(Long pending) {
        this.pending = pending;
    }
}
