<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore();

const userInfo = ref({...userInfoStore.info})
const rules = {
    uname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}
//修改个人信息
import {userInfoUpdateService} from '@/api/user.js'
import {ElMessage, valueEquals} from 'element-plus'
const updateUserInfo = async ()=>{
    //调用接口
    let result = await userInfoUpdateService(userInfo.value);
    ElMessage.success(result.msg? result.msg : '修改成功');
    //修改pinia中的个人信息
    userInfoStore.setInfo(userInfo.value)


}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.account" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="uname">
                        <el-input v-model="userInfo.uname"></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="sex">
                        <el-select v-model="userInfo.sex" >
                            <el-option label='男' :value="0"/>
                            <el-option label="女" :value="1"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="学历" prop="degree">
                        <el-input v-model="userInfo.degree"></el-input>
                    </el-form-item>
                    <el-form-item label="毕业院校" prop="graduatedSchool">
                        <el-input v-model="userInfo.graduatedSchool"></el-input>
                    </el-form-item>
                    <el-form-item label="专业" prop="major">
                        <el-input v-model="userInfo.major"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="userInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item label="电话" prop="tel">
                        <el-input v-model="userInfo.tel"></el-input>
                    </el-form-item>
                    <el-form-item label="自我介绍" prop="introduction">
                        <el-input v-model="userInfo.introduction"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>