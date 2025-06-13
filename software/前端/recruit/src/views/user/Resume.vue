<script lang='ts' setup>
import VuePdfEmbed from 'vue-pdf-embed'
// essential styles
import 'vue-pdf-embed/dist/style/index.css'

// optional styles
import 'vue-pdf-embed/dist/style/annotationLayer.css'
import 'vue-pdf-embed/dist/style/textLayer.css'
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'    //导入图标

import {ref} from 'vue'
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();
// either URL, Base64, binary, or document proxy
// const pdfSource = 'https://mozilla.github.io/pdf.js/web/compressed.tracemonkey-pldi-09.pdf'
const resumeList = ref([
    {   "id":'',
        "uid":'',
        "resumeName":'',
        "resumeUrl":'',
        "note":''
    }
])
import {userResumeListService} from "@/api/user.js"
const getUserResume=async()=>{
    let result = await userResumeListService();
    resumeList.value = result.data;
}
getUserResume();

const resumeVisible = ref(false)
const resumeVisUrl = ref('')
const lookResume= (row) =>{
  resumeVisible.value = true;
  resumeVisUrl.value = row.resumeUrl;
}

const DialogVisible = ref(false);
const updateResumeData = ref();
const uploadRef = ref();
const updateDialog = ref(false)
const addDialog = ref(false)
const dialogTitle = ref()
//打开Dialog
const openModifyDialog= (row)=>{
  dialogTitle.value = "修改简历";
  addDialog.value = false;
  updateDialog.value = true;
  DialogVisible.value = true;
  updateResumeData.value = row;
}
//修改resume
const updateResume=() =>{
  uploadRef.value!.submit()
}
//删除resume
import {ElMessageBox, genFileId, UploadRawFile} from 'element-plus'
import {userResumeRemoveService} from '@/api/user.js'
const removeResume= (row) =>{
  let params = {
        resumeId: row.id,
  }
  ElMessageBox.confirm(
    '确认删除此简历?',
    '警告',
    {
      cancelButtonClass: 'btn-custom-cancel',
      confirmButtonClass: 'btn-custom-confirm',
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async() => {
      ElMessage({
        type: 'warning',
        message: '正在删除',
      })
      let result = await userResumeRemoveService(params)
      if(result.code===0){
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
      }
      else{
        ElMessage({
          type: 'warning',
          message: '出错'
        })
      }

    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '删除已取消',
      })
    })
}
//添加resume
const showAddResumeDialog= () =>{
  dialogTitle.value = "添加简历";
  addDialog.value = true;
  updateDialog.value = false;
  DialogVisible.value = true;
  updateResumeData.value = ref();
}

import { ElMessage, UploadFile, UploadInstance } from 'element-plus'
import type { UploadProps } from 'element-plus'
//简历上传前的检查
const beforeResumeUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'application/pdf'
  ) {
    ElMessage.error('简历文件的格式必须为pdf!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 5) {
    ElMessage.error('简历大小不能超过5MB!')
    return false
  } //TODO 非空校验
  return true
}


import {userResumeUpdateService, userResumeAddService} from '@/api/user.js'
//上传成功的回调函数
const uploadSuccess = async (result) =>{
    updateResumeData.value.resumeUrl = result.data
    //调用接口
    if(updateDialog.value === true && addDialog.value == false){
      let resultMsg = await userResumeUpdateService(updateResumeData.value);
      ElMessage.success(resultMsg.msg? resultMsg.msg:'修改成功')
    }
    else if(updateDialog.value === false && addDialog.value == true){
      let resultMsg = await userResumeAddService(updateResumeData.value);
      ElMessage.success(resultMsg.msg? resultMsg.msg:'添加成功')
    }
    else{
      ElMessage.error('error');
    }
    //清空
    updateDialog.value = false;
    addDialog.value = false;
    DialogVisible.value = false;
    updateResumeData.value = ref();
    getUserResume();
}

