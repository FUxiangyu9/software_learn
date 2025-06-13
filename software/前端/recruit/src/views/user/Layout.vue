<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'

import {userInfoService} from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
//调用函数,获取用户详细信息
const getUserInfo = async()=>{
    //调用接口
    let result = await userInfoService();
    //数据存储到pinia中
    userInfoStore.setInfo(result.data);
}
getUserInfo();


//条目被点击后,调用的函数
import {useRouter} from 'vue-router'
const router = useRouter();
import {ElMessage,ElMessageBox} from 'element-plus'
import {ref} from "vue";
const handleCommand = (command) =>{
    //判断指令
    if(command === "logout"){
        //退出登录
        // alert("退出登录");
        ElMessageBox.confirm(
            "您确认要退出吗？",
            "温馨提示",
            {
                confirmButtonText:"确认",
                cancelButtonClass:"取消",
                type:"warning",
            }
        )
            .then(async() => {
                //退出登录
                //1.清空pinia中存储的token以及个人信息
                tokenStore.removeToken();
                userInfoStore.removeInfo();

                //2.跳转到登录页面
                router.push('/login');
                ElMessage({
                    type:"success",
                    message:"退出登录成功",
                })
            })
            .catch(()=>{
                ElMessage({
                    type:"info",
                    message:"用户取消了退出登录",
                })
            })
    }else{
        //路由
        router.push("/user/"+command)
    }
}

//
// const activeIndex = ref('1')
// const handleSelect = (key, keyPath) => {
//   console.log(key, keyPath)
// }

</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
<!--      header-->
      <el-header>
        <el-menu
            router
            :default-active="activeIndex"
            class="el-header-menu"
            mode="horizontal"
            @select="handleSelect"
        >
          <el-menu-item>
            <img
                style="width: 100px"
                src="@/assets/logo.png"
                alt="Element logo"
            />
          </el-menu-item>
          <el-menu-item index="/user/position/list"><span>首页</span></el-menu-item>
          <el-menu-item index="/user/resume">简历管理</el-menu-item>
          <el-menu-item index="/user/applystatus">投递状态</el-menu-item>
          <el-menu-item index="/user/info">个人信息</el-menu-item>
        </el-menu>
        <!-- <div>黑马程序员：<strong>nickname</strong></div> -->
        <!-- 下拉菜单 -->
        <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
        <div class="el-header-nameDisplay"><strong>{{ userInfoStore.info.account }}</strong></div>
        <div style="width: 15px"> </div>
        <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.avatarUrl? userInfoStore.info.avatarUrl : avatar" />
                      <!-- <el-avatar :src="avatar" /> -->
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <!--此处的command内容应与index.js中对应部分内容相同-->
              <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
              <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
              <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
<!--      main-->
      <el-main>
        <router-view></router-view>
      </el-main>
<!--      footer-->
      <el-footer class="layout-footer">会员制software</el-footer>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .el-header-menu{
          display: flex;
          width: 100%;
        }
        .el-header-nameDisplay{
          margin-left: auto;
        }
        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }
}
.layout-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        width: 100%;
        color: #666;
    }
</style>