package ru.otus.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import ru.otus.api.TariffController;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffDto;
import ru.otus.service.DataStore;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Tariff controller tests")
class TariffTest {
    class SavedTariff extends Tariff {
        protected SavedTariff(Long id, Tariff tariff) {
            super(id,
                    tariff.getType(),
                    tariff.getName(),
                    tariff.getPrice(),
                    tariff.getInitialValue());
        }
    }

    @BeforeAll
    static void setUp() throws Exception {
    }

    @AfterAll
    static void tearDown() throws Exception {
    }

    @DisplayName("Get tariff returns tariff from db")
    @Test
    void getTariffReturnsTariffFromDb() throws Exception {
        //arrange
        var dataStore = mock(DataStore.class);
        var pool = Schedulers.newParallel("test-pool", 1);
        var tariffController = new TariffController(dataStore, pool);
        var tariff = new Tariff(1L, "test tariff", 123L, 321L);
        given(dataStore.loadTariff(anyInt())).willReturn(Mono.just(tariff));

        //act
        var tariffStream = tariffController.getTariffById(1);

        //assert
        StepVerifier
                .create(tariffStream)
                .consumeNextWith(tariffDto -> {
                    assertThat(tariffDto.type()).isEqualTo(tariff.getType());
                    assertThat(tariffDto.name()).isEqualTo(tariff.getName());
                    assertThat(tariffDto.price()).isEqualTo(tariff.getPrice());
                    assertThat(tariffDto.initialValue()).isEqualTo(tariff.getInitialValue());
                })
                .verifyComplete();
    }

    @DisplayName("Get all tariffs returns all tariffs from db")
    @Test
    void getAllTariffsReturnsAllTariffsFromDb() throws Exception {
        //arrange
        var dataStore = mock(DataStore.class);
        var pool = Schedulers.newParallel("test-pool", 1);
        var tariffController = new TariffController(dataStore, pool);
        var tariffs = List.of(
                new Tariff(1L, "test tariff", 123L, 321L),
                new Tariff(1L, "test tariff", 123L, 321L));

        given(dataStore.loadTariffs()).willReturn(Flux.fromIterable(tariffs));

        //act
        var tariffStream = tariffController.getTariffById();

        //assert
        StepVerifier
                .create(tariffStream)
                .consumeNextWith(tariffDto -> {
                    assertThat(tariffDto.type()).isEqualTo(tariffs.get(0).getType());
                    assertThat(tariffDto.name()).isEqualTo(tariffs.get(0).getName());
                    assertThat(tariffDto.price()).isEqualTo(tariffs.get(0).getPrice());
                    assertThat(tariffDto.initialValue()).isEqualTo(tariffs.get(0).getInitialValue());
                })
                .consumeNextWith(tariffDto -> {
                    assertThat(tariffDto.type()).isEqualTo(tariffs.get(1).getType());
                    assertThat(tariffDto.name()).isEqualTo(tariffs.get(1).getName());
                    assertThat(tariffDto.price()).isEqualTo(tariffs.get(1).getPrice());
                    assertThat(tariffDto.initialValue()).isEqualTo(tariffs.get(1).getInitialValue());
                })
                .verifyComplete();
    }

    @DisplayName("Save tariff saves tariff to db and returns id")
    @Test
    void saveTariffSavesTariffToDbAndReturnsId() throws Exception {
        //arrange
        var savedTariffId = 123L;
        var receivedTariff = new TariffDto(132L, "test tariff", 321L, 456L);
        var tariff = new Tariff(1L, "test tariff", 123L, 321L);
        var savedTariff = new SavedTariff(savedTariffId, tariff);
        var dataStore = mock(DataStore.class);
        var pool = Schedulers.newParallel("test-pool", 1);
        var tariffController = new TariffController(dataStore, pool);
        given(dataStore.saveTariff(any(Tariff.class))).willReturn(Mono.just(savedTariff));

        //act
        var tariffStream = tariffController.createTariff(receivedTariff);

        //assert
        StepVerifier
                .create(tariffStream)
                .consumeNextWith(tariffId -> {
                    assertThat(tariffId).isEqualTo(savedTariffId);
                })
                .verifyComplete();
    }
}