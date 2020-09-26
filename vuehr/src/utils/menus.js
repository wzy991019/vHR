import {getRequest} from "./api";

export const initMenu=(router,store)=>{
    if (store.state.routes.length > 0){
        return;//有菜单数据到此结束，不用再继续加载
    }
    getRequest('/system/config/menu').then(data=>{
        if (data){
            let fmtRoutes = formatRoutes(data);//将接收到的字符串转化为一个变量
            router.addRoutes(fmtRoutes);//将转换后的json放进router
            store.commit('initRoutes',fmtRoutes);//将数据动态放进store中（vuex）

        }
    })
}

export const formatRoutes=(routes)=>{
    let fmRoutes = [];//定义要返回的数据

    routes.forEach(router=>{
        let {
            path,
            component,
            name,
            meta,
            iconCls,
            children
        } = router;//将router里面的属性值赋值给对应的变量
        if (children && children instanceof Array){//说明这个是一个一级菜单
            children=formatRoutes(children);//递归调用
        }
        let fmRouter={
            path:path,
            name:name,
            iconCls:iconCls,
            meta:meta,
            children:children,
            component(resolve){
                if (component.startsWith("Home")){
                    require(['../views/'+component+'.vue'],resolve);//拼接完整路径
                }else if (component.startsWith("Emp")){
                    require(['../views/emp/'+component+'.vue'],resolve);//拼接完整路径
                }else if (component.startsWith("Per")){
                    require(['../views/per/'+component+'.vue'],resolve);//拼接完整路径
                }else if (component.startsWith("Sal")){
                    require(['../views/sal/'+component+'.vue'],resolve);//拼接完整路径
                }else if (component.startsWith("Sta")){
                    require(['../views/sta/'+component+'.vue'],resolve);//拼接完整路径
                }else if (component.startsWith("Sys")){
                    require(['../views/sys/'+component+'.vue'],resolve);//拼接完整路径
                }
            }
        }
        fmRoutes.push(fmRouter);
    })

    return fmRoutes;
}