package com.example.cryptowatcher.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ScheduledTask {

    private final NotifyService notifyService;
    private final ActualCoinService actualCoinService;

    @Scheduled(fixedRate = 60000)
    public void notifyTask() throws IOException {
        actualCoinService.update();
        notifyService.notifyUser();
    }
}
