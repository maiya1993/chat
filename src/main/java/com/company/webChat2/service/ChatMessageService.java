package com.company.webChat2.service;

import com.company.webChat2.domain.ChatMessage;
import com.company.webChat2.domain.Room;
import com.company.webChat2.repos.ChatMessageRepo;
import com.company.webChat2.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepo chatMessageRepo;

    @Autowired
    private RoomRepo roomRepo;

    public void deleteMessagesByRoomId(String roomId) {
        Optional<Room> thisRoom = roomRepo.findById(Long.valueOf(roomId));

        if (thisRoom.isPresent()) {
            List<ChatMessage> chatMessageList = chatMessageRepo
                    .findAllByRoomId(thisRoom.get());
            for (ChatMessage message : chatMessageList) {
                chatMessageRepo.delete(message);
            }
        }
        else {
            System.out.println("Комната не найдена");
        }
    }

    public void deleteMessage(String id) {
        Optional<ChatMessage> message = chatMessageRepo.findById(Long.valueOf(id));

        if (message.isPresent()) {
            ChatMessage messageForDelete = message.get();
            chatMessageRepo.delete(messageForDelete);
        }
        else {
            System.out.println("Сообщение не найдено");
        }

    }

}