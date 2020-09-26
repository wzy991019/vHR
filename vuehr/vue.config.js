//配置代理对象，解决开发环境中的跨域问题
let proxyobj = {};
//wobsocket协议配置
proxyobj['/ws'] = {
    ws: true,
    target: "ws://localhost:8081"
};
proxyobj['/']={
    ws: false,//关闭webstock
    target:'http://localhost:8081',//配置转发地址
    changeOrigin:true,
    pathRewrite:{//请求地址不做修改
        '^/':''
    }
}
module.exports={
    devServer:{
        host:'localhost',//ip地址
        port:8080,//端口号
        proxy:proxyobj//地理对象
    }
}