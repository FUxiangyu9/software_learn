<template>
    <el-container style="width: 100%; height: 100%;">
        <el-container class="left">
            <div>
                <el-card class="table-card">
                    <template #header>
                        <el-container class="card-header">
                            <div class="card-header-left">
                                <h2 style="margin: 0.7%; color: #409EFF;">简历</h2>
                            </div>
                        </el-container>
                    </template>
                    <el-table :data="applyList">
                        <el-table-column label="编号" width="70" type="index"></el-table-column>
                        <el-table-column sortable label="名称" prop="uname"></el-table-column>
                        <el-table-column label="状态"
                                         prop="state"
                                         column-key="state"
                                         :filters="[
                                             {text:'已通过', value:'已通过'},
                                             {text:'已阅读', value:'已阅读'},
                                             {text:'已接受', value:'已接受'},
                                             {text:'已拒绝', value:'已拒绝'},
                                         ]"
                                         :filter-method="filterHandler"
                        ></el-table-column>
                        <el-table-column label="操作" width="230">
                            <template #default="{ row }">
                                <el-button round plain @click="lookResume(row)">查看</el-button>
                                <el-button round type="primary" plain @click="applyPass(row)">通过</el-button>
                                <el-button round type="danger" plain @click="applyReject(row)">拒绝</el-button>
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
</template>

<script lang="ts" setup>


import {ref} from "vue";
import {ElMessage, ElMessageBox, TableColumnCtx} from "element-plus";
import {useRoute} from "vue-router";
import VuePdfEmbed from "vue-pdf-embed";
import {getApplyListByIdService, passApplyById, rejectApplyById, ReadTimeUpdateService} from '@/api/firm'

const route = useRoute();

const applyList = ref([
  {
  uname:'',
  state:'',
  postTime:'',
  acceptTime:'',
  readTime:'',
  resumeRul:'',
  }
])
interface applyMsg {
  uname:string,
  state:string,
  postTime:string,
  acceptTime:string,
  readTime:string,
  resumeRul:string,
}

const resumeVisible = ref(false);
const resumeVisUrl = ref();

const filterHandler = (
    value: string,
    row: applyMsg,
    column: TableColumnCtx<applyMsg>
) => {
  console.log(value)
  console.log(row)
  console.log(column)
  const property = column['property']
  return row[property] === value
}

const getApplyListById = async() => {
  let params = {
      positionId:route.query.id
    }
  let result = await getApplyListByIdService(params)
  applyList.value = result.data;
}
getApplyListById();

const updateReadTime=async(params)=>{
  let result = await ReadTimeUpdateService(params);
  ElMessage.success(result.msg? result.msg:'状态已更新')
}

//查看简历
const lookResume = (row) =>{
  resumeVisible.value = true;
  resumeVisUrl.value = row.resumeUrl;
  if(row.readTime === null){
    let params = {
      positionId:row.positionId,
      userid:row.userid,
    }
    updateReadTime(params);
  }
}

const applyPass = (row) =>{
  let params = {
    positionId:row.positionId,
    userid:row.userid,
  }
  ElMessageBox.confirm(
      '你确认要通过该简历吗?',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        let result = await passApplyById(params);
        ElMessage({
          type: 'success',
          message: '操作成功',
        })
        if(result.code===0){
          row.state = "已通过"
        }
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '操作取消',
        })
      })
}

const applyReject= (row)=>{
  let params = {
    positionId:row.positionId,
    userid:row.userid,
  }
  ElMessageBox.confirm(
      '你确认要拒绝该简历吗?',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        let result = await rejectApplyById(params);
        ElMessage({
          type: 'success',
          message: '操作成功',
        })
        if(result.code===0){
          row.state = "已拒绝"
        }
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '操作取消',
        })
      })
}

</script>

<style scoped>
.left{
  float: left;
  display: block;
  width: 50%;
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
