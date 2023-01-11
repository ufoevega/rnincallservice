
package com.ufotech.rnincallservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

public class RNIncallserviceModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  EventEmitter emitter=EventEmitter.getSharedInstance();

  public RNIncallserviceModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;


    emitter.attachReactContext(reactContext);
  }

  @ReactMethod
  public void activate() {
    final Activity activity = getCurrentActivity();

    if (activity != null) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                  WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                  WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                  WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }
      });
    }
  }



  @ReactMethod
  public void deactivate() {
    final Activity activity = getCurrentActivity();

    if (activity != null) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          activity.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
      });
    }
  }

  @ReactMethod
  public void init(){
    emitter.notifyJsReady(true);
  }

  @ReactMethod
  public void call(String number, Callback callback){
    Context ctx = getReactApplicationContext();
    //Toast.makeText(ctx, number, Toast.LENGTH_SHORT).show();

    Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse(number));
    intent_call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity(intent_call);
    callback.invoke(1);
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @ReactMethod
  public void answer(){
    OngoingCall.getInstance().answer();
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @ReactMethod
  public void playDtmfTone(String digit){
    try{
      OngoingCall.getInstance().call.postDialContinue(true);
      OngoingCall.getInstance().call.playDtmfTone(digit.charAt(0));
    }catch (Exception e){
      Log.e("UFO",e.toString());
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @ReactMethod
  public void hangup(){
    OngoingCall.getInstance().hangup();
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @ReactMethod
  public void muted(Boolean muted){
    try{
      OngoingCall.getInstance().IC.setMuted(muted);
    }catch (Exception e){
      Log.e("UFO",e.toString());
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @ReactMethod
  public void hold(Boolean paused){
    try{

      if(paused){
        if(OngoingCall.getInstance().call.getState()== Call.STATE_ACTIVE) {
          OngoingCall.getInstance().call.hold();
        }
      }else{
        if(OngoingCall.getInstance().call.getState()==Call.STATE_HOLDING) {
          OngoingCall.getInstance().call.unhold();
        }
      }

    }catch (Exception e){
      Log.e("UFO",e.toString());
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @ReactMethod
  public void speaker(Boolean mode){
    try{
      if(mode){
        OngoingCall.getInstance().IC.setAudioRoute(CallAudioState.ROUTE_SPEAKER);
      }else{
        OngoingCall.getInstance().IC.setAudioRoute(CallAudioState.ROUTE_WIRED_OR_EARPIECE);
      }
    }catch (Exception e){
      Log.e("UFO",e.toString());
    }

  }

  @ReactMethod
  public void addListener(String eventName) {
    emitter.addListener(eventName);
  }

  @ReactMethod
  public void removeListener(String eventName) {
    emitter.removeListener(eventName,false);
  }

  @ReactMethod
  public void removeListeners(Integer count) {
  }

  @Override
  public String getName() {
    return "RNIncallservice";
  }
}