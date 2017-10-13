package com.tarija.tresdos.safetarija.model;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class Notification {
    private String body;
    private String title;

    public Notification() {
    }

    public Notification(String body, String title) {
        this.setBody(body);
        this.setTitle(title);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
