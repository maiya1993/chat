package com.company.webChat2.repos;

import com.company.webChat2.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends CrudRepository<Message, Long> {
    Optional<Message> findById(Long id);
    List<Message> findByTag(String tag);
}