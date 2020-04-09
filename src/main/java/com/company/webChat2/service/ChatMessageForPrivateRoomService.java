package com.company.webChat2.service;

import com.company.webChat2.domain.ChatMessageForPrivateRoom;
import com.company.webChat2.domain.Room;
import com.company.webChat2.repos.ChatMessageForPrivateRoomRepo;
import com.company.webChat2.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageForPrivateRoomService {

    @Autowired
    private ChatMessageForPrivateRoomRepo chatMessageForPrivateRoomRepo;

    @Autowired
    private RoomRepo roomRepo;

    public void deleteMessagesByRoomId(String roomId) {

        Optional<Room> thisRoom = roomRepo.findById(Long.valueOf(roomId));

        if (thisRoom.isPresent()) {
            List<ChatMessageForPrivateRoom> chatMessageForPrivateRoomList = chatMessageForPrivateRoomRepo
                    .findAllByRoomId(thisRoom.get());
            for (ChatMessageForPrivateRoom message: chatMessageForPrivateRoomList) {
                chatMessageForPrivateRoomRepo.delete(message);
            }
        }
        else {
            System.out.println("Комната не найдена");
        }

    }

    public void deleteMessage(String id) {
        Optional<ChatMessageForPrivateRoom> message = chatMessageForPrivateRoomRepo.findById(Long.valueOf(id));

        if (message.isPresent()) {
            ChatMessageForPrivateRoom messageForDelete = message.get();
            chatMessageForPrivateRoomRepo.delete(messageForDelete);
        }
        else {
            System.out.println("Сообщение не найдено");
        }

    }

}
