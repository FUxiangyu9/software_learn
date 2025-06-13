<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'    //导入图标
import 'element-plus/theme-chalk/display.css'   //导入css
import { ref } from 'vue'
import {useRouter} from 'vue-router'
const router = useRouter();

const positionStatus = ref([
    {
        jobName: "去油自动化产线工程师",
        hc:   "99",
        deliveryNum:   "9999",
        payroll:    "111",
        passNum:  "10"
    }
])

import {userApplyStatusListService} from '@/api/user'
const getApplyStatus = async () =>{
    let result = await userApplyStatusListService();
    applyStatus.value = result.data;
}
// getPositionStatusList
const loading = ref(true)
import {firmPositionStatusListService} from '@/api/firm'
const getPositionStatusList = async() =>{
    positionStatus.value = null;
    let result = await firmPositionStatusListService();
    console.log(result.data)
    positionStatus.value = result.data;
    loading.value=false;
}
getPositionStatusList();

const DialogVisible = ref(false)
const DialogMsg = ref({})
const DeliveryMsg = ref({})
const DialogMsgTable = ref({})
import {DeliveryCountService} from '@/api/firm'
const showDialog = async(row) =>{
    DialogVisible.value = true;
    DialogMsg.value = row;
    console.log(row);
    let params = {positionId: row.id}
    console.log(params)
    let result = await DeliveryCountService(params);
    DeliveryMsg.value = result.data;
    console.log(DeliveryMsg.value)

    let completePercentage = 0;
    if(DialogMsg.value.hc===0) completePercentage = 0;
    else completePercentage = Math.floor(DeliveryMsg.value.passNum/DialogMsg.value.hc*100);
    if(completePercentage>=100) completePercentage = 100;
    DeliveryMsg.value.percentage = completePercentage;
    DialogMsgTable.value = {
        "职位": row.jobName,
        "薪资(K)": row.payroll,
        "城市": row.city,
        "学历要求": row.degree,
        "工作地址": row.location,
    }
}

import{WithDrawApplyService} from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus';
//撤回申请
const deleteApply = async(row) =>{
    let params = {
        positionId:row.positionId,
    }
    ElMessageBox.confirm(
        '你确认要删除这个职位吗?',
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
            message: '用户取消了删除',
        })
    })
}

//前往job详情页
const gotoJobDetial=(positionId)=>{
    router.push({path:'/firm/position/detail', query:{id:positionId}})
}

//前往投递管理页
const gotoDeliveryManager=(positionId)=>{
    console.log(positionId)
    router.push({path:'/firm/deliveryManage', query:{id:positionId}})
}

//修改job
const modifyPosition=(positionId)=>{
    router.push(({path:'/firm/positionModify', query:{id:positionId}}))
}

//添加岗位
const addPosition=()=>{
    router.push('/firm/positionAdd')
}

import {positionDeleteService} from '@/api/firm'
import { positionListService } from '@/api/position';
//删除岗位
const positionDelete= (row)=>{
    let params = {
        positionId: row.id,
    }
    ElMessageBox.confirm(
        '确认删除此岗位?',
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
        let result = await positionDeleteService(params)
        console.log(result)
        ElMessage({
            type: 'success',
            message: '删除成功',
        })
        loading.value = true;
        getPositionStatusList();
        console.log(positionStatus.value)
    })
    .catch(() => {
        ElMessage({
            type: 'info',
            message: '删除已取消',
        })
    })
}

</script>

<template>
    <el-container class="status-container">
        <el-card class="status-card">
            <template #header>
                <div class="card-header">
                    <span class="card-header-title">发布的岗位</span>
                    <el-button style="float: right;" type="primary" @click="addPosition()">添加岗位</el-button>
                </div>
            </template>
            <el-table :data="positionStatus"  v-loading="loading">
                <!-- 岗位，公司，城市，薪资，状态, 操作 -->
                <el-table-column label="编号" width="100" type="index"> </el-table-column>
                <el-table-column label="岗位" prop="jobName"></el-table-column>
                <el-table-column label="薪资(千)" prop="payroll"></el-table-column>
                <el-table-column label="招聘指标" prop="hc"></el-table-column>
                <el-table-column label="操作" width="200">
                    <template #default="{ row }">
                        <el-button round type="primary" @click="showDialog(row)">详情</el-button>
                        <el-button round type="danger" @click="positionDelete(row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <el-dialog v-model="DialogVisible" title="详细信息" center style="width: 75%;">
            <el-container>
                <div class="dialog-left">
                    <el-progress class="el-progress" type="dashboard" :percentage="DeliveryMsg.percentage">
                        <template #default="{ percentage }">
                            <span class="percentage-value">{{ percentage }}%</span>
                            <span class="percentage-label">Progressing</span>
                        </template>
                    </el-progress>
                </div>
                <div class="dialog-center">
                    <p>投递人数: {{ DeliveryMsg.deliveryNum }}人</p>
                    <p>通过人数: {{ DeliveryMsg.passNum }}人</p>
                    <p>招聘指标: {{ DialogMsg.hc }}人</p>
                </div>
                <div class="dialog-right">
                    <el-descriptions title="详细信息"
                                    :column="3"
                                    border
                    >
                        <template #extra>
                            <el-button type="primary" @click="gotoDeliveryManager(DialogMsg.id)">管理投递信息</el-button>
                            <el-button type="primary" @click="gotoJobDetial(DialogMsg.id)">前往职位详情页</el-button>
                            <el-button type="primary" @click="modifyPosition(DialogMsg.id)">修改</el-button>
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
            .card-header-title{
                margin: 0.7%;
                font-size:x-large;
                font-weight: bolder;
                color: #409EFF;
            }
        }
    }
}
.dialog-left{
    display: block;
    // float: left;
    text-align: center;
    width:20%;
    max-height:fit-content;
}
.dialog-center{
    display: block;
    width:15%;
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
.percentage-value {
    display: block;
    margin-top: 10px;
    font-size: 28px;
}
.percentage-label {
    display: block;
    margin-top: 10px;
    font-size: 12px;
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
    float: left;
    background: #F56C6C !important;
    color: #fefefe !important;
    border:0;
}
</style>