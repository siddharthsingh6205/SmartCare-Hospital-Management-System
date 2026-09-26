package org.ai.appointmentbackend.dto;

public  class PaymentDetails {
    private final double amount;
    private final String transactionId;

    public PaymentDetails(double amount, String transactionId) {
        this.amount = amount;
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionId() {
        return transactionId;
    }
}