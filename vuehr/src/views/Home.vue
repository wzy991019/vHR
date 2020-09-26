<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <div class="title">微人事</div>
                <div>
                    <el-button icon="el-icon-bell" size="normal" type="text" style="margin-right: 10px" @click="goChat"></el-button>
                    <el-dropdown class="userInfo" @command="commandHandler">
                  <span class="el-dropdown-link">
                    {{user.name}}<i ><img :src="user.userface" alt=""></i>
                  </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
                            <el-dropdown-item command="setting">设置</el-dropdown-item>
                            <el-dropdown-item divided command="logout">注销登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu router unique-opened>
                        <el-submenu :index="index+''" v-for="(item,index) in routes" :key="index" v-if="!item.hidden">
                            <template slot="title">
                                <i style="color: gray" :class="item.iconCls"></i>
                                <span style="margin-left: 5px">{{item.name}}</span>
                            </template>
                                <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" :key="indexj">{{child.name}}</el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/home'">
                        欢迎来到微人事
                    </div>
                <!--此时有两个router-view页面不知道该替代哪个-->
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data(){
            return{
                user: JSON.parse(window.sessionStorage.getItem("user"))
            }
        },
        computed: {
          routes() {
              return this.$store.state.routes;
          }
        },
        methods:{
            goChat(){
              this.$router.push("/chat");
            },
            commandHandler(cmd){
                if (cmd=='logout'){
                    this.$confirm('此操作将注销登录, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.getRequest('/logout');
                        window.sessionStorage.removeItem('user');
                        this.$store.commit('initRoutes',[]);//清空菜单
                        this.$router.replace('/');
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    });
                }
            }
        }
    }
</script>

<style>
.homeHeader{
    background-color: #000000;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0px 15px;
    box-sizing: border-box;
}
    .title{
        font-size: 30px;
        font-family: 宋体;
        color: #ffffff;
    }
    .homeHeader userInfo{
        cursor: pointer;
    }
    .el-dropdown-link img {
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }
    .el-dropdown-link{
        display: flex;
        align-items: center;
        color: #ffffff;
    }
    .homeWelcome{
        text-align: center;
        font-size: 50px;
        font-family: 华文行楷;
        color: black;
        padding-top: 100px;
    }
    .homeRouterView{
        margin-top: 10px;
    }
</style>