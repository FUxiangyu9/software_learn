<template>
  <el-container class="container">
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span class="card-header-title">添加岗位</span>
        </div>
      </template>
      <el-form :model="jobForm" size="large" label-width="auto" style="max-width: 600px;padding:20px;">
        <el-form-item label="岗位名称" prop="name">
          <el-input clearable :minlength="1" :maxlength="255" v-model="jobForm.jobName" />
        </el-form-item>
        <el-form-item label="薪资(千)" prop="jobName">
          <el-input-number :min="0" clearable v-model="jobForm.payroll" />
        </el-form-item>
        <el-form-item clearable label="招聘数量(人)" prop="jobAlias">
          <el-input-number :min="1" clearable  v-model="jobForm.hc" />
        </el-form-item>
        <el-form-item label="学历要求" prop="jobAlias">
          <el-input clearable :minlength="1" :maxlength="255" v-model="jobForm.degree" />
        </el-form-item>
        <el-form-item label="工作地点" prop="jobAlias">
          <el-input clearable :minlength="1" :maxlength="255" v-model="jobForm.location" />
        </el-form-item>
        <el-form-item label="城市" prop="jobAlias">
          <el-input clearable :minlength="1" :maxlength="255" v-model="jobForm.city" />
        </el-form-item>
        <el-form-item label="要求">
          <el-input clearable autosize :minlength="1" maxlength="2047" show-word-limit="true" v-model="jobForm.request" type="textarea" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" auto-insert-space @click="positionAddOrUpdate">提交</el-button>
          <el-button auto-insert-space @click="resetForm">清空</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </el-container>
</template>

<script lang="ts" setup>
import {reactive, ref} from 'vue'
import {ElMessage} from "element-plus";
import {useRouter, useRoute} from "vue-router";
import {positionAddService} from '@/api/firm'
import { getPositionByIdService } from '@/api/position';
const router = useRouter();
const route = useRoute();

const jobForm = ref({
  jobName:'',
  hc:'',
  payroll:'',
  degree:'',
  city:'',
  location:'',
  request:'',
})

const getPositionById = async () => {
  let params = {
    id: route.query.id
  }
  let result = await getPositionByIdService(params);
  jobForm.value = result.data;
}

//如果时update，则获取数据
if(route.query.id!=undefined){
  getPositionById()
}

const positionAddOrUpdate = async ()=>{
  console.log(jobForm.value)
  let result = await positionAddService(jobForm.value);
  ElMessage.success(result.msg? result.msg:'添加成功')
  if(route.query.id===null){
    resetForm();
  }
  router.push('/firm/JobManage');
}

const resetForm = () =>{
  jobForm.value = {
    jobName:'',
    hc:1,
    payroll:0,
    degree:'',
    city:'',
    location:'',
    request:'',
  }
}
</script>

<style scoped>
.container {
  display: block;
  padding: 0;
  width: 100%;
}

.card {
  display: block;
  width: 70%;
  margin: 15%;
  margin-top: 0%;
}

.card-header {
  margin: 0;

  .card-header-title {
    margin: 0.7%;
    font-size: x-large;
    font-weight: bolder;
    color: #409EFF;
  }
}
</style>