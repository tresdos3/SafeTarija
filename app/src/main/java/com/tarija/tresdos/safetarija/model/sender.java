package com.tarija.tresdos.safetarija.model;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class sender {
    private String to;
    private Notification notification;

    public sender() {
    }

    public sender(String to, Notification notification) {
        this.setTo(to);
        this.setNotification(notification);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
