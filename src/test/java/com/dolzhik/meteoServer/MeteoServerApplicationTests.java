package com.dolzhik.meteoServer;

import com.dolzhik.meteoServer.captcha.LocalCaptchaGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeteoServerApplicationTests {

    @Test
    void contextLoads() {
        var captchaGenerator = new LocalCaptchaGenerator();

        captchaGenerator.getCaptcha();
    }

}
