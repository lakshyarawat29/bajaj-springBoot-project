package com.bajaj.bajajapp;

import com.bajaj.bajajapp.service.WebhookClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final WebhookClient webhookClient;

    public StartupRunner(WebhookClient webhookClient) {
        this.webhookClient = webhookClient;
    }

    @Override
    public void run(String... args) {
        webhookClient.triggerChallenge();
    }
}
