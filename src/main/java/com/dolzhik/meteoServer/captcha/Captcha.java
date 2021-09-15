package com.dolzhik.meteoServer.captcha;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Blob;
import java.sql.Timestamp;

public class Captcha {
    private Long id;
    private String answer;
    private Blob imageBlob;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Kiev")
    private Timestamp created;

    public Captcha(Long id, String answer, Blob imageBlob) {
        this.id = id;
        this.answer = answer;
        this.imageBlob = imageBlob;
    }

    public Captcha() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
