//导入request.js请求工具
import request from '@/utils/request.js'
//导入Token工具
// import { useTokenStore } from '@/stores/token.js';

export const positionListService = () =>{
    // const tokenStore = useTokenStore();
    //在pinia中定义的响应式数据，都不需要.value就可以直接使用
    // return request.get('/position/getPositions', {headers:{'Authorization':tokenStore.token}});
    return request.get('/position/getPositions');
}

export const getPositionByIdService = (params) =>{
    return request.get('/position/getPositionById', {params:params});
}