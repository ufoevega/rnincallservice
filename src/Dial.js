import { useState } from "react"
import { Text, TouchableOpacity, View,StyleSheet } from "react-native"

const Dial=()=>{
    const [number,setNumber] = useState("");

    const keyup=(digit)=>{
        setNumber(prev=>prev+digit)
    }

    return <View style={{flex:1,backgroundColor:"#3498DB"}}>
            <View>
                
            </View>
            <View style={{display:"flex",flex:1,flexDirection:"row",marginVertical:16}}>
                <TouchableOpacity style={style_digit_button} onPress={()=>keyup(1)}>
                    <Text style={style_digit_text}>1</Text>
                </TouchableOpacity>
                <TouchableOpacity style={style_digit_button} onPress={()=>keyup(2)}>
                    <Text style={style_digit_text}>2</Text>
                </TouchableOpacity>
                <TouchableOpacity style={style_digit_button} onPress={()=>keyup(3)}>
                    <Text style={style_digit_text}>3</Text>
                </TouchableOpacity>
            </View>
    </View>

}

const style_digit_text=StyleSheet.create({
    color:"#F2F3F4",
    fontSize:32
})

const style_digit_button=StyleSheet.create({
    backgroundColor:"#21618C",
    borderRadius:8,
    padding:24,
    flex:1,
    margin:8
})
export default Dial