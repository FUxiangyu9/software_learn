//导入request.js请求工具
import request from '@/utils/request.js'

//提供调用获取用户信息接口的函数
export const userInfoService = () =>{
    return request.get('/user/info');
}

//修改个人信息
export const userInfoUpdateService = (userInfoData)=>{
   return request.put('/user/update',userInfoData)
}

//修改头像
//请求方式patch
export const userAvatarUpdateService = (avatarUrl)=>{
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/user/updateAvatar',params)
}

//修改密码
export const userPasswordUpdateService = (passwordData) =>{
    return request.patch('/user/updatePwd', passwordData)
}

export const userResumeListService=()=>{
    return request.get('/user/resume');
}

export const userJobApplyService=(params)=>{
    return request.put('/user/apply', params);
}

//拿取申请信息
export const userApplyStatusListService=()=>{
    return request.get('/user/applyStatus')
}

//撤回申请(通过的无法撤回)
export const WithDrawApplyService = (data) =>{
    const params = new URLSearchParams()    //创建x-www-form-urlencoded格式的对象
    for(let key in data){
        params.append(key,data[key]);   //x-www-form-urlencoded格式的数据
    }
    return request.delete('/user/withdrawApply', {params:params})
}

//修改Resume
export const userResumeUpdateService = (data) =>{
    return request.put('/user/updateResume',data)
}

//删除
export const userResumeRemoveService = (data) =>{
    // const params = new URLSearchParams()    //创建x-www-form-urlencoded格式的对象
    // for(let key in data){
    //     params.append(key,data[key]);   //x-www-form-urlencoded格式的数据
    // }
    return  request.delete('/user/removeResume',  {params: data} )
}

//添加
export const userResumeAddService = (data) =>{
    return request.post('/user/upLoadResume',data)
}

//搜索工作
export const getPositionListBySelect=(params)=>{
    return request.get('/user/positionListBySelect', {params:params})
}













