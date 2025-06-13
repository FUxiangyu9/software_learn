<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <!--借助el-upload进行上传-->
                <el-upload
                ref="uploadRef"
                class="avatar-uploader"
                action="/api/upload"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :auto-upload="false"
                :on-change="justSelect"
                name="file"
                :headers="{'Authorization':tokenStore.token}"
                :on-success="uploadSuccess"
                :accept="'.jpg, .jpeg, .png'"
                >
                    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                    <template #tip>
                        <div class="el-upload__tip">请上传jpg或png格式，大小不超过3MB的文件 </div>
                    </template>
                </el-upload>
                <div style="margin-top:20px;">
                    <el-button type="primary" @click="uploadRef.$el.querySelector('input').click()">选择图片</el-button>
                    <el-button class="ml-3" type="success" @click="updateAvatar">提交修改</el-button>
                </div>
            </el-col>
        </el-row>
    </el-card>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessage, UploadFile, UploadInstance } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();

import useFirmInfoStore from '@/stores/firmInfo.js'
const firmInfoStore = useFirmInfoStore();
//用户头像地址
const imageUrl = ref(firmInfoStore.info.firmAvatarUrl)
//头像修改
import {firmAvatarUpdateService} from '@/api/firm.js'
const updateAvatar = async () =>{
    uploadRef.value!.submit()
}
//图片上传成功的回调函数
const uploadSuccess = async (result) =>{
    console.log(result.data)
    imageUrl.value = result.data;
        //调用接口
    let resultMsg = await firmAvatarUpdateService(imageUrl.value);

    ElMessage.success(resultMsg.msg? resultMsg.msg:'修改成功')

    //修改pinia中的数据
    firmInfoStore.info.avatarUrl = imageUrl.value;
}


import type { UploadProps } from 'element-plus'
const uploadRef = ref<UploadInstance>()
const justSelect = (uploadFile: UploadFile) =>{
  imageUrl.value = URL.createObjectURL(uploadFile.raw)
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' &&
      rawFile.type !== 'image/png'  &&
      rawFile.type !== 'image/jpg'
  ) {
    ElMessage.error('头像格式必须为jpg, png!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 3) {
    ElMessage.error('头像大小不能超过3MB!')
    return false
  }
  return true
}
</script>
<!-- <style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style> -->
<style scoped>
.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}
.avatar-uploader {
    .avatar {
        width: 278px;
        height: auto;
        display: block;
    }
    .el-upload {
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);
    }
    .el-upload:hover {
        border-color: var(--el-color-primary);
    }
    .el-icon.avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 278px;
        height: 278px;
        text-align: center;
    }
}
</style>