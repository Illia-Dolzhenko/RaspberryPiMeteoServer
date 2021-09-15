package com.dolzhik.meteoServer.captcha;

import org.springframework.stereotype.Service;

@Service
public interface CaptchaProvider {

    Captcha getCaptcha();

    boolean validateCaptcha(long id, String answer);

    void removeCaptcha(long id);

}
