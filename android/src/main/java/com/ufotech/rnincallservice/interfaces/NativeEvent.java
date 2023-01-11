package com.ufotech.rnincallservice.interfaces;

import com.facebook.react.bridge.WritableMap;

public interface NativeEvent {
    String getEventName();

    WritableMap getEventBody();

    String getAppName();
}
