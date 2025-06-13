import {defineStore} from 'pinia'
import {ref} from 'vue'
const useFirmInfoStore = defineStore('firmInfo',()=>{
    //定义状态相关的内容

    const info = ref({})
    const setInfo = (newInfo)=>{
        info.value = newInfo
        console.log(newInfo)
    }


    const removeInfo = ()=>{
        info.value = {}
    }

    return {info,setInfo,removeInfo}

},{persist:true})

export default useFirmInfoStore;