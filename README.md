# vHR
基于前后端分离的微人事系统，采用vue3.0以及springboot2.3.3的前后端架构
加入了RabbitMQ来进行消息队列的存储与转发，实现了邮件的发送
数据库层面使用Mysq8.0.19以及Druid连接池进行连接
实现了模块的重构，使得部分业务可以进行分离，并且实现不同端口号之间的跨域处理
前端使用了vue的axious来进行跨域处理以及vuex来进行数据的存储
