package com.dolzhik.meteoServer.entity;

public class MessageUI extends Message{
    private String captcha;
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
}
