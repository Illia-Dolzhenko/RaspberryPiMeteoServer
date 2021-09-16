package com.dolzhik.meteoServer.service;

import com.dolzhik.meteoServer.entity.Captcha;
import com.dolzhik.meteoServer.repository.CaptchaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class LocalCaptchaGenerator implements CaptchaProvider {

    @Autowired
    CaptchaRepository captchaRepository;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();
    private static final int CAPTCHA_SIZE = 5;
    private static final int CAPTCHA_HEIGHT = 120;
    private static final int CAPTCHA_WIDTH = 400;
    private static final Logger LOGGER = LogManager.getLogger(LocalCaptchaGenerator.class);


    @Override
    public Captcha getCaptcha() {
        var image = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();

        var captchaBuilder = new StringBuilder();

        for (int i = 0; i < CAPTCHA_SIZE; i++) {
            captchaBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        var captchaString = captchaBuilder.toString();

        drawBackground(graphics);
        drawText(graphics, captchaString);
        drawForeground(graphics);

        var capthca = new Captcha();
        capthca.setAnswer(captchaString.toLowerCase());
        capthca.setCreated(new Timestamp(Instant.now().toEpochMilli()));

        var byteArrayStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteArrayStream);
            capthca.setImageBlob(new SerialBlob(byteArrayStream.toByteArray()));
        } catch (IOException | SQLException e) {
            LOGGER.info("Unable to save BufferedImage to Blob");
            LOGGER.error(e);
            return null;
        }

        captchaRepository.save(capthca);
        LOGGER.info("Captcha created: {}", capthca);

        return capthca;
    }

    @Override
    public boolean validateCaptcha(long id, String answer) {
        var captcha = Optional.ofNullable(captchaRepository.getCaptchaById(id)).orElse(new Captcha());
        answer = answer.toLowerCase();
        if (answer.equals(captcha.getAnswer())) {
            captchaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void removeCaptcha(long id) {

    }

    private void drawText(Graphics2D graphics, String text) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Serif", Font.BOLD, 120));
        graphics.drawString(text, 0, CAPTCHA_HEIGHT - CAPTCHA_HEIGHT / 6);
    }


    private void drawBackground(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
        drawLines(graphics, 30);
        drawCircles(graphics, 20);
    }

    private void drawForeground(Graphics2D graphics) {
        drawLines(graphics, 30);
        drawCircles(graphics, 10);
    }

    private void drawLines(Graphics2D graphics, int count) {
        for (int i = 0; i < count; i++) {
            var color = new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));
            graphics.setColor(color);
            graphics.drawLine(RANDOM.nextInt(CAPTCHA_WIDTH), RANDOM.nextInt(CAPTCHA_HEIGHT), RANDOM.nextInt(CAPTCHA_WIDTH), RANDOM.nextInt(CAPTCHA_HEIGHT));
        }
    }

    private void drawCircles(Graphics2D graphics, int count) {
        for (int i = 0; i < count; i++) {
            var color = new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));
            graphics.setColor(color);
            graphics.fillOval(RANDOM.nextInt(CAPTCHA_WIDTH), RANDOM.nextInt(CAPTCHA_HEIGHT), RANDOM.nextInt(CAPTCHA_WIDTH) / 5, RANDOM.nextInt(CAPTCHA_HEIGHT) / 2);
        }
    }
}
