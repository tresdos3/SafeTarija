package com.tarija.tresdos.safetarija.service;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by Tresdos on 10/15/2017.
 */

public class AccessibilityService extends android.accessibilityservice.AccessibilityService{
    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 30;
        this.setServiceInfo(info);
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        String eventText = null;
        switch (eventType){
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "Focused: ";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "Focused: ";
                break;
        }
        eventText = eventText + event.getContentDescription();
        Toast.makeText(getApplicationContext(), eventText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInterrupt() {

    }
}
