import {NativeModules} from 'react-native'
import * as Constants from "./src/Constants"
import useCallState from "./src/Hooks/useCallState";
import Dial from "./src/Dial"
import { listeners } from './src/Actions';

const RNIncallservice = NativeModules.RNIncallservice

class RNInCallService {
    constructor(){
        this._inCallServiceEventHandlers=new Map()
    }

    ready=()=>{
        RNIncallservice.init();
    }

    addEventListener=(type,handler)=>{
        console.log(listeners[type])
        const listener = listeners[type](handler);
        this._inCallServiceEventHandlers.set(type, listener);
    }

    removeEventListener=(type)=>{
        const listener = this._inCallServiceEventHandlers.get(type);
        if (!listener) {
          return;
        }
    
        listener.remove();
        this._inCallServiceEventHandlers.delete(type);
    }

    playDigit=(digit)=>{
        RNIncallservice.playDtmfTone(digit)
    }

    toFront=()=>{
        RNIncallservice.activate()
    }

    toBack=()=>{
        RNIncallservice.deactivate()
    }

    speaker=(active)=>{
        RNIncallservice.speaker(active)
    }

    hold=(paused)=>{
        RNIncallservice.hold(paused)
    }

    muted=(_muted)=>{
        RNIncallservice.muted(_muted)
    }


    call=(number)=>{
        RNIncallservice.call(number,()=>{
            console.log("Listo")
        })
    }

    answer=()=>{
        RNIncallservice.answer()
    }

    hangup=()=>{
        RNIncallservice.hangup()
    }
}

export {
    Constants,
    useCallState,
    Dial
}

export default new RNInCallService();