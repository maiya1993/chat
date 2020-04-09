package com.company.webChat2.service;

import com.company.webChat2.domain.Message;
import com.company.webChat2.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public void deleteMessage(String id) {
        Optional<Message> message = messageRepo.findById(Long.valueOf(id));

        if (message.isPresent()) {
            Message messageForDelete = message.get();
            messageRepo.delete(messageForDelete);
        }
        else {
            System.out.println("Сообщение не найдено");
        }

    }

}

