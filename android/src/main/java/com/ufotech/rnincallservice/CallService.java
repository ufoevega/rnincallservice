package com.ufotech.rnincallservice;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CallService extends InCallService {
    EventEmitter emitter = EventEmitter.getSharedInstance();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    @Override
    public void onCallAdded(Call call) {
        Log.d("UFO","llamada entrante");
        OngoingCall.getInstance().setCall(call);
        OngoingCall.getInstance().setIC(this);

        Log.d("UFO",getApplicationContext().getPackageName());
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setComponent(new ComponentName(getApplicationContext().getPackageName(), getApplicationContext().getPackageName()+".MainActivity"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);






        WritableMap eventBody = Arguments.createMap();

        String display_name =  call.getDetails().getCallerDisplayName();
        String contact_display_name=call.getDetails().getContactDisplayName();
        String direction=""+call.getDetails().getCallDirection();
        String start=""+call.getDetails().getCreationTimeMillis();
        String end=""+call.getDetails().getConnectTimeMillis();
        String number=""+call.getDetails().getHandle().toString();

        eventBody.putString("state",""+call.getState());
        eventBody.putString("number",call.getDetails().getHandle().toString());
        eventBody.putString("number",number);
        eventBody.putString("caller_display_name",display_name);
        eventBody.putString("contact_display_name",contact_display_name);
        eventBody.putString("direction",direction);
        eventBody.putString("start",start);
        eventBody.putString("end",end);
        emitter.getSharedInstance().sendEvent(new ICSEvent("call-state",eventBody));
        super.onCallAdded(call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        WritableMap eventBody = Arguments.createMap();
        eventBody.putString("number",call.getDetails().getHandle().toString());
        emitter.sendEvent(new ICSEvent("call-ended",eventBody));
        OngoingCall.getInstance().setCall(null);
    }
}
