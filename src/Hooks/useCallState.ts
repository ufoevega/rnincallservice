import React,{useState} from "react";
import {NativeModules,NativeEventEmitter} from "react-native";
import { isIOS } from "../Utils/platform";

const RNIncallservice = NativeModules.RNIncallservice

export default function useCallState(){
    const [call_state,setState] = useState(-1);

  
    React.useEffect(()=>{

        if (!isIOS()) {
            return () => {};
        }

        const emitter = new NativeEventEmitter(RNIncallservice);
        emitter.addListener('call-state',({state})=>setState(state))
        
        return ()=>{
            emitter.removeAllListeners('call-state');
        }
    },[])

    return {
        call_state
    }
}
