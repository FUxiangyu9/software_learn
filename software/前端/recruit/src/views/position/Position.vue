<script setup>
import {
    Edit,
    Delete
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
const positionList = async () => {
    let result = await positionListService();
    positions.value = result.data;
}
//调用函数
positionList();

//跳转到卡片详情
const gotoDetailOfPosition = (id) =>{
  router.push({path:'/position/detail', query:{id:id}});
}

// articleCategoryList();
// //控制添加分类弹窗
// const dialogVisible = ref(false)

// //添加分类数据模型
// const categoryModel = ref({
//     categoryName: '',
//     categoryAlias: ''
// })
// //添加分类表单校验
// const rules = {
//     categoryName: [
//         { required: true, message: '请输入分类名称', trigger: 'blur' },
//     ],
//     categoryAlias: [
//         { required: true, message: '请输入分类别名', trigger: 'blur' },
//     ]
// }


// //调用接口,添加表单
// import { ElMessage } from 'element-plus'
// const addCategory = async () => {
//     //调用接口
//     let result = await articleCategoryAddService(categoryModel.value);
//     ElMessage.success(result.msg ? result.msg : '添加成功')

//     //调用获取所有文章分类的函数
//     articleCategoryList();
//     dialogVisible.value = false;
// }

// //定义变量,控制标题的展示
// const title = ref('')

// //展示编辑弹窗
// const showDialog = (row) => {
//     dialogVisible.value = true; title.value = '编辑分类'
//     //数据拷贝
//     categoryModel.value.categoryName = row.categoryName;
//     categoryModel.value.categoryAlias = row.categoryAlias;
//     //扩展id属性,将来需要传递给后台,完成分类的修改
//     categoryModel.value.id = row.id
// }

// //编辑分类
// const updateCategory = async () => {
//     //调用接口
//     let result = await articleCategoryUpdateService(categoryModel.value);

//     ElMessage.success(result.msg ? result.msg : '修改成功')

//     //调用获取所有分类的函数
//     articleCategoryList();

//     //隐藏弹窗
//     dialogVisible.value = false;
// }

// //清空模型的数据
// const clearData = () => {
//     categoryModel.value.categoryName = '';
//     categoryModel.value.categoryAlias = '';
// }

// //删除分类
// import {ElMessageBox} from 'element-plus'
// const deleteCategory = (row) => {
//     //提示用户  确认框

//     ElMessageBox.confirm(
//         '你确认要删除该分类信息吗?',
//         '温馨提示',
//         {
//             confirmButtonText: '确认',
//             cancelButtonText: '取消',
//             type: 'warning',
//         }
//     )
//         .then(async () => {
//             //调用接口
//             let result = await articleCategoryDeleteService(row.id);
//             ElMessage({
//                 type: 'success',
//                 message: '删除成功',
//             })
//             //刷新列表
//             articleCategoryList();
//         })
//         .catch(() => {
//             ElMessage({
//                 type: 'info',
//                 message: '用户取消了删除',
//             })
//         })
// }
</script>
<template>
<!--  工作推荐-->

  <el-container class="position-container">
    <el-row><el-col><div/>工作</el-col></el-row>
    <el-row>
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
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true; title = '添加分类'; clearData()">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="positions" style="width: 100%">
            <el-table-column label="编号" width="100" type="index"> </el-table-column>
            <el-table-column label="firmid" prop="firmid"></el-table-column>
            <el-table-column label="职位要求" prop="request"></el-table-column>
            <el-table-column label="招聘人数" prop="hc"></el-table-column>
            <el-table-column label="薪资" prop="payroll"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title == '添加分类' ? addCategory() : updateCategory()"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
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