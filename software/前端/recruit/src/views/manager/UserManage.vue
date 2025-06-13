<script setup>
import {UserListService, UserDeleteService, getUserListBySelect} from '@/api/manager.js'
import {ElMessage} from "element-plus";
import {ref} from "vue";
import { Search } from '@element-plus/icons-vue'

const userList = ref([
  {}
])
const getUserList=async ()=>{
  let result = await UserListService();
  console.log(result.data)
  userList.value = result.data;
}
getUserList();

const deleteUser=async (row)=>{
  let params = {
    uid: row.uid,
  }
  let result = await UserDeleteService(params);
  ElMessage.success(result.msg? result.msg:'删除成功')
  getUserList();
}

const input = ref()
const select = ref()

const selectBy=async (select, input)=>{
  let params = {
    selectType:select,
    input:input,
  }
  if(params.selectType===undefined) params.selectType=1;
  let result = await getUserListBySelect(params);
  userList.value = result.data;
  ElMessage.success(result.msg? result.msg:'搜索成功')
}

</script>

<template>
  <el-container class="status-container">
    <el-card class="status-card">
      <template #header>
        <div class="card-header">
          <span class="card-header-title">用户账号</span>
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
                </el-select>
              </template>
              <template #append>
                <el-button :icon="Search" @click="selectBy(select, input)" />
              </template>
            </el-input>
          </div>
        </div>
      </template>
      <el-table :data="userList" height="400">
        <!-- 岗位，公司，城市，薪资，状态, 操作 -->
        <el-table-column fixed label="uid"  prop="uid"></el-table-column>
        <el-table-column label="账号名" prop="account"></el-table-column>
        <el-table-column label="用户名" prop="uname"></el-table-column>
        <el-table-column label="电话" prop="tel"></el-table-column>
        <el-table-column label="学历" prop="degree"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button round type="danger" @click="deleteUser(row)">删除</el-button>
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
    margin: 10%;
    margin-top: 2%;
    max-height: 100%;

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
.input-with-select .el-input-group__prepend {
  background-color: var(--el-fill-color-blank);
  float: right;
}
.card-header-title{
  margin: 0.7%;
  font-size:x-large;
  font-weight: bolder;
  color: #409EFF;
}
</style>