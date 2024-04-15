package com.example.demo.socket;

import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;


/*
* WebSocket Handler 작성
* 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
* TextWebSocketHandler를 상속받아 핸들러 작성
* 클라이언트로 받은 메세지를 log로 출력하고 클라이언트로 환영 메세지를 보내줌
* */
@Slf4j
@Component
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    private final Map<String, User> sessionToUser = new HashMap<>();

    @GetMapping
    public List<String> allRoomList(){
        List<String> list = new ArrayList<>();
        Set<Long> ids = chatRoomSessionMap.keySet();
        for (Long aLong : ids) {
            list.add(String.valueOf(aLong));
        }
        return list;
    }

    @GetMapping("/{roomId}")
    public List<String> roomSessionList(@PathVariable String roomId){
        List<String> list = new ArrayList<>();
        long sessionId = Long.parseLong(roomId);
        if(!chatRoomSessionMap.containsKey(sessionId)){
            return null;
        }

        Set<WebSocketSession> webSocketSessions = chatRoomSessionMap.get(sessionId);
        for (WebSocketSession socketSession : webSocketSessions) {
            list.add(socketSession.getId());
        }
        return list;
    }

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결됨", session.getId());
        //세션과 유저정보를 매핑한다 -> 온라인인지 아닌지 판별하게 해준다
        sessions.add(session);
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        log.info("session {}", chatMessageDto.toString());

        Long chatRoomId = chatMessageDto.getChatRoomId();
        if(!chatRoomSessionMap.containsKey(chatRoomId)){
            chatRoomSessionMap.put(chatRoomId,new HashSet<>());
        }

        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
                chatRoomSession.add(session);
        }

        // 하나의 세션에 2명만 참여가능
        if (chatRoomSession.size()>=3) {
            removeClosedSession(chatRoomSession);
        }

        sendMessageToChatRoom(chatMessageDto, chatRoomSession);

    }

    // 소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    // ====== 채팅 관련 메소드 ======
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));//2
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}


// 선생님이 빙고 게임 방을 만들어야 아이의 화면에 활성화가 된다
// 선생님은 많은 아이들을 담당하고 있다
// 소켓을 만들때 선생님 아이디, 아이아이디를 포함시키고 소켓목록을 불러올떄 해당 아이디를 가지고 있는 아이에게만 보이게한다.