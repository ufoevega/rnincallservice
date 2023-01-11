package com.ufotech.rnincallservice;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.VideoProfile;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class OngoingCall {


    public BehaviorSubject state=BehaviorSubject.create();

    private static volatile OngoingCall INSTANCE=null;
    public static Call call=null;
    public static InCallService IC;

    private OngoingCall(){}

    @SuppressLint("NewApi")
    Call.Callback callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int new_state) {
            Log.d("UFO","Estado "+new_state);

            String display_name =  call.getDetails().getCallerDisplayName();
            String contact_display_name=call.getDetails().getContactDisplayName();
            String direction=""+call.getDetails().getCallDirection();
            String start=""+call.getDetails().getCreationTimeMillis();
            String end=""+call.getDetails().getConnectTimeMillis();
            String number=""+call.getDetails().getHandle().toString();

            state.onNext(new_state);
            WritableMap eventBody = Arguments.createMap();

            eventBody.putString("state",""+new_state);
            eventBody.putString("number",number);
            eventBody.putString("caller_display_name",display_name);
            eventBody.putString("contact_display_name",contact_display_name);
            eventBody.putString("direction",direction);
            eventBody.putString("start",start);
            eventBody.putString("end",end);

            EventEmitter.getSharedInstance().sendEvent(new ICSEvent("call-state",eventBody));
            super.onStateChanged(call, new_state);
        }
    };

    public void setIC(InCallService ic){
        IC=ic;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCall(Call current_call){
        if(call!=null){
            call.unregisterCallback(callback);
        }

        call = current_call;

        if(current_call!=null) {
            call.registerCallback(callback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void answer(){
        if(call!=null){
            call.answer(VideoProfile.STATE_AUDIO_ONLY);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hangup(){
        if(call!=null){
            call.disconnect();
        }
    }

    public static OngoingCall getInstance(){
        if(INSTANCE==null){
            synchronized (OngoingCall.class){
                if(INSTANCE==null){
                    INSTANCE=new OngoingCall();
                }
            }
        }
        return  INSTANCE;
    }
}
