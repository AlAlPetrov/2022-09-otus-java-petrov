package ru.otus.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Account;
import ru.otus.domain.PaymentRequest;
import ru.otus.service.PaymentJdbc;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentJdbc paymentJdbc;

    public PaymentController(PaymentJdbc paymentJdbc) {
        this.paymentJdbc = paymentJdbc;
    }

    @PostMapping(value = "/writeOff")
    @ResponseStatus(HttpStatus.CREATED)
    public Account writeOffAccount(@RequestBody PaymentRequest paymentRequest) {
        return paymentJdbc.writeOff(paymentRequest);
    }
}
