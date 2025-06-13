<script setup>
import {
  Edit,
  Delete, Search
} from '@element-plus/icons-vue'
import 'element-plus/theme-chalk/display.css'
//条目被点击后,调用的函数
import {useRouter} from 'vue-router'
const router = useRouter();

import { ref } from 'vue';
const positions = ref([
    {
        "id": "",
        "firmid": '',
        "hc": '',
        "payroll": '',
        "jobName":'',
        "city":'',
        "degree":''
    }
])
//声明一个异步的函数

import { positionListService } from '@/api/position';
import {getPositionListBySelect} from "@/api/user.js";
import {ElMessage} from "element-plus";
const positionList = async () => {
    let result = await positionListService();
    positions.value = result.data;
}
//调用函数
positionList();

//跳转到卡片详情
const gotoDetailOfPosition = (id) =>{
  router.push({path:'/user/position/detail', query:{id:id}});
}

const input = ref()
const select = ref()

const selectBy=async (select, input)=>{
  let params = {
    selectType:select,
    input:input,
  }
  console.log(params)
  if(params.selectType===undefined) params.selectType=1;
  let result = await getPositionListBySelect(params);
  positions.value = result.data;
  ElMessage.success(result.msg? result.msg:'搜索成功')
}

</script>
<template>
  <el-container style="width: 100%; display: block">
    <div style="width: 60%; margin-left:20%; margin-right: 20%">
      <el-input
          v-model="input"
          style="max-width: 600px"
          placeholder="请输入内容"
          size="large"
          type="text"
      >
        <template #prepend>
          <el-select v-model="select" size="large" placeholder="请选择" style="width: 115px">
            <el-option label="工作名称" value="1" />
            <el-option label="城市" value="2" />
            <el-option label="公司" value="3" />
            <el-option label="学历" value="4" />
          </el-select>
        </template>
        <template #append>
          <el-button :icon="Search" @click="selectBy(select, input)" />
        </template>
      </el-input>
    </div>
  </el-container>
  <el-container class="position-container">
    <el-empty v-if="positionList.value===undefined" style="margin-top:10%;" description="没有工作" />
    <el-row v-else>
<!--      warp效果-->
      <el-space wrap style="min-width: 100%;">
            <el-card class="el-job-card" shadow="hover" v-for="item in positions" @click="gotoDetailOfPosition(item.id)">
              <p class="el-job-p">
                <span class="fl zp-job-title">{{ item.jobName }}</span>
                <img src="" class="fl zp-job__label-proxy">
                <span class="fr zp-job-salary">{{item.payroll}}K</span>
              </p>
              <p class="el-job-p">
                <span class="zp-job-condition-label">{{ item.city }} | {{ item.degree }}</span>
              </p>
              <hr>
              <p class="el-job-p">
                <span class="clearfix zp-job-company-info">{{item.firmCorp}}</span>
              </p>
              <p class="el-job-p">
                <span class="zp-job-company-hc-type">招收：{{item.hc}}人</span>
              </p>
            </el-card>

      </el-space>
    </el-row>
  </el-container>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;
    .el-row{
        flex-direction: column;
        align-items: center;
    }
    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

}
.position-container{
  width: 100%;
  display: block;
  .el-job-card{
    min-width: 30%;
    padding: 0;
    display: flex;
    position: relative;
    cursor: pointer;
    .card-content {
      white-space: normal;
    }
    .el-job-p{
      margin: 0px;
      display: flex;
    }
    .clearfix {
      zoom: 1;
      display: block;
    }

    .fl {
      float: left;
    }
    .fr {
      float: right;
    }
    .zp-job-title {
      min-width: 150px;
      font-size: 18px;
      color: #333;
      font-weight: 700;
      max-width: 200px;
    }
    .zp-job-salary {
      font-size: 18px;
      color: #ff5959;
      font-weight: 700;
    }
    .zp-job__label-proxy {
      height: 20px;
      margin-left: 75px;
      position: relative;
      top: 4px;
    }
    .zp-job-condition-label {
      font-size: 16px;
      color: #666;
      margin-right: 10px;
    }
    .zp-job-company-info {
      font-size: small;
    }
    .zp-job-company-hc-type{
      font-size: xx-small;
    }
  }
}
</style>