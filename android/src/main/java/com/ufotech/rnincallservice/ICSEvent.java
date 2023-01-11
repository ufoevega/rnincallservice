package com.ufotech.rnincallservice;

import com.facebook.react.bridge.WritableMap;
import com.ufotech.rnincallservice.interfaces.NativeEvent;

public class ICSEvent implements NativeEvent {

    private String eventName;
    private WritableMap eventBody;

    public ICSEvent(String eventName, WritableMap eventBody) {
        this.eventName = eventName;
        this.eventBody = eventBody;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public WritableMap getEventBody() {
        return eventBody;
    }

    @Override
    public String getAppName() {
        return null;
    }
}
