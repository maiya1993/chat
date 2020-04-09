package com.company.webChat2.service;

import com.company.webChat2.domain.CommonChatMessage;
import com.company.webChat2.repos.CommonChatMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonChatMessageService {

    @Autowired
    private CommonChatMessageRepo commonChatMessageRepo;

    public void deleteMessage(String id) {
        Optional<CommonChatMessage> message = commonChatMessageRepo.findById(Long.valueOf(id));

        if (message.isPresent()) {
            CommonChatMessage messageForDelete = message.get();
            commonChatMessageRepo.delete(messageForDelete);
        }
        else {
            System.out.println("Сообщение не найдено");
        }

    }

}