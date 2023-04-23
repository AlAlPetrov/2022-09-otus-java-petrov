package ru.otus.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.exception.ResourceNotFoundException;
import ru.otus.api.TariffTypeController;
import ru.otus.domain.TariffType;
import ru.otus.domain.TariffTypeRequest;
import ru.otus.service.DataStore;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("TariffType controller tests")
class TariffTypeTest {
    static class SavedTariffType extends TariffType {
        protected SavedTariffType(Long id, TariffType tariffType) {
            super(id, tariffType.getName());
        }
    }

    @DisplayName("Get tariffType with existing id returns tariffType from db")
    @Test
    void getTariffTypeReturnsTariffTypeFromDb() {
        //arrange
        var dataStore = mock(DataStore.class);
        var tariffTypeController = new TariffTypeController(dataStore);
        var tariffTypeFromDb = new TariffType(123L, "test name");
        given(dataStore.loadTariffType(anyLong())).willReturn(Optional.of(tariffTypeFromDb));

        //act
        var tariff = tariffTypeController.getTariffTypeById(anyLong());

        //assert
        assertThat(tariff).isEqualTo(tariffTypeFromDb);
    }

    @DisplayName("Get tariffType with non-existent id throws ResourceNotFound exception")
    @Test
    void getTariffTypeWithNonExistentIdThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var tariffTypeController = new TariffTypeController(dataStore);
        given(dataStore.loadTariffType(anyLong())).willReturn(Optional.empty());

        //assert
        assertThatThrownBy(()-> tariffTypeController.getTariffTypeById(anyLong()))
                .isInstanceOf(ResourceNotFoundException.class);
    }



    @DisplayName("Save tariff type saves tariff type to db and returns id")
    @Test
    void saveTariffTypeSavesTariffTypeToDbAndReturnsSavedTariffType() {
        //arrange
        var savedTariffTypeId = 123L;
        var receivedTariffType = new TariffTypeRequest("test tariff type");
        var savedTariffType = new SavedTariffType(savedTariffTypeId,
                new TariffType("test tariff"));
        var dataStore = mock(DataStore.class);
        var tariffTypeController = new TariffTypeController(dataStore);
        given(dataStore.saveTariffType(any(TariffType.class))).willReturn(savedTariffType);

        //act
        var tariff = tariffTypeController.createTariffType(receivedTariffType);

        //assert
        assertThat(tariff).isEqualTo(savedTariffType);
    }
}