<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage, roleTypes } from 'element-plus'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
const form = ref()
// const isUser = ref(false)
// const isFirm = ref(false)
// const isManager = ref(false)

//定义数据模型
// .value后才能访问数据模型中的
const registerData = ref({
    account: '',
    password: '',
    rePassword: '',
    role:''
})

//校验密码的函数
const checkRePassword = (rule, value, callback) => {
    if (value == '') {
        callback(new Error('请再次确认密码'))
    } else if (value != registerData.value.password) {
        callback(new Error('请确保两次输入的密码一样'))
    } else {
        callback()
    }
}

//定义表单校验规则
const rules = {
    account: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassword, trigger: 'blur' }
    ]
}

//调用后台接口,完成注册
import { RegisterService, LoginService} from '@/api/login.js'
const register = async () => {  //async表示这是一个异步函数，该函数运行不会阻塞后续函数的运行
    await form.value.validate();    //校验不通过后续代码将不会执行
    //registerData是一个响应式对象,如果要获取值,需要.value
    let result = await RegisterService(registerData.value);     //await等待结果（没有返回结果不会执行后续代码）
    ElMessage.success(result.msg ? result.msg : '注册成功')
}

//绑定数据,复用注册表单的数据模型
//表单数据校验
//登录函数
import {useTokenStore} from '@/stores/token.js'
import {useRouter} from 'vue-router'
const router = useRouter()
//调用useTokenStore获取token
const tokenStore = useTokenStore();
const login =async ()=>{
    //调用接口,完成登录

    let result =  await LoginService(registerData.value);
    ElMessage.success(result.msg ? result.msg : '登录成功')
   //把得到的token存储到pinia中
    tokenStore.setToken(result.data)

   //跳转到首页 路由完成跳转
    let role = registerData.value.role;
    if(role === "User"){
    router.push("/user");
    }
    else if(role === "Firm"){
    router.push("/firm");
    }
    else if(role === "Manager"){
    router.push("/manager");
    }
}

//定义函数,清空数据模型的数据
const clearRegisterData = ()=>{
    registerData.value={
        account:'',
        password:'',
        rePassword:''
    }
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item label="账户类型">
                    <el-select v-model="registerData.role" placeholder="请选择账户类型">
                        <el-option label="用户" value="User" />
                        <el-option label="企业" value="Firm" />
                    </el-select>
                </el-form-item>
                <el-form-item prop="account" >  <!-- prop属性表示要校验的字段 -->
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.account"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">   <!--@click 绑定单击事件(执行函数或代码)-->
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false;clearRegisterData()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item label="账户类型">
                    <el-select v-model="registerData.role" placeholder="请选择账户类型">
                        <el-option label="用户" value="User" />
                        <el-option label="企业" value="Firm" />
                        <el-option label="管理员" value="Manager" />
                    </el-select>
                </el-form-item>
                <!-- prop是表单校验规则的属性名，rules是校验规则的数组 -->
                <el-form-item prop="account">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.account"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <!-- 点注册时清除数据模型的数据 -->
                    <el-link type="info" :underline="false" @click="isRegister = true;clearRegisterData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>



<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
            url('@/assets/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>