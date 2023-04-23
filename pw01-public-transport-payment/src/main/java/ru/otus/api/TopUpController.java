package ru.otus.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.*;
import ru.otus.service.TopUpJdbc;

@RestController
@RequestMapping("/api/v1/topUp")
public class TopUpController {
    private static final Logger log = LoggerFactory.getLogger(TopUpController.class);
    private final TopUpJdbc topUpJdbc;

    public TopUpController(TopUpJdbc topUpJdbc) {
        this.topUpJdbc = topUpJdbc;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Account topUpAccount(@RequestBody PaymentRequest paymentRequest) {
        return topUpJdbc.topUpAccount(paymentRequest);
    }
}
