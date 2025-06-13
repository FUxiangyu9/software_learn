//导入request.js请求工具
import request from '@/utils/request.js'

//提供调用注册接口的函数
export const RegisterService = (registerData)=>{

    const params = new URLSearchParams()    //创建x-www-form-urlencoded格式的对象
    for(let key in registerData){
        params.append(key,registerData[key]);   //x-www-form-urlencoded格式的数据
    }
    return request.post('/register',params);    //request.post('/register/userReg',params)会返回一个数据
}

//提供调用登录接口的函数
export const LoginService = (loginData)=>{
    const params = new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key])
    }
    return request.post('/login',params)
}


