package com.scalable.notifications.Controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.scalable.notifications.Models.Note;
import com.scalable.notifications.Services.NotificationsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class NotificationsController {
    private final NotificationsService firebaseService;

    public NotificationsController(NotificationsService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @RequestMapping("/send-notification")
    @ResponseBody
    public String sendNotification(@RequestBody Note note,
                                   @RequestParam String topic) throws FirebaseMessagingException {
        return firebaseService.sendNotification(note, topic);
    }
}
