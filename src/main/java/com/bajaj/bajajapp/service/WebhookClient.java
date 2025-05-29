package com.bajaj.bajajapp.service;

import com.bajaj.bajajapp.model.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void triggerChallenge() {
        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "John Doe");
        requestBody.put("regNo", "REG12347");
        requestBody.put("email", "john@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(generateUrl, entity, WebhookResponse.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to generate webhook");
        }

        String accessToken = response.getBody().getAccessToken();
        String webhookUrl = response.getBody().getWebhook();

        String finalSQLQuery = """
            SELECT
                p.amount AS SALARY,
                CONCAT(e.first_name, ' ', e.last_name) AS NAME,
                TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS AGE,
                d.department_name AS DEPARTMENT_NAME
            FROM payments p
            JOIN employee e ON p.emp_id = e.emp_id
            JOIN department d ON e.department = d.department_id
            WHERE DAY(p.payment_time) != 1
            ORDER BY p.amount DESC
            LIMIT 1
            """;

        Map<String, String> finalPayload = new HashMap<>();
        finalPayload.put("finalQuery", finalSQLQuery);

        HttpHeaders finalHeaders = new HttpHeaders();
        finalHeaders.setContentType(MediaType.APPLICATION_JSON);
        finalHeaders.setBearerAuth(accessToken); // JWT Token

        HttpEntity<Map<String, String>> finalEntity = new HttpEntity<>(finalPayload, finalHeaders);
        ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, finalEntity, String.class);

        System.out.println("Submitted SQL Query. Status: " + submitResponse.getStatusCode());
        System.out.println("Response: " + submitResponse.getBody());
    }
}
