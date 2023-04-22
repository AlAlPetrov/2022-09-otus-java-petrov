package ru.otus.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.api.ResourceNotFoundException;
import ru.otus.api.TariffController;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffRequest;
import ru.otus.service.DataStore;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Tariff controller tests")
class TariffTest {
    static class SavedTariff extends Tariff {
        protected SavedTariff(Long id, Tariff tariff) {
            super(id,
                    tariff.getType(),
                    tariff.getName(),
                    tariff.getPrice(),
                    tariff.getInitialValue());
        }
    }

    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
    }

    @DisplayName("Get tariff with existing id returns tariff")
    @Test
    void getTariffWithExistingIdReturnsTariff() {
        //arrange
        var dataStore = mock(DataStore.class);
        var tariffController = new TariffController(dataStore);
        var tariffFromDb = new Tariff(1L, "test tariff", 123L, 321L);
        given(dataStore.loadTariff(anyLong())).willReturn(Optional.of(tariffFromDb));

        //act
        var tariff = tariffController.getTariffById(1L);

        //assert
        assertThat(tariff).isEqualTo(tariffFromDb);
    }

    @DisplayName("Get tariff with non-existent id throws ResourceNotFound exception")
    @Test
    void getTariffWithNonExistentIdThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var tariffController = new TariffController(dataStore);
        given(dataStore.loadTariff(anyLong())).willReturn(Optional.empty());

        //assert
        assertThatThrownBy(()-> tariffController.getTariffById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("Get all tariffs returns all tariffs from db")
    @Test
    void getAllTariffsReturnsAllTariffsFromDb() {
        //arrange
        var dataStore = mock(DataStore.class);
        var tariffController = new TariffController(dataStore);
        var tariffFromDb = List.of(
                new Tariff(1L, "test tariff", 123L, 321L),
                new Tariff(1L, "test tariff", 123L, 321L));

        given(dataStore.loadTariffs()).willReturn(tariffFromDb);

        //act
        var tariffs = tariffController.getTariffs();

        //assert
        assertThat(tariffs).containsExactlyInAnyOrderElementsOf(tariffFromDb);
    }

    @DisplayName("Save tariff saves tariff to db and returns id")
    @Test
    void saveTariffSavesTariffToDbAndReturnsSavedTariff() {
        //arrange
        var savedTariffId = 123L;
        var receivedTariff = new TariffRequest(132L, "test tariff", 321L, 456L);
        var savedTariff = new SavedTariff(savedTariffId,
                new Tariff(1L, "test tariff", 123L, 321L));
        var dataStore = mock(DataStore.class);
        var tariffController = new TariffController(dataStore);
        given(dataStore.saveTariff(any(Tariff.class))).willReturn(savedTariff);

        //act
        var tariff = tariffController.createTariff(receivedTariff);

        //assert
        assertThat(tariff).isEqualTo(savedTariff);
    }
}