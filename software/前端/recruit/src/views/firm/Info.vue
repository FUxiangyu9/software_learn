<script setup>
import { ref } from 'vue'
import useFirmInfoStore from '@/stores/firmInfo.js'
const firmInfoStore = useFirmInfoStore();

const firmInfo = ref({...firmInfoStore.info})
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
import {firmInfoUpdateService} from '@/api/firm.js'
import {ElMessage, valueEquals} from 'element-plus'
const updateFirmInfo = async ()=>{
    //调用接口
    let result = await firmInfoUpdateService(firmInfo.value);
    ElMessage.success(result.msg? result.msg : '修改成功');
    //修改pinia中的个人信息
    firmInfoStore.setInfo(firmInfo.value)


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
                <el-form :model="firmInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="firmInfo.firmAccount" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="firmUname">
                        <el-input v-model="firmInfo.firmUname"></el-input>
                    </el-form-item>
                    <el-form-item label="公司" prop="firmCorp">
                        <el-input v-model="firmInfo.firmCorp"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="firmEmail">
                        <el-input v-model="firmInfo.firmEmail"></el-input>
                    </el-form-item>
                    <el-form-item label="电话" prop="firmTel">
                        <el-input v-model="firmInfo.firmTel"></el-input>
                    </el-form-item>
                    <el-form-item label="介绍" prop="firmIntroduction">
                        <el-input v-model="firmInfo.firmIntroduction"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateFirmInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>