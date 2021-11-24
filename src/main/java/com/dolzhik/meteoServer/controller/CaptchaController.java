package com.dolzhik.meteoServer.controller;

import com.dolzhik.meteoServer.service.CaptchaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaController {

    private final CaptchaProvider captchaProvider;

    @Autowired
    public CaptchaController(CaptchaProvider captchaProvider) {
        this.captchaProvider = captchaProvider;
    }

    @GetMapping("/captcha")
    public Object getCaptcha() {
        return captchaProvider.getCaptcha();
    }
}
