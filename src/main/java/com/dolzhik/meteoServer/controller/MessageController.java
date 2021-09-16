package com.dolzhik.meteoServer.controller;

import com.dolzhik.meteoServer.entity.MessageUI;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @PostMapping("/sendMessage")
    Object postMessage(@RequestBody MessageUI message){
        return null;
    }
}
