<script setup>
import { reactive, ref } from 'vue'
import {ElMessage} from 'element-plus'
import {firmPasswordUpdateService} from '@/api/firm.js'
import {useRouter} from 'vue-router'
const router = useRouter();

const form = ref()
const passwordData = ref({
    oldPwd:'',
    newPwd:'',
    rePwd:''
})

const checkRePassword = (rule, value, callback)=>{
    if(value == ''){
        callback(new Error('请再次确认密码'))
    }else if(value != passwordData.value.newPwd){
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    newPwd:[
        {required: true, message:'请输入密码', trigger:'blur'},
        {min:5, max:16, message: '长度为5~16为非空字符', trigger: 'blur'}
    ],
    rePwd:[
        {validator: checkRePassword, trigger: 'blur'}
    ]
}


const updatePwd = async() =>{
    await form.value.validate();    //用于校验，如果校验失败，后续代码将不会执行
    let result = await firmPasswordUpdateService(passwordData.value);

    ElMessage.success(result.msg? result.msg:'修改成功')
    router.push("/login")
}

const resetForm = () =>{
    passwordData.value={
        oldPwd:'',
        newPwd:'',
        rePwd:''
    }
}

</script>


<template>
    <el-form ref="form" style="max-width: 600px" :model="passwordData" status-icon :rules="rules" label-width="auto"  autocomplete="off">
        <el-form-item label="旧密码">
            <el-input :prefix-icon="Lock" v-model="passwordData.oldPwd" type="password" placeholder="请输入旧密码"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
            <el-input :prefix-icon="Lock" v-model="passwordData.newPwd" type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="再次输入新密码" prop="rePwd">
            <el-input :prefix-icon="Lock" v-model="passwordData.rePwd" type="password" placeholder="请再次输入新密码" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" auto-insert-space @click="updatePwd">
                提交
            </el-button>
            <el-button auto-insert-space @click="resetForm">清空</el-button>
        </el-form-item>
    </el-form>
</template>

