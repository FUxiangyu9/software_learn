<script setup>
import { ref } from 'vue'
import { getPositionByIdService } from '@/api/position';
import {useRouter, useRoute} from 'vue-router'
import { ElMessage } from 'element-plus';
import { userResumeListService } from '@/api/user';
import {userJobApplyService} from "@/api/user"
import useUserInfoStore from '@/stores/userInfo.js'
const jobDetail = ref({
  "id": "2",
  "firmid": 2,
  "location": "xxx产业园",
  "city":"qingdao",
  "degree": "本科",
  "request": "民主",
  "hc": 4,
  "payroll": 4333,
  "firmCorp": "东京烧烤股份有限公司",
});

const router = useRouter();
const route = useRoute();
const getPositionById = async () => {
    let params = {
      id:route.query.id
    }
    let result = await getPositionByIdService(params);
    jobDetail.value = result.data;
}
//调用函数
getPositionById();

//选择简历
// import { ElMessage, ElMessageBox } from 'element-plus'
// import type { Action } from 'element-plus'
// const selectResume = () => {
//   ElMessageBox.alert('This is a message', 'Title', {
//     // if you want to disable its autofocus
//     // autofocus: false,
//     confirmButtonText: 'OK',
//     callback: (action: Action) => {
//       ElMessage({
//         type: 'info',
//         message: `action: ${action}`,
//       })
//     },
//   })
// }

const DialogVisible = ref(false);
const resumeInfo = ref([
])
const SelectResumeDialog = async() =>{
    let result = await userResumeListService();
    resumeInfo.value = result.data;
}


const userInfoStore = useUserInfoStore();
const userInfo = ref({...userInfoStore.info})
console.log(userInfo.value)

const resumeId = ref('')
const jobApply = async () =>{
    const params = {
        "positionId" : jobDetail.value.id,
        "resumeId" : resumeId.value,
        "userid" : userInfo.value.uid,
        "firmid" : jobDetail.value.firmid
    }
    let result = await userJobApplyService(params);
    ElMessage.success(result.msg? result.msg:'操作成功')
    if(result.code===0) router.push('/user/states')
}


</script>

<template>
  <el-container class="job-header">
    <div class="job-header-content">
      <h2>岗位名称</h2>
      <div class="clearfix">
        <div class="job-header-left">
          <span class="job-payroll">{{ jobDetail.payroll }}K</span>
          <p style="margin-top: 10px; text-align: left;">{{ jobDetail.city }} | {{ jobDetail.degree }} | 招{{ jobDetail.hc }}人</p>
        </div>
        <div class="job-header-right">
          <div class="job-header-action">
            <el-button  type="primary" class="job-action-button" @click="SelectResumeDialog(); DialogVisible=true">申请职位</el-button>
            <el-dialog v-model="DialogVisible" title="请选择简历" center>
              <el-select
                v-model="resumeId"
                placeholder="请选择简历"
                size="large"
                style="width: 240px"
              >
                <el-option v-for="resume in resumeInfo" :label="resume.resumeName" :value="resume.id" />
              </el-select>
              <div class="dialog-submit-button">
                <el-button type="primary" size="large" @click="jobApply(); DialogVisible=false">提交</el-button>
                <el-button type="primary" size="large" @click="DialogVisible=false">取消</el-button>
              </div>

            </el-dialog>
          </div>
        </div>
      </div>
    </div>
    <br>
  </el-container>
  <el-container class="job-detail">
    <div class="discription">
      <h3 class="describtion__title">职位描述：</h3>
      <div class="describtion__detail-content">{{ jobDetail.request }}</div>
    </div>
    <div class="job-address clearfix" style="clear: both;">
      <h3 class="job-address__title">工作地点:</h3>
      <div class="job-address__content">{{ jobDetail.location }}</div>
    </div>
  </el-container>
  <!-- <a>{{ position.value.id }}</a> -->
</template>

<style scoped lang="scss">
.job-header{
  display: auto;
  width: 100%;
  height: 35%;
  .job-header-content{
    display: auto;
    margin: 0 auto;
    position: relative;
    width: 80%;
    margin-top: 1%;
  }
  .job-header-left{
    float:left;
    .job-payroll{
      color: #426eff;
      font-size: 24px;
      font-weight: 600;
      margin: 0;
      overflow: hidden;
      padding-right: 15px;
    }
    .job-info{
      margin-top: 12px;
    }
  }
  .job-header-right{
    float: right;
    margin-top: 15px;
    .job-header-action{
      -webkit-box-align: start;
      -ms-flex-align: start;
      align-items: flex-start;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      justify-content: center;
      .job-action-button{
        font-size: 16px;
        font-weight: 500;
        height: 48px;
        width: 220px;
        background: #426eff;
        border-color: #426eff;
        border-width: 1px;
      }
    }
  }
}

.job-detail{
  display: block;
  background-color: white;
  min-height: 65%;
  margin: 0;
  .discription{
    display: block;
    margin: 0 auto;
    margin-bottom: 20px;
    position: relative;
    width: 80%;
    .describtion__detail-content{
      white-space: pre-wrap;
    }
  }

}
.job-address{
    display: block;
    color: #222;
    margin-bottom: 20px;
    position: relative;
  }
.job-address__title{
    color: #222;
    margin: 0 auto;
    margin-bottom: 10px;
    position: relative;
    width: 80%;

}
.job-address__content{
    color: #222;
    margin-top: 16px;
    position: relative;
    width: 80%;
    margin: 0 auto;
}
.clearfix{
  display: block;
  zoom: 1;
}
.dialog-submit-button{
  float:right;
}
</style>