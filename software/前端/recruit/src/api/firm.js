//导入request.js请求工具
import request from '@/utils/request.js'


//添加职位
export const positionAddService=(jobFormData)=> {
    return request.put('/firm/positionAdd', jobFormData)
}


//提供调用获取用户信息接口的函数
export const firmInfoService = () =>{
    return request.get('/firm/info');
}

//修改个人信息
export const firmInfoUpdateService = (firmInfoData)=>{
    return request.put('/firm/update',firmInfoData)
}

//修改密码
export const firmPasswordUpdateService = (passwordData) =>{
    return request.patch('/firm/updatePwd', passwordData)
}

//修改头像
//请求方式patch
export const firmAvatarUpdateService = (avatarUrl)=>{
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/firm/updateAvatar',params)
}

//获取职位list
export const firmPositionStatusListService = ()=>{
    return request.get('/firm/positionList');
}

//获取职位的投递数量
export const DeliveryCountService = (params) =>{
    return request.get('/firm/ResumeCount', {params:params})
}

//删除岗位
export const positionDeleteService=(params)=>{
    request.delete('/firm/positionDelete',  {params: params} )
}

//拒绝简历
export const rejectApplyById=(data)=> {
    const params = new URLSearchParams()    //创建x-www-form-urlencoded格式的对象
    for(let key in data){
        params.append(key,data[key]);   //x-www-form-urlencoded格式的数据
    }
    return request.patch('/firm/rejectApply',params)
}

//通过简历
export const passApplyById=(data)=> {
    const params = new URLSearchParams()    //创建x-www-form-urlencoded格式的对象
    for(let key in data){
        params.append(key,data[key]);   //x-www-form-urlencoded格式的数据
    }
    return request.patch('/firm/passApply',params)
}


//获取职位相关的所有申请
export const getApplyListByIdService=(params)=> {
    return request.get('/firm/applyListById', {params:params});
}

//更新查看时间
export const ReadTimeUpdateService=(data)=>{
    const params = new URLSearchParams()    //创建x-www-form-urlencoded格式的对象
    for(let key in data){
        params.append(key,data[key]);   //x-www-form-urlencoded格式的数据
    }
    return request.patch("/firm/readTimeUpdate", params)
}