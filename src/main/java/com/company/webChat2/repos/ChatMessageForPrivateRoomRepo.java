package com.company.webChat2.repos;

import com.company.webChat2.domain.ChatMessageForPrivateRoom;
import com.company.webChat2.domain.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageForPrivateRoomRepo extends CrudRepository<ChatMessageForPrivateRoom, Long> {
    Optional<ChatMessageForPrivateRoom> findById(Long id);
    List<ChatMessageForPrivateRoom> findAllByRoomId (Room room);
}