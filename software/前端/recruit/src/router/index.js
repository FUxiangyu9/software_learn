import { createRouter, createWebHistory } from 'vue-router'

//导入组件
import LoginVue from '@/views/Login.vue'

// import ArticleCategoryVue from '@/views/article/ArticleCategory.vue'
// import ArticleManageVue from '@/views/article/ArticleManage.vue'

//定义路由关系
const routes = [
    //{路径, 组件}
    { path: '/', component: LoginVue },
    { path: '/login', component: LoginVue },
    // { path: '/position/list', component: PositionVue },
    {
        path: '/user', component: ()=>import("@/views/user/Layout.vue"),
        redirect:'/user/position/list', children: [  //redirect指的是跳转至/后，再次跳转的子页面
            { path: '/user/position/list', component: ()=>import("@/views/user/Position.vue") },
        //     { path: '/article/manage', component: ArticleManageVue },
            { path: '/user/resume', component: ()=>import("@/views/user/Resume.vue") },
            { path: '/user/info', component: ()=>import("@/views/user/Info.vue") },
            { path: '/user/avatar', component: ()=>import("@/views/user/Avatar.vue") },
            { path: '/user/resetPassword', component: ()=>import("@/views/user/ResetPassword.vue") },
            { path: '/user/position/detail', component: ()=>import("@/views/user/DetailOfPosition.vue")},
            { path: '/user/applystatus', component: ()=>import("@/views/user/ApplyStatus.vue")}
        ]
    },
    {
        path: '/firm', component: ()=>import("@/views/firm/Layout.vue"),
        redirect:'/firm/JobManage', children: [  //redirect指的是跳转至/后，再次跳转的子页面
            { path: '/firm/position/list', component: ()=>import("@/views/firm/Position.vue") },
            { path: '/firm/info', component: ()=>import("@/views/firm/Info.vue") },
            { path: '/firm/avatar', component: ()=>import("@/views/firm/Avatar.vue") },
            { path: '/firm/resetPassword', component: ()=>import("@/views/firm/ResetPassword.vue") },
            { path: '/firm/JobManage', component: ()=>import("@/views/firm/JobManage.vue") },
            { path: '/firm/deliveryManage', component: ()=>import("@/views/firm/DeliveryManage.vue") },
            { path: '/firm/positionAdd', component: ()=>import("@/views/firm/AddOrUpdateJobContent.vue")},
            { path: '/firm/positionModify', component: ()=>import("@/views/firm/AddOrUpdateJobContent.vue")},
            { path: '/firm/position/detail', component: ()=>import("@/views/firm/DetailOfPosition.vue")},
        //     { path: '/article/manage', component: ArticleManageVue },
            // { path: '/user/info', component: UserInfoVue }
        //     { path: '/user/avatar', component: UserAvatarVue },
        //     { path: '/user/resetPassword', component: UserResetPasswordVue }
        ]
    },
    {
        path: '/manager', component: ()=>import("@/views/manager/Layout.vue"),
        redirect:'/manager/info', children: [  //redirect指的是跳转至/后，再次跳转的子页面
            { path: '/manager/info', component: ()=>import("@/views/manager/Info.vue") },
            { path: '/manager/avatar', component: ()=>import("@/views/manager/Avatar.vue") },
            { path: '/manager/resetPassword', component: ()=>import("@/views/manager/ResetPassword.vue") },
            { path: '/manager/userManage', component: ()=>import("@/views/manager/UserManage.vue") },
            { path: '/manager/firmManage', component: ()=>import("@/views/manager/FirmManager.vue") },
        ]
    },
]

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由
export default router
