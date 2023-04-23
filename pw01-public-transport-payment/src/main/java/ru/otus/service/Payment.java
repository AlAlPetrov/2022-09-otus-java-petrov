package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Account;
import ru.otus.domain.PaymentRequest;

@Service
public interface Payment {
    Account writeOff(PaymentRequest paymentRequest);
}
