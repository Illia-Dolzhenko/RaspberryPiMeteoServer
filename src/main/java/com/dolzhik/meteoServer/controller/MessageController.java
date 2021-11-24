package com.dolzhik.meteoServer.controller;

import com.dolzhik.meteoServer.entity.Message;
import com.dolzhik.meteoServer.entity.MessageUI;
import com.dolzhik.meteoServer.repository.MessageRepository;
import com.dolzhik.meteoServer.service.CaptchaProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MessageController {

    private final MessageRepository messageRepository;
    private final CaptchaProvider captchaProvider;
    private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

    @Autowired
    public MessageController(MessageRepository messageRepository, CaptchaProvider captchaProvider){
        this.messageRepository = messageRepository;
        this.captchaProvider = captchaProvider;
    }

    @PostMapping("/sendMessage")
    ResponseEntity<String> postMessage(@Valid @RequestBody MessageUI message) {
        if (captchaProvider.validateCaptcha(message.getCaptchaId(), message.getCaptcha())) {
            messageRepository.save(message.getMessage());
            return ResponseEntity.ok("Message sent");
        }
        return ResponseEntity.badRequest().body("Captcha is not valid");
    }

    @GetMapping("/getMessages")
    Object getMessages() {
        return messageRepository.findAll();
    }
}
