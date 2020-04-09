package com.company.webChat2.repos;

import com.company.webChat2.domain.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepo extends CrudRepository<Room, Long> {
    Optional<Room> findById(Long id);
    Room findByName(String name);
    List<Room> findByIsPrivate(boolean isPrivate);
}
