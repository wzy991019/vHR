import axios from 'axios';
import { Message } from 'element-ui';
import router from "../router";
//响应拦截器实现错误提示
axios.interceptors.response.use(success=>{
    //约等于成功（不一定成功）
    if (success.status/* 状态码存在*/ && success.status == 200/*http里面的status*/ && success.data.status/*返回的json里面的status*/ == 500){
        // 说明这个是一个业务上的错误
        Message.error({message:success.data.msg})//此处的msg就是后台返回的json里面的msg
        return;
    }
    if (success.data.msg){
        Message.success({message:success.data.msg})
    }
    return success.data;// 此处表示成功
},error => {
    //一定是失败的
    if (error.response.status==504 || error.response.status==404){
        Message.error({message:'服务器被吃了( ╯□╰ )！'})
    }else if (error.response.status==403){
        Message.error({message:'权限不足，请联系管理员！'})
    }else if (error.response.status==401){
        Message.error({message:'尚未登录，请登录！'});
        router.replace('/');//回到登录页
    }else {
        if (error.response.data.msg){
            Message.error({message:error.response.msg})
        }else {
            Message.error({message:'未知错误！'})
        }
    }
    return;
})

// 定义全局变量,防止以后增加前缀之类的
let base = '';

//登录请求的参数只支持key-value的形式传参，其他的形式不支持
export const postKeyValueRequest = (url,params)=>{
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        // 将json转为key-value的形式
        transformRequest: [function (data) {
            let ret = '';
            for (let i in data){
                ret+=encodeURIComponent(i)+'='+encodeURIComponent(data[i])+'&'
            }
            return ret;
        }],
        headers:{
            'Content-Type':'application/x-www-form-urlencoded'
        }
    })
}
//封装传递json的post
export const postRequest=(url,params)=>{
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params
    })
}
//封装传递json的put
export const putRequest=(url,params)=>{
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}
//封装传递json的get
export const getRequest=(url,params)=>{
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}
//封装传递json的post
export const deleteRequest=(url,params)=>{
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
}