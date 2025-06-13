<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'    //导入图标
import 'element-plus/theme-chalk/display.css'   //导入css
import { ref } from 'vue'
import {useRouter} from 'vue-router'
const router = useRouter();

const applyStatus = ref([
    {
        jobName: "去油自动化产线工程师",
        firmCorp:   "Auschwitz Crop",
        city:   "Poland Cracow",
        payroll:    "111",
        state:  "已申请"
    }
])

import {userApplyStatusListService} from '@/api/user'
const getApplyStatus = async () =>{
    let result = await userApplyStatusListService();
    applyStatus.value = result.data;
}
getApplyStatus();

const DialogVisible = ref(false)
const DialogMsg = ref({})
const DialogMsgTable = ref({})
const showDialog = (row) =>{
    DialogVisible.value = true;
    DialogMsg.value = row;
    DialogMsgTable.value = {
        "职位": row.jobName,
        "公司": row.firmCorp,
        "薪资": row.payroll,
        "学历": row.degree,
        "城市": row.city,
        "工作地址": row.location,
        "状态": row.state,
    }
}

import{WithDrawApplyService} from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus';
//撤回申请
const deleteApply = (row) =>{
    let params = {
        positionId:row.positionId,
    }
    ElMessageBox.confirm(
        '你确认要撤回这条申请吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
    .then(async () => {
        //调用接口
        let result = await WithDrawApplyService(params);
        ElMessage({
            type: 'success',
            message: '删除成功',
        })
        //刷新列表
        getApplyStatus();
    })
    .catch(() => {
        ElMessage({
            type: 'info',
            message: '用户取消了撤回',
        })
    })
}

//前往job详情页
const gotoJobDetial=(positionId)=>{
    router.push({path:'/user/position/detail', query:{id:positionId}})
}

</script>

<template>
    <el-container class="status-container">
        <el-card class="status-card">
            <template #header>
                <div class="card-header">
                    <h2 style="margin: 0.7%;">投递状态</h2>
                </div>
            </template>
            <el-table :data="applyStatus">
                <!-- 岗位，公司，城市，薪资，状态, 操作 -->
                <el-table-column label="编号" width="100" type="index"> </el-table-column>
                <el-table-column label="岗位" prop="jobName"></el-table-column>
                <el-table-column label="公司" prop="firmCorp"></el-table-column>
                <el-table-column label="城市" prop="city"></el-table-column>
                <el-table-column label="薪资(千)" prop="payroll"></el-table-column>
                <el-table-column label="状态" prop="state"></el-table-column>
                <el-table-column label="操作" width="200">
                    <template #default="{ row }">
                        <el-button round type="primary" @click="showDialog(row)">详情</el-button>
                        <el-button round type="danger" @click="deleteApply(row)">撤回</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <el-dialog v-model="DialogVisible" title="详细信息" center style="width: 75%;">
            <el-container>
                <div class="dialog-left">
                <el-timeline style="width: 100%;">
                    <el-timeline-item :type="'primary'" :hollow="true" v-show="DialogMsg.postTime!=null" :timestamp="DialogMsg.postTime"> 发送时间 </el-timeline-item>
                    <el-timeline-item :timestamp="DialogMsg.postTime" v-show="DialogMsg.postTime==null"> 发送时间 </el-timeline-item>
                    <el-timeline-item :type="'primary'" :hollow="true" v-show="DialogMsg.readTime!=null" :timestamp="DialogMsg.readTime"> 阅读 </el-timeline-item>
                    <el-timeline-item v-show="DialogMsg.readTime==null" :timestamp="DialogMsg.readTime"> 阅读 </el-timeline-item>
                    <el-timeline-item :type="'primary'" :hollow="true" v-show="DialogMsg.acceptTime!=null" :timestamp="DialogMsg.acceptTime"> 决定 </el-timeline-item>
                    <el-timeline-item v-show="DialogMsg.acceptTime==null" :timestamp="DialogMsg.acceptTime"> 决定 </el-timeline-item>
                </el-timeline>
            </div>
            <div class="dialog-right">
                <el-descriptions title="详细信息"
                                :column="3"
                                border
                >
                    <template #extra>
                        <el-button type="primary" @click="gotoJobDetial(DialogMsg.positionId)">前往职位详情页</el-button>
                    </template>
                    <el-descriptions-item v-for="(value, key) in DialogMsgTable">
                      <template #label>
                        <div class="cell-item">{{ key }}</div>
                      </template>
                      {{ value }}
                    </el-descriptions-item>
                </el-descriptions>
            </div>
            </el-container>
        </el-dialog>
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
</style>