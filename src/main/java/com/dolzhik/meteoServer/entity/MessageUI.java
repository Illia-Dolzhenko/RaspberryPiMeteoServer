package com.dolzhik.meteoServer.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;

public class MessageUI extends Message {
    @NotBlank(message = "Captcha cannot be empty")
    private String captcha;
    @NotNull(message = "Captcha id cannot be empty")
    private Long captchaId;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Long getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(Long captchaId) {
        this.captchaId = captchaId;
    }

    public Message getMessage() {
        var message = new Message();
        message.setText(this.getText());
        message.setUserName(this.getUserName());
        message.setCreated(new Timestamp(Instant.now().toEpochMilli()));
        return message;
    }

}
