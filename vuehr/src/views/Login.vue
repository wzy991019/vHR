<template>
    <div>
        <el-form
                v-loading="loading"
                element-loading-text="正在登录.."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                :rules="rules" ref="loginForm" :model="loginForm" class="loginContainer">
            <h3 class="loginTitle">系统登录</h3>
            <el-form-item prop="username">
                <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="请输入密码" @keydown.enter.native="submitLogin"></el-input>
            </el-form-item>
            <el-checkbox class="loginRemember" v-model="checked">记住密码</el-checkbox>
            <el-button type="primary" style="width: 100%;" @click="submitLogin">登录</el-button>
        </el-form>
    </div>
</template>

<script>
    import {postKeyValueRequest} from "../utils/api";

    export default {
        name: "Login",
        data(){
            return {
                loading: false,
                loginForm:{
                    username: 'admin',
                    password: '123'
                },
                checked: true,
                rules:{
                    username:[{required:true,message:'请输入用户名',trigger:'blur'}],
                    password:[{required:true,message:'请输入密码',trigger:'blur'}]
                }
            }
        },
        methods:{
            submitLogin(){
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        this.postKeyValueRequest('/doLogin',this.loginForm).then(resp=>{
                            this.loading = false;
                            if (resp){
                                // 将登录的用户放在session里面
                               window.sessionStorage.setItem("user",JSON.stringify(resp.obj));
                               // 未登录的跳转获取路径
                               let path = this.$route.query.redirect;
                               // 获取当前对象的router对象,replace必能后退到前面一页，参数为页面的名称
                               this.$router.replace((path=='/'||path==undefined)?'/home':path);
                            }
                        })
                        // alert('submit!');
                    } else {
                        this.$message.error('请输入用户名和密码！')
                        return false;
                    }
                });
            }
        }
    }
</script>

<style>
    .loginContainer{
        border-radius: 15px;
        background-clip: padding-box;
        margin: 120px auto;
        width: 350px;
        padding: 15px 30px 15px 30px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
    }
    .loginTitle{
        margin: 15px auto 20px auto;
        text-align: center;
        color: #505458;
    }
    .loginRemember{
        text-align: left;
        margin: 0px 0px 20px 0px;
    }
</style>