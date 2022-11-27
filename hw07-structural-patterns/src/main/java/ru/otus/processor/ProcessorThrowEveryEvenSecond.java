package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowEveryEvenSecond implements Processor {
    private final DateTimeProvider dateTimeProvider;

    private boolean isEvenSecond() {
        return dateTimeProvider.getDateTime().getSecond() % 2 == 0;
    }

    public ProcessorThrowEveryEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (isEvenSecond())
            throw new ProcessorException("even second exception");
        return message;
    }
}
