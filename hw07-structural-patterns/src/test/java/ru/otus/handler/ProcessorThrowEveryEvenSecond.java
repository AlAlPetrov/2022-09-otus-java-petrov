package ru.otus.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.ProcessorException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProcessorThrowEveryEvenSecond {

    @Test
    @DisplayName("With even second throws")
    void Process_WithEvenSecond_Throws() {
        var dateTimeProviderMock = mock(DateTimeProvider.class);
        when(dateTimeProviderMock.getDateTime())
                .thenReturn(LocalDateTime.ofEpochSecond(2, 0, ZoneOffset.UTC));
        var message = new Message.Builder(1L).field9("field9").build();
        var processor = new ru.otus.processor.ProcessorThrowEveryEvenSecond(dateTimeProviderMock);

        assertThatExceptionOfType(ProcessorException.class)
                .isThrownBy(() -> processor.process(message));
    }

    @Test
    @DisplayName("With odd second returns message")
    void Process_WithOddSecond_ReturnsMessage() {
        var dateTimeProviderMock = mock(DateTimeProvider.class);
        when(dateTimeProviderMock.getDateTime())
                .thenReturn(LocalDateTime.ofEpochSecond(1, 0, ZoneOffset.UTC));
        var message = new Message.Builder(1L).field9("field9").build();
        var processor = new ru.otus.processor.ProcessorThrowEveryEvenSecond(dateTimeProviderMock);

        var result = processor.process(message);

        assertThat(result).isEqualTo(message);
    }
}