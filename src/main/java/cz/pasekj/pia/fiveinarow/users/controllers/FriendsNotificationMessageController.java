package cz.pasekj.pia.fiveinarow.users.controllers;

import cz.pasekj.pia.fiveinarow.users.FriendsMessage;
import cz.pasekj.pia.fiveinarow.users.services.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket controller for friends notifications
 */
@Controller
@RequiredArgsConstructor
public class FriendsNotificationMessageController {

    /** Messaging service used for sending user-directed messages */
    private final SimpMessagingTemplate simpMessagingTemplate;
    /** Friends service */
    private final FriendsService friendsService;

    /**
     * Handle friend message using FriendsService and sendscorrespondingg messages to concerned
     * @param msg
     * @param user
     * @param sessionId
     * @throws Exception
     */
    @MessageMapping("/secured/friends")
    public void handleFriendsMessage(@Payload FriendsMessage msg,
                                     Principal user,
                                     @Header("simpSessionId") String sessionId) throws Exception {

        switch (msg.getAction()) {
            case FRIENDS_ADD:
                friendsService.requestFriend(msg.getFrom(), msg.getTo());
                break;
            case FRIENDS_ACCEPT:
                friendsService.confirmFriend(msg.getTo(), msg.getFrom());
                break;
            case FRIENDS_REFUSE:
                friendsService.refuseFriend(msg.getTo(), msg.getFrom());
                break;
            case FRIENDS_REMOVE:
                friendsService.removeFriend(msg.getFrom(), msg.getTo());
                break;
        }

        FriendsMessage outputMessage = new FriendsMessage(msg.getFrom(), msg.getTo(), msg.getAction());
        simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/secured/notification/queue/specific-user", outputMessage);
    }

}
