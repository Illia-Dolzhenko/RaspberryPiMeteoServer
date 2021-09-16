package com.dolzhik.meteoServer.service;

import com.dolzhik.meteoServer.entity.Captcha;

public interface CaptchaProvider {

    Captcha getCaptcha();

    boolean validateCaptcha(long id, String answer);

    void removeCaptcha(long id);

}
