package com.example.springbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendBalanceUpdate(Long customerId, String msg) {
        messagingTemplate.convertAndSend(
                "/topic/balance/" + customerId,
                msg
        );
    }
}
