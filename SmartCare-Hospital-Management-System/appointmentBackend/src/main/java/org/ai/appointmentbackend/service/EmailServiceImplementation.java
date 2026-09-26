package org.ai.appointmentbackend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.ai.appointmentbackend.dto.PaymentDetails;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImplementation implements EmailService{
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;



    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }




    @Override
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        // Create MimeMessage for multipart email
        MimeMessage message = mailSender.createMimeMessage();

        // Create MimeMessageHelper for easy manipulation of the message
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' means multipart


        helper.setTo(to);  // Recipient's email address

        // Set the subject of the email
        helper.setSubject(subject);

        // Set the email body, both HTML and plain text versions
        helper.setText(body, true); // true means HTML content
        // If you want to add plain text as well, uncomment the line below:
        // helper.setText("Your plain text version of the body.", false);

        // Send the email
        mailSender.send(message);


    }

    public void sendAppointmentConfirmation(AppointmentEntity appointment) throws MessagingException {
        UserEntity patient = appointment.getPatient().getUser();
        UserEntity doctor = appointment.getDoctor().getUser();

        String subject = "Appointment Confirmation with Dr. " + doctor.getName();

        String htmlContent = buildAppointmentConfirmationEmail(
                patient.getName(),
                doctor.getName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getDoctor().getFees(),
                appointment.getId()
        );


        sendHtmlEmail(patient.getEmail(), subject,  htmlContent);
    }

    private String buildAppointmentConfirmationEmail(String patientName, String doctorName,
                                                     LocalDate date, LocalTime time, Long fees, Long appointmentId) {

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
                "        .header { background-color: #5f6fff; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }" +
                "        .content { padding: 20px; }" +
                "        .button { background-color: #5f6fff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Appointment Confirmation</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear " + patientName + ",</p>" +
                "            <p>Your appointment with <strong>Dr. " + doctorName + "</strong> has been confirmed.</p>" +
                "            <p><strong>Date:</strong> " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) + "</p>" +
                "            <p><strong>Time:</strong> " + time.format(DateTimeFormatter.ofPattern("h:mm a")) + "</p>" +
                "            <p><strong>Consultation Fee:</strong> Rs." + fees + "</p>" +
                "            <p>Please complete the payment to secure your appointment:</p>" +
                "            <p>If you have any questions, please contact our support team.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© 2023 Your Healthcare App. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    @Override
    public void sendAppointmentComplete(AppointmentEntity appointment) throws MessagingException {
        UserEntity patient = appointment.getPatient().getUser();
        UserEntity doctor = appointment.getDoctor().getUser();

        String subject = "Appointment Confirmation with Dr. " + doctor.getName();

        String htmlContent = buildAppointmentCompleteEmail(
                patient.getName(),
                doctor.getName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getDoctor().getFees(),
                appointment.getId()
        );


        sendHtmlEmail(patient.getEmail(), subject,  htmlContent);

    }


    private String buildAppointmentCompleteEmail(String patientName, String doctorName,
                                                     LocalDate date, LocalTime time, Long fees, Long appointmentId) {

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
                "        .header { background-color: #5f6fff; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }" +
                "        .content { padding: 20px; }" +
                "        .button { background-color: #5f6fff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Appointment Confirmation</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear " + patientName + ",</p>" +
                "            <p>Your appointment with <strong>Dr. " + doctorName + "</strong> has been confirmed.</p>" +
                "            <p><strong>Date:</strong> " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) + "</p>" +
                "            <p><strong>Time:</strong> " + time.format(DateTimeFormatter.ofPattern("h:mm a")) + "</p>" +
                "            <p><strong>Consultation Fee:</strong> P" + fees + "</p>" +
                "            <p>Your appointment process successfully completed :</p>" +
                "            <p>The doctor has received your amount </p>" +
                "            <p>If you have any questions, please contact our support team.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© 2023 Your Healthcare App. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }





    public void sendAppointmentRejection(AppointmentEntity appointment) throws MessagingException {
        UserEntity patient = appointment.getPatient().getUser();
        UserEntity doctor = appointment.getDoctor().getUser();

        String subject = "Appointment Request Declined by Dr. " + doctor.getName();

        String htmlContent = buildAppointmentRejectionEmail(
                patient.getName(),
                doctor.getName(),
                appointment.getDate(),
                appointment.getTime()
        );

        sendHtmlEmail(patient.getEmail(), subject, htmlContent);
    }

    @Override
    public void sendPaymentConfirmation(AppointmentEntity appointment, PaymentDetails paymentDetails) throws MessagingException {
        UserEntity patient = appointment.getPatient().getUser();
        UserEntity doctor = appointment.getDoctor().getUser();

        String subject = "Payment Confirmation for Appointment with Dr. " + doctor.getName();

        String htmlContent = buildPaymentConfirmationEmail(
                patient.getName(),
                doctor.getName(),
                appointment.getDate(),
                appointment.getTime(),
                paymentDetails.getAmount(),
                paymentDetails.getTransactionId(),
                appointment.getId()
        );

        sendHtmlEmail(patient.getEmail(), subject, htmlContent);
    }

    private String buildPaymentConfirmationEmail(String patientName, String doctorName,
                                                 LocalDate date, LocalTime time,
                                                 double amount, String transactionId,
                                                 Long appointmentId) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
                "        .header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }" +
                "        .content { padding: 20px; }" +
                "        .receipt { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 15px 0; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Payment Confirmed</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear " + patientName + ",</p>" +
                "            <p>Thank you for your payment. Your appointment with <strong>Dr. " + doctorName + "</strong> is now confirmed.</p>" +
                "            <div class=\"receipt\">" +
                "                <h3>Payment Receipt</h3>" +
                "                <p><strong>Appointment ID:</strong> " + appointmentId + "</p>" +
                "                <p><strong>Date:</strong> " + date.format(dateFormatter) + "</p>" +
                "                <p><strong>Time:</strong> " + time.format(timeFormatter) + "</p>" +
                "                <p><strong>Amount Paid:</strong> Rs." + String.format("%.2f", amount) + "</p>" +
                "                <p><strong>Transaction ID:</strong> " + transactionId + "</p>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© 2023 Your Healthcare App. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
    @Override
    public void sendDoctorNotification(AppointmentEntity appointment, PaymentDetails paymentDetails) throws MessagingException {
        UserEntity patient = appointment.getPatient().getUser();
        UserEntity doctor = appointment.getDoctor().getUser();

        String subject = "New Appointment Booking - Payment Received";

        String htmlContent = buildDoctorNotificationEmail(
                patient.getName(),
                doctor.getName(),
                appointment.getDate(),
                appointment.getTime(),
                paymentDetails.getAmount(),
                paymentDetails.getTransactionId(),
                appointment.getId()
        );

        sendHtmlEmail(doctor.getEmail(), subject, htmlContent);
    }



    private String buildDoctorNotificationEmail(String patientName, String doctorName,
                                                LocalDate date, LocalTime time,
                                                double amount, String transactionId,
                                                Long appointmentId) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
                "        .header { background-color: #2196F3; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }" +
                "        .content { padding: 20px; }" +
                "        .details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 15px 0; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>New Appointment Booking</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear Dr. " + doctorName + ",</p>" +
                "            <p>You have a new appointment booking with <strong>" + patientName + "</strong>.</p>" +
                "            <div class=\"details\">" +
                "                <h3>Appointment Details</h3>" +
                "                <p><strong>Appointment ID:</strong> " + appointmentId + "</p>" +
                "                <p><strong>Date:</strong> " + date.format(dateFormatter) + "</p>" +
                "                <p><strong>Time:</strong> " + time.format(timeFormatter) + "</p>" +
                "                <p><strong>Payment Received:</strong> Rs." + String.format("%.2f", amount) + "</p>" +
                "                <p><strong>Transaction ID:</strong> " + transactionId + "</p>" +
                "            </div>" +
                "            <p>Please prepare for the upcoming appointment.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© 2023 Your Healthcare App. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String buildAppointmentRejectionEmail(String patientName, String doctorName,
                                                  LocalDate date, LocalTime time) {

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }" +
                "        .header { background-color: #f44336; color: white; padding: 10px; text-align: center; border-radius: 5px 5px 0 0; }" +
                "        .content { padding: 20px; }" +
                "        .button { background-color: #5f6fff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Appointment Declined</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear " + patientName + ",</p>" +
                "            <p>We regret to inform you that your appointment request with <strong>Dr. " + doctorName + "</strong> has been declined.</p>" +
                "            <p><strong>Requested Date:</strong> " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) + "</p>" +
                "            <p><strong>Requested Time:</strong> " + time.format(DateTimeFormatter.ofPattern("h:mm a")) + "</p>" +
                "            <p>We apologize for any inconvenience caused.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© 2023 Your Healthcare App. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }




    public void sendAppointmentCancellation(AppointmentEntity appointment, boolean refund, boolean byDoctor) throws MessagingException {
        UserEntity patient = appointment.getPatient().getUser();
        UserEntity doctor = appointment.getDoctor().getUser();

        // Send to patient
        String patientSubject = byDoctor ?
                "Appointment Cancelled by Doctor" :
                "Your Appointment Cancellation Confirmation";
        String patientHtml = buildPatientCancellationEmail(
                patient.getName(),
                doctor.getName(),
                appointment.getDate(),
                appointment.getTime(),
                byDoctor,
                refund,
                appointment.getAmount()
        );
        sendHtmlEmail(patient.getEmail(), patientSubject, patientHtml);

        // Send to doctor if cancelled by patient
        if (!byDoctor) {
            String doctorSubject = "Appointment Cancelled by Patient";
            String doctorHtml = buildDoctorCancellationEmail(
                    doctor.getName(),
                    patient.getName(),
                    appointment.getDate(),
                    appointment.getTime(),
                    refund,
                    appointment.getAmount()
            );
            sendHtmlEmail(doctor.getEmail(), doctorSubject, doctorHtml);
        }
    }



    private String buildPatientCancellationEmail(String patientName, String doctorName,
                                                 LocalDate date, LocalTime time,
                                                 boolean byDoctor,
                                                 boolean refund,
                                                 double amount) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        String cancellationReason = byDoctor ?
                "Dr. " + doctorName + " has cancelled your appointment due to unforeseen circumstances. We sincerely apologize for this inconvenience." :
                "You have successfully cancelled your appointment with Dr. " + doctorName + ".";

        String refundSection = "";
        if (refund) {
            refundSection = "<div style=\"background-color: #e8f5e9; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #4caf50;\">" +
                    "<h3 style=\"margin-top: 0; color: #2e7d32;\">Refund Information</h3>" +
                    "<p>A refund of <strong>Rs." + String.format("%.2f", amount) + "</strong> will be processed to your original payment method within 5-7 business days.</p>" +
                    "<p>You'll receive a confirmation email once the refund is processed.</p>" +
                    "</div>";
        } else if (!byDoctor) {
            refundSection = "<div style=\"background-color: #fff3e0; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #ffa000;\">" +
                    "<p>For refund assistance, please contact our support team at support@yourapp.com or call +1 (555) 123-4567.</p>" +
                    "</div>";
        }

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }" +
                "        .container { max-width: 600px; margin: 20px auto; padding: 0; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }" +
                "        .header { background-color: #f44336; color: white; padding: 20px; text-align: center; }" +
                "        .content { padding: 25px; background-color: #ffffff; }" +
                "        .details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #f44336; }" +
                "        .button { background-color: #4285f4; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; display: inline-block; font-weight: bold; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; padding: 15px; background-color: #f5f5f5; }" +
                "        .signature { margin-top: 20px; color: #555; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Appointment Cancellation</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear " + patientName + ",</p>" +
                "            <p>" + cancellationReason + "</p>" +
                "            <div class=\"details\">" +
                "                <h3 style=\"margin-top: 0; color: #f44336;\">Appointment Details</h3>" +
                "                <p><strong>Doctor:</strong> Dr. " + doctorName + "</p>" +
                "                <p><strong>Date:</strong> " + date.format(dateFormatter) + "</p>" +
                "                <p><strong>Time:</strong> " + time.format(timeFormatter) + "</p>" +
                "                <p><strong>Amount:</strong> Rs." + String.format("%.2f", amount) + "</p>" +
                "            </div>" +
                refundSection +

                "            <div class=\"signature\">" +
                "                <p>Thank you for choosing our service.</p>" +
                "                <p><strong>The Healthcare Team</strong></p>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© " + LocalDate.now().getYear() + " Your Healthcare App. All rights reserved.</p>" +
                "            <p>If you have any questions, please contact support@yourapp.com</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String buildDoctorCancellationEmail(String doctorName, String patientName,
                                                LocalDate date, LocalTime time,
                                                boolean refund,
                                                double amount) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        String refundNote = refund ?
                "<p>A refund of Rs." + String.format("%.2f", amount) + " has been initiated for the patient.</p>" :
                "<p>No refund was required for this cancellation.</p>";

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }" +
                "        .container { max-width: 600px; margin: 20px auto; padding: 0; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }" +
                "        .header { background-color: #4285f4; color: white; padding: 20px; text-align: center; }" +
                "        .content { padding: 25px; background-color: #ffffff; }" +
                "        .details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #4285f4; }" +
                "        .refund-info { background-color: #e8f5e9; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #4caf50; }" +
                "        .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; padding: 15px; background-color: #f5f5f5; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h2>Appointment Cancellation Notice</h2>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Dear Dr. " + doctorName + ",</p>" +
                "            <p>Your patient " + patientName + " has cancelled their upcoming appointment.</p>" +
                "            <div class=\"details\">" +
                "                <h3 style=\"margin-top: 0; color: #4285f4;\">Appointment Details</h3>" +
                "                <p><strong>Patient:</strong> " + patientName + "</p>" +
                "                <p><strong>Date:</strong> " + date.format(dateFormatter) + "</p>" +
                "                <p><strong>Time:</strong> " + time.format(timeFormatter) + "</p>" +
                "                <p><strong>Amount:</strong> Rs." + String.format("%.2f", amount) + "</p>" +
                "            </div>" +
                "            <div class=\"refund-info\">" +
                "                <h3 style=\"margin-top: 0; color: #2e7d32;\">Refund Status</h3>" +
                refundNote +
                "            </div>" +
                "            <p>The time slot has been made available for other patients to book.</p>" +
                "            <p>You can view your updated schedule at any time through your provider dashboard.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© " + LocalDate.now().getYear() + " Your Healthcare App. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }



    @Override
    public void sendDoctorRegistraionEmail(UserEntity user, String password) throws MessagingException {


        String subject = "Welcome to Our Healthcare System - Doctor Account Created";

        String htmlContent = buildDoctorRegistrationEmail(
        user,password
        );

        sendHtmlEmail(user.getEmail(), subject, htmlContent);


    }

    private String buildDoctorRegistrationEmail(UserEntity user, String password) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #4a90e2; color: white; padding: 10px; text-align: center; }
                .content { padding: 20px; }
                .credentials { background-color: #f5f5f5; padding: 15px; border-radius: 5px; }
                .doctor-info { margin-top: 20px; }
                .footer { margin-top: 20px; font-size: 0.9em; text-align: center; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h2>Welcome to Our Healthcare System</h2>
                </div>
                <div class="content">
                    <p>Dear Dr. %s,</p>
                    <p>We are pleased to welcome you to our healthcare system as a registered doctor.</p>
                    
                    <div class="credentials">
                        <p><strong>Login Credentials:</strong></p>
                        <p>Email: %s</p>
                        <p>Temporary Password: %s</p>
>
                                                   
                    </div>
                    
                  
                    <p>Please log in to complete your profile setup and verify your availability schedule.</p>
                    <p>For any assistance, please contact our administrator.</p>
                </div>
                <div class="footer">
                    <p>© 2023 Healthcare System. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                user.getName(),
                user.getEmail(),
                password);


    }

    @Override
    public void sendAdminConfirmationEmail(UserEntity user, Role role) throws MessagingException {

        String subject = "Welcome to Our Healthcare System -  Admin confirmation ";

        String htmlContent = buildAdminConfirmationEmail(
                user,role
        );

        sendHtmlEmail(user.getEmail(), subject, htmlContent);
        

    }

    private String buildAdminConfirmationEmail(UserEntity user, Role role) {
        return """
    <!DOCTYPE html>
    <html>
    <head>
        <style>
            body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
            .container { max-width: 600px; margin: 0 auto; padding: 20px; }
            .header { background-color: #4a90e2; color: white; padding: 10px; text-align: center; }
            .content { padding: 20px; }
            .credentials { background-color: #f5f5f5; padding: 15px; border-radius: 5px; }
            .doctor-info { margin-top: 20px; }
            .footer { margin-top: 20px; font-size: 0.9em; text-align: center; color: #777; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h2>Welcome to Our Healthcare System</h2>
            </div>
            <div class="content">
                <p>Dear Administrator,</p>
                <p>A new user has been registered in the healthcare system with %s privileges.</p>
                
                <div class="credentials">
                    <p><strong>User Details:</strong></p>
                    <p>Name: %s</p>
                    <p>Email: %s</p>
                    <p>Role: %s</p>
                </div>
                
                <p>Please review this registration in the admin portal.</p>
                <p>For any questions, please contact the system administrator.</p>
            </div>
            <div class="footer">
                <p>© 2023 Healthcare System. All rights reserved.</p>
            </div>
        </div>
    </body>
    </html>
    """.formatted(
                role.toString().toLowerCase(),
                user.getName(),
                user.getEmail(),
                role.toString());
    }

    @Override
    public void sendAdminRegistrationEmail(UserEntity user, String password) throws MessagingException {
        String subject = "Welcome to Our Healthcare System - Admin Account Created";

        String htmlContent = buildAdminRegistrationEmail(
                user,password
        );

        sendHtmlEmail(user.getEmail(), subject, htmlContent);
    }

    private String buildAdminRegistrationEmail(UserEntity user, String password) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #4a90e2; color: white; padding: 10px; text-align: center; }
                .content { padding: 20px; }
                .credentials { background-color: #f5f5f5; padding: 15px; border-radius: 5px; }
                .doctor-info { margin-top: 20px; }
                .footer { margin-top: 20px; font-size: 0.9em; text-align: center; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h2>Welcome to Our Healthcare System</h2>
                </div>
                <div class="content">
                    <p>Dear Dr. %s,</p>
                    <p>We are pleased to welcome you to our healthcare system as a registered admin.</p>
                    
                    <div class="credentials">
                        <p><strong>Login Credentials:</strong></p>
                        <p>Email: %s</p>
                        <p>Temporary Password: %s</p>
                            <p>Please use the following link to access your account:</p>
                                                    <a href="http://localhost:5174/login" class="button">Login to Your Account</a>
                    </div>
                    
                  
                    <p>Please log in to complete your profile setup and verify your availability schedule.</p>
                    <p>For any assistance, please contact our administrator.</p>
                </div>
                <div class="footer">
                    <p>© 2023 Healthcare System. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                user.getName(),
                user.getEmail(),
                password);
    }


}
