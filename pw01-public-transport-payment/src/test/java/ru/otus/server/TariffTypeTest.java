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
import ru.otus.api.TariffTypeController;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffDto;
import ru.otus.domain.TariffType;
import ru.otus.domain.TariffTypeDto;
import ru.otus.service.DataStore;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("TariffType controller tests")
class TariffTypeTest {
    class SavedTariffType extends TariffType {
        protected SavedTariffType(Long id, TariffType tariffType) {
            super(id, tariffType.getName());
        }
    }

    @BeforeAll
    static void setUp() throws Exception {
    }

    @AfterAll
    static void tearDown() throws Exception {
    }

    @DisplayName("Get tariffType returns tariffType from db")
    @Test
    void getTariffTypeReturnsTariffTypeFromDb() throws Exception {
        //arrange
        var dataStore = mock(DataStore.class);
        var pool = Schedulers.newParallel("test-pool", 1);
        var tariffTypeController = new TariffTypeController(dataStore, pool);
        var tariffType = new TariffType(1L, "test tariff type");
        given(dataStore.loadTariffType(anyLong())).willReturn(Mono.just(tariffType));

        //act
        var tariffStream = tariffTypeController.getTariffTypeById(1L);

        //assert
        StepVerifier
                .create(tariffStream)
                .consumeNextWith(tariffTypeDto -> {
                    assertThat(tariffTypeDto.name()).isEqualTo(tariffType.getName());
                })
                .verifyComplete();
    }

    @DisplayName("Save tariff type saves tariff type to db and returns id")
    @Test
    void saveTariffSavesTariffToDbAndReturnsId() throws Exception {
        //arrange
        var savedTariffTypeId = 123L;
        var receivedTariffType = new TariffTypeDto("test tariff type");
        var tariff = new TariffType("test tariff");
        var savedTariffType = new SavedTariffType(savedTariffTypeId, tariff);
        var dataStore = mock(DataStore.class);
        var pool = Schedulers.newParallel("test-pool", 1);
        var tariffTypeController = new TariffTypeController(dataStore, pool);
        given(dataStore.saveTariffType(any(TariffType.class))).willReturn(Mono.just(savedTariffType));

        //act
        var tariffStream = tariffTypeController.createTariffType(receivedTariffType);

        //assert
        StepVerifier
                .create(tariffStream)
                .consumeNextWith(tariffId -> {
                    assertThat(tariffId).isEqualTo(savedTariffTypeId);
                })
                .verifyComplete();
    }
}