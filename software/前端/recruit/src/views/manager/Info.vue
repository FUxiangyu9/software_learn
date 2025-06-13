<script setup>
import { ref } from 'vue'
import useManagerInfoStore from '@/stores/managerInfo.js'
const managerInfoStore = useManagerInfoStore();

const managerInfo = ref({...managerInfoStore.info})
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
import {managerInfoUpdateService} from '@/api/manager.js'
import {ElMessage, valueEquals} from 'element-plus'
const updateManagerInfo = async ()=>{
    //调用接口
    let result = await managerInfoUpdateService(managerInfo.value);
    ElMessage.success(result.msg? result.msg : '修改成功');
    //修改pinia中的个人信息
    managerInfoStore.setInfo(managerInfo.value)
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
                <el-form :model="managerInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="managerInfo.account" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="uname">
                        <el-input v-model="managerInfo.uname"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="managerInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item label="电话" prop="tel">
                        <el-input v-model="managerInfo.tel"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateManagerInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>