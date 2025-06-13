<script setup>
import {FirmListService, FirmDeleteService, getFirmListBySelect} from '@/api/manager.js'
import {ElMessage} from "element-plus";
import {ref} from "vue";
import {Search} from "@element-plus/icons-vue";

const firmList = ref([
  {}
])
const getFirmList=async ()=>{
  let result = await FirmListService();
  console.log(result.data)
  firmList.value = result.data;
}
getFirmList();

const deleteFirm=async (row)=>{
  let params = {
    firmUid: row.firmUid,
  }
  let result = await FirmDeleteService(params);
  ElMessage.success(result.msg? result.msg:'删除成功')
  getFirmList();
}

const input = ref()
const select = ref()

const selectBy=async (select, input)=>{
  let params = {
    selectType:select,
    input:input,
  }
  if(params.selectType===undefined) params.selectType=1;
  let result = await getFirmListBySelect(params);
  firmList.value = result.data;
  ElMessage.success(result.msg? result.msg:'搜索成功')
}

</script>

<template>
  <el-container class="status-container">
    <el-card class="status-card">
      <template #header>
        <div class="card-header">
          <span class="card-header-title">企业账号</span>
          <div style="float:right;">
            <el-input
                v-model="input"
                style="max-width: 400px"
                placeholder="请输入内容"
                class="input-with-select"
            >
              <template #prepend>
                <el-select v-model="select" placeholder="请选择" style="width: 115px">
                  <el-option label="uid" value="1" />
                  <el-option label="账号名" value="2" />
                  <el-option label="用户名" value="3" />
                  <el-option label="电话" value="4" />
                  <el-option label="公司" value="5" />
                </el-select>
              </template>
              <template #append>
                <el-button :icon="Search" @click="selectBy(select, input)" />
              </template>
            </el-input>
          </div>
        </div>
      </template>
      <el-table :data="firmList" height="400">
        <!-- 岗位，公司，城市，薪资，状态, 操作 -->
        <el-table-column fixed label="uid" prop="firmUid"></el-table-column>
        <el-table-column label="账号名" prop="firmAccount"></el-table-column>
        <el-table-column label="用户名" prop="firmUname"></el-table-column>
        <el-table-column label="电话" prop="firmTel"></el-table-column>
        <el-table-column label="公司" prop="firmCorp"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button round type="danger" @click="deleteFirm(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </el-container>
</template>

<style scoped lang="scss">
.status-container{
  width: 100%;
  display: block;

  .status-card{
    display: block;
    max-height: 100%;
    margin: 10%;
    margin-top: 2%;

    .card-header{
      margin: 0;
      color: #409EFF;
    }
  }
}
.dialog-left{
  display: inline-block;
  // float: left;
  width:35%;
  max-height:fit-content;
}
.dialog-right{
  // float: right;
  display: inline-block;
  width:65%;
  max-height:fit-content;
}
.cell-item {
  display: flex;
  align-items: center;
}
.card-header-title{
  margin: 0.7%;
  font-size:x-large;
  font-weight: bolder;
  color: #409EFF;
}
</style>