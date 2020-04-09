package com.company.webChat2.repos;

import com.company.webChat2.domain.CommonChatMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommonChatMessageRepo extends CrudRepository<CommonChatMessage, Long> {
    Optional<CommonChatMessage> findById(Long id);
}