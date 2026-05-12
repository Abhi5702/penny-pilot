package com.abhinavprasad.pennypilot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${mail.from}")
    private String mailFrom;

    public void sendEmail(String to, String subject, String body) {
        String payload = String.format("""
            {
              "sender": {"email": "%s", "name": "Penny Pilot"},
              "to": [{"email": "%s"}],
              "subject": "%s",
              "textContent": "%s"
            }
            """, mailFrom, to, subject, body);

        callBrevoApi(payload);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) {
        // Brevo requires attachments as base64
        String base64Content = Base64.getEncoder().encodeToString(attachment);

        String payload = String.format("""
            {
              "sender": {"email": "%s", "name": "Penny Pilot"},
              "to": [{"email": "%s"}],
              "subject": "%s",
              "textContent": "%s",
              "attachment": [
                {
                  "content": "%s",
                  "name": "%s"
                }
              ]
            }
            """, mailFrom, to, subject, body, base64Content, filename);

        callBrevoApi(payload);
    }

    private void callBrevoApi(String payload) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", brevoApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            restTemplate.postForObject(
                    "https://api.brevo.com/v3/smtp/email",
                    request,
                    String.class
            );
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }
}