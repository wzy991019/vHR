<template>
    <div>
        <div style="margin-top: 10px;display: flex;justify-content: center">
            <el-input @keydown.enter.native="doSearch" style="width: 400px;margin-right: 10px;" v-model="keywords" placeholder="通过用户名搜索用户.." prefix-icon="el-icon-search"></el-input>
            <el-button icon="el-icon-search" type="primary" @click="doSearch">搜索</el-button>
        </div>
        <div class="hr-container">
            <el-card class="hr-card" v-for="(hr,index) in hrs" :key="index">
                <div slot="header" class="clearfix">
                    <span>{{hr.name}}</span>
                    <el-button @click="deleteHr(hr)" style="float: right; padding: 3px 0;color: red" type="text" icon="el-icon-delete"></el-button>
                </div>
                <div>
                    <div class="img-container">
                        <img :src="hr.userface" :alt="hr.name" :title="hr.name" class="userface-img">
                    </div>
                    <div class="userinfo-container">
                        <div>用户名：{{hr.name}}</div>
                        <div>手机号码：{{hr.phone}}</div>
                        <div>电话号码：{{hr.telephone}}</div>
                        <div>地址：{{hr.address}}</div>
                        <div>用户状态：<el-switch @change="enabledChange(hr)" active-text="启用" active-color="#13ce66" inactive-text="禁用" inactive-color="#ff4949" v-model="hr.enabled"></el-switch></div>
                        <div>用户角色：<el-tag size="small"
                                      type="success"
                                      style="margin-right: 2px"
                                      v-for="(role,index) in hr.roles"
                                      :key="index">{{role.nameZh}}</el-tag>
                            <el-popover
                                    placement="right"
                                    title="角色列表"
                                    @show="showPop(hr)"
                                    @hide="hidePop(hr)"
                                    width="200"
                                    trigger="click">
                                <el-select multiple v-model="selectedRoles" placeholder="请选择">
                                    <el-option
                                            v-for="(r,indexk) in allroles"
                                            :key="indexk"
                                            :label="r.nameZh"
                                            :value="r.id">
                                    </el-option>
                                </el-select>
                                <el-button slot="reference" icon="el-icon-more" type="text" style="margin-left: 2px"></el-button>
                            </el-popover>
                        </div>
                        <div>备注：{{hr.remark}}</div>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SysHr",
        data(){
            return{
                keywords:'',
                hrs:[],
                allroles:[],
                selectedRoles:[]
            }
        },
        mounted() {
            this.initHrs();
        },
        methods:{
            deleteHr(hr){
                this.$confirm('此操作将永久删除【'+hr.name+'】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/hr/'+hr.id).then(resp=>{
                        if (resp){
                            this.initHrs();//刷新表格
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            doSearch(){
              this.initHrs();
            },
            hidePop(hr){
                let roles = [];
                Object.assign(roles,hr.roles);
                let flag = false;
                if (roles.length != this.selectedRoles.length){
                    flag = true;
                }else {
                    for (let i = 0; i < roles.length; i++){
                        let role = roles[i];
                        for (let j=0;j<this.selectedRoles.length;j++){
                            let sr = this.selectedRoles[j];
                            if (role.id==sr){
                                roles.splice(i,1);
                                i--;
                                break;
                            }
                        }
                    }
                    if (roles.length!=0){
                        flag = true;
                    }
                }
                if (flag){
                    let url = '/system/hr/role?hrid='+hr.id;
                    this.selectedRoles.forEach(sr=>{
                        url+='&rids='+sr;
                    })
                    this.putRequest(url).then(resp=>{
                        if (resp){
                            this.initHrs();
                        }
                    })
                }
            },
            showPop(hr){
                this.initAllRoles();
                let roles = hr.roles;
                this.selectedRoles = [];
                roles.forEach(r=>{
                    this.selectedRoles.push(r.id);
                })
            },
            initAllRoles(){
              this.getRequest('/system/hr/roles').then(resp=>{
                  if (resp){
                      this.allroles = resp;
                  }
              })
            },
            enabledChange(hr){
                delete hr.roles;
                this.putRequest('/system/hr/',hr).then(resp=>{
                    if (resp){
                        this.initHrs();
                    }
                })
            },
            initHrs(){
                this.getRequest('/system/hr/?keywords='+this.keywords).then(resp=>{
                    if (resp){
                        this.hrs=resp;
                    }
                })
            }
        }
    }
</script>

<style>
.hr-card{
    width: 350px;
    margin-bottom: 20px;
}
    .hr-container{
        margin-top: 10px;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
    }
    .userface-img{
        width: 72px;
        height: 72px;
        border-radius: 72px;
    }
    .img-container{
        width: 100%;
        display: flex;
        justify-content: center;
    }
    .userinfo-container{
        margin-top: 20px;
    }
    .userinfo-container div{
        font-size: 12px;
        color: rgba(41,157,255,0.73);
    }
</style>