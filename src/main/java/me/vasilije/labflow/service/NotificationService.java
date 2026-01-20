package me.vasilije.labflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate template;

    public void sendNotification(long userId, long testId, String message) {
        template.convertAndSendToUser(String.valueOf(userId), "/schedule/" + testId, message);
    }
}