const handleExceed: UploadProps['onExceed'] = (files) => {
  uploadRef.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  uploadRef.value!.handleStart(file)
}



</script>

<template>
  <el-container style="width: 100%; height: 100%;">
    <el-container class="left">
      <div>
        <el-card class="table-card">
          <template #header>
            <el-container class="card-header">
              <div class="card-header-left"><h2 style="margin: 0.7%; color: #409EFF;">我的简历</h2></div>
              <div class="card-header-right"><div style="float: right;"><el-button type="primary" plain @click="showAddResumeDialog()">添加简历</el-button></div></div>
            </el-container>
          </template>
          <el-table :data="resumeList">
            <el-table-column label="编号" width="70" type="index"></el-table-column>
            <el-table-column label="简历名称" prop="resumeName"></el-table-column>
            <!-- <el-table-colunm label="更新时间" prop="lastUpdateTime"></el-table-colunm> -->
            <el-table-column label="备注" prop="note"></el-table-column>
            <el-table-column label="操作" width="230">
              <template #default="{ row }">
                <el-button round plain @click="lookResume(row)">查看</el-button>
                <el-button round type="primary" plain @click="openModifyDialog(row)">修改</el-button>
                <el-button round type="danger" plain @click="removeResume(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </el-container>
    <el-container class="right">
        <el-watermark :content="'会员制software'">
          <VuePdfEmbed v-show="resumeVisible" annotation-layer text-layer :source="resumeVisUrl" />
        </el-watermark>
    </el-container>
  </el-container>
  <!-- <VuePdfEmbed annotation-layer text-layer :source="resumeInfo.resumeUrl" /> -->
  <el-dialog v-model="DialogVisible" center style="width: 55%;">
    <template #header>
      <h3>{{ dialogTitle }}</h3>
    </template>
      <el-container>
        <el-form :model="updateResumeData" label-width="100px" size="large" style="width:50%;">
          <el-form-item label="简历名称" prop="resumeName">
            <el-input v-model="updateResumeData.resumeName"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="note">
            <el-input v-model="updateResumeData.note"></el-input>
          </el-form-item>
          <el-form-item label="简历" prop="newResume">
            <el-upload
              ref="uploadRef"
              action="/api/upload"
              :show-file-list="true"
              :before-upload="beforeResumeUpload"
              :auto-upload="false"
              name="file"
              :headers="{'Authorization':tokenStore.token}"
              :on-success="uploadSuccess"
              :accept="'.pdf'"
              :limit="1"
              :on-exceed="handleExceed"
              >
                <template #trigger>
                  <el-button type="primary" plain>选择文件</el-button>
                </template>

                <template #tip>
                    <div class="el-upload__tip">请上传pdf格式，大小不超过5MB的文件 </div>
                </template>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button v-if="updateDialog===true" type="primary" @click="updateResume">提交修改</el-button>
            <el-button v-if="addDialog===true" type="primary" @click="updateResume">提交简历</el-button>
          </el-form-item>
        </el-form>
      </el-container>
  </el-dialog>
</template>

<style scoped>
.left{
  float: left;
  display: block;
  width: 70%;
  height: auto;
  margin-left: 2%;
  /* background-color: blue; */
}
.right{
  float: right;
  display: block;
  width: 70%;
  height: auto;
  margin-left: 5%;
  margin-top: 1%;
  margin-right:6%;
  min-height: 100%;
  /* background-color: black; */
}
.table-card{
  margin-left: 3%;
  margin-top: 2%;
  margin-right:2%;
  min-height: 50%;
}
.card-header{
  margin: 0;
  width: 100%;
}
.card-header-left{
  width: 100%;
}
.card-header-right{
  width: 100%;
}
</style>
<style>
.btn-custom-cancel{
  border:0;
  float: right;
  background: #409EFF !important;
  color: #fefefe !important;
}
.btn-custom-confirm{
  border:0;
  float: left;
  background: #F56C6C !important;
        color: #fefefe !important;
}
</style>