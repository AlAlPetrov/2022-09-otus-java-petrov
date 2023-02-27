package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.domain.Message;

import static ru.otus.domain.Constants.SPECIAL_ROOM_ID;

public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {

    @Query("select * from message where room_id = :room_id or " +
            SPECIAL_ROOM_ID + " = :room_id order by id")
    Flux<Message> findByRoomId(@Param("roomId") String roomId);

}
