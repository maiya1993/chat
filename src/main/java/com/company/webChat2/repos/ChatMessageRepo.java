package com.company.webChat2.repos;

import com.company.webChat2.domain.ChatMessage;
import com.company.webChat2.domain.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepo extends CrudRepository<ChatMessage, Long> {
    Optional<ChatMessage> findById(Long id);
    List<ChatMessage> findAllByRoomId (Room room);
}
