package ru.otus.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffRequest;
import ru.otus.service.DataStore;

@RestController
@RequestMapping("/api/v1/tariff")
public class TariffController {
    private final DataStore dataStore;

    public TariffController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Tariff createTariff(@RequestBody TariffRequest tariffRequest) {
        return dataStore.saveTariff(new Tariff(tariffRequest.type(),
                        tariffRequest.name(),
                        tariffRequest.price(),
                        tariffRequest.initialValue()));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tariff getTariffById(@PathVariable("id") Long id) {
        return dataStore
                .loadTariff(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Tariff> getTariffs() {
        return dataStore.loadTariffs();
    }
}
