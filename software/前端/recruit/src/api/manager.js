//导入request.js请求工具
import request from '@/utils/request.js'

//提供调用获取用户信息接口的函数
export const managerInfoService = () =>{
    return request.get('/manager/info');
}

//修改用户信息
export const managerInfoUpdateService=(InfoData)=> {
    return request.put('/manager/update',InfoData)
}

//修改头像
//请求方式patch
export const managerAvatarUpdateService = (avatarUrl)=>{
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/manager/updateAvatar',params)
}

//修改密码
export const managerPasswordUpdateService = (passwordData) =>{
    return request.patch('/manager/updatePwd', passwordData)
}

//查询用户列表
export const UserListService=()=>{
    return request.get('/manager/userList')
}

//删除用户
export const UserDeleteService=(params)=>{
    return request.delete('/manager/userDelete', {params:params})
}

//查询企业列表
export const FirmListService = ()=>{
    return request.get('/manager/firmList')
}

//删除企业用
export const FirmDeleteService= (params)=>{
    return request.delete('/manager/firmDelete', {params:params})
}

//搜索用户
export const getUserListBySelect=(params)=>{
    return request.get('/manager/userListBySelect', {params:params})
}

//搜索企业
export const getFirmListBySelect=(params)=>{
    return request.get('/manager/firmListBySelect', {params:params})
}