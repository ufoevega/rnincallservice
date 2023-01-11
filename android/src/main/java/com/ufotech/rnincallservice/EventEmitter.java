package com.ufotech.rnincallservice;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import androidx.annotation.MainThread;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.ufotech.rnincallservice.interfaces.NativeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEmitter {

    private static EventEmitter sharedInstance = new EventEmitter();
    private final List<NativeEvent> queuedEvents = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final HashMap<String, Integer> jsListeners = new HashMap<>();
    private ReactContext reactContext;
    private Boolean jsReady = false;
    private int jsListenerCount;

    public static EventEmitter getSharedInstance(){
        return sharedInstance;
    }

    public void attachReactContext(final ReactContext reactContext){
        handler.post(
                ()->{
                   EventEmitter.this.reactContext = reactContext;
                   sendQueuedEvents();
                });
    }

    public void notifyJsReady(Boolean ready){
        handler.post(()->{
            Log.d("TRULI","notifyJsReady");
            jsReady=ready;
            sendQueuedEvents();
        });
    }

    public void sendEvent(final NativeEvent event){
        handler.post(()->{
            synchronized (jsListeners){
                if(!jsListeners.containsKey(event.getEventName()) || !emit(event)){
                    Log.d("TRULI","Agregar evento "+event.getEventName());
                    queuedEvents.add(event);
                }
            }
        });
    }

    public void addListener(String eventName){
        synchronized (jsListeners){
            jsListenerCount++;
            if(!jsListeners.containsKey(eventName)){
                jsListeners.put(eventName,1);
            }else{
                int listenerForEvent=jsListeners.get(eventName);
                jsListeners.put(eventName,listenerForEvent+1);
            }
        }
        handler.post(this::sendQueuedEvents);
    }

    public void removeListener(String eventName,Boolean all){
        synchronized (jsListeners){
            if(jsListeners.containsKey(eventName)){
                int listenerForEvent = jsListeners.get(eventName);

                if(listenerForEvent<=1 || all){
                    jsListeners.remove(eventName);
                }else{
                    jsListeners.put(eventName,listenerForEvent-1);
                }

                jsListenerCount-=all ? listenerForEvent : 1;
            }
        }
    }

    public WritableMap getListenersMap(){
        WritableMap writeableMap = Arguments.createMap();
        WritableMap events = Arguments.createMap();

        writeableMap.putInt("listeners",jsListenerCount);
        writeableMap.putInt("queued",queuedEvents.size());

        synchronized (jsListeners){
            for(Map.Entry<String,Integer> entry : jsListeners.entrySet()){
                events.putInt(entry.getKey(),entry.getValue());
            }
        }
        writeableMap.putMap("events",events);
        return writeableMap;
    }

    @MainThread
    private void sendQueuedEvents(){
        synchronized (jsListeners){
            for(NativeEvent event : new ArrayList<>(queuedEvents)){
                if(jsListeners.containsKey(event.getEventName())){
                    queuedEvents.remove(event);
                    sendEvent(event);
                }
            }
        }
    }

    @MainThread
    private boolean emit(final NativeEvent event){
        if(!jsReady || reactContext == null || !reactContext.hasActiveCatalystInstance()){
            Log.d("TRULI","JS no ready");
            return false;
        }
        try{
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event.getEventName(),event.getEventBody());
        }catch (Exception e){
            Log.e("TRULI "+event.getEventName(),e.toString());
            return false;
        }
        Log.d("TRULI","Evento enviado "+event.getEventName());
        return true;
    }
}
