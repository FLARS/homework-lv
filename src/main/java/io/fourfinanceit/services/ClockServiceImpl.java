package io.fourfinanceit.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ClockServiceImpl implements ClockService {

    @Override
    public LocalTime getLocalDateNow() {
        return LocalDateTime.now().toLocalTime();
    }

}
