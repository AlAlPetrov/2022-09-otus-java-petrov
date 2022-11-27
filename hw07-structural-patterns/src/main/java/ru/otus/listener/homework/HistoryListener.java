package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private HashMap<Long, Message> history = new HashMap<Long, Message>();
    @Override
    public void onUpdated(Message msg) {
        /*
            не очень понятна идея, с 1ой стороны в классе Message есть
            hash, видимо предполагается его использовать в качестве ключа, но тогда
            надо в методе findMessageById либо конструировать Message чтобы получить хэш
            и получается что мы должны знать о внутренней реализации message-а, что неправильно,
            либо делать дополнительно статический метод рассчета ключа с аргументом
            что так же не выглядит правильным решением

            есть опасения, что Message с 1м и тем же id может несколько раз быть передан в onUpdate
            тогда надо по-другому его хранить, но метод findMessageById возвращает 1 значение т.е.,
            видимо опасения необоснованы
        */
        history.put(msg.getId(), msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}
