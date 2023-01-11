import { NativeModules, NativeEventEmitter, Platform } from 'react-native';

const isIOS = Platform.OS === 'ios';
const RNIncallservice = NativeModules.RNIncallservice;
const eventEmitter = new NativeEventEmitter(RNIncallservice);

const onCall=handler=>
    eventEmitter.addListener('call',handler)

const onCallEnded=handler=>
    eventEmitter.addListener('call-ended',handler)

const onCallState=handler=>
  eventEmitter.addListener('call-state',handler)

export const listeners = {
    onCall,
    onCallEnded,
    onCallState
}



