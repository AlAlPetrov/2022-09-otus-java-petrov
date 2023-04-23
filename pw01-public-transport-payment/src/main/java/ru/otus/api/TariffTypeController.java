package ru.otus.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.TariffType;
import ru.otus.domain.TariffTypeRequest;
import ru.otus.exception.ResourceNotFoundException;
import ru.otus.service.DataStore;

@RestController
@RequestMapping("/api/v1/tariffType")
public class TariffTypeController {
    private final DataStore dataStore;

    public TariffTypeController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TariffType createTariffType(@RequestBody TariffTypeRequest tariffTypeRequest) {
        return dataStore.saveTariffType(new TariffType(tariffTypeRequest.name()));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TariffType getTariffTypeById(@PathVariable("id") Long id) {
        return  dataStore
                .loadTariffType(id)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
