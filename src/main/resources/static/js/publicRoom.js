'use strict';


var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('#connecting');
var usersAttention = document.querySelector("#usersAttention");
var stompClient = null;
var username = null;
var namePublicRoom = null;

function connect() {
    username = document.querySelector('#username').innerText.trim();

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

// Connect to WebSocket Server.
connect();

function onConnected() {
    // Subscribe to the Public Topic
    namePublicRoom = window.location.pathname.split('/')[2];
    stompClient.subscribe('/topic/publicChatRoom/' + namePublicRoom, onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat/" + namePublicRoom + "/addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Невозможно соединиться с сервером. Пожалуйста перезагрузите эту страницу и попробуйте снова!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    var messageToUser;
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            messageToUser: messageToUser,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat/" + namePublicRoom + "/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if (message.messageToUser == null) {

        var messageElement = document.createElement('li');

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' присоединился(лась) к чату';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' покинул(а) чат';
        } else {
            messageElement.classList.add('chat-message');
            var usernameElement = document.createElement('strong');
            usernameElement.classList.add('nickname');
            var usernameText = document.createTextNode(message.sender);
            var usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        var textElement = document.createElement('span');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }

    if (message.messageToUser != null){
        var messageUpdate = document.createElement('div');
        messageUpdate.classList.add('message-user-to-update');
        var textOfAttention = document.createTextNode(message.messageToUser);
        messageUpdate.append(textOfAttention);
        usersAttention.append(messageUpdate);
        usersAttention.scrollTop = usersAttention.scrollHeight;
    }

}


messageForm.addEventListener('submit', sendMessage, true);