<template>
    <div v-loading="globalLoading"
         element-loading-text="正在添加.."
         element-loading-spinner="el-icon-loading"
         element-loading-background="rgba(0, 0, 0, 0.8)">
        <div class="permissManaTool">
            <el-input size="small" placeholder="请输入角色英文名" v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input size="small" placeholder="请输入角色中文名" v-model="role.nameZh" @keydown.enter.native="doAddRole"></el-input>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="doAddRole">添加角色</el-button>
        </div>
        <div class="permissManaMain">
            <el-collapse
                    v-loading="loading"
                    element-loading-text="正在加载.."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    v-model="activeName"
                    accordion
                    @change="change">
                <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r,index) in roles" :key="index">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>可访问的资源</span>
                            <el-button @click="deleteRole(r)" style="float: right; padding: 3px 0;color: red" icon="el-icon-delete" type="text"></el-button>
                        </div>
                        <div>
                            <el-tree show-checkbox ref="tree" :key="index" node-key="id" :default-checked-keys="selectedMenus" :data="allMenus" :props="defaultProps"></el-tree>
                            <div style="display: flex;justify-content: flex-end">
                                <el-button size="mini" @click="cancelUpdate">取消修改</el-button>
                                <el-button size="mini" type="primary" @click="doUpdate(r.id,index)">确认修改</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PemissMana",
        data(){
            return{
                globalLoading:false,
                loading:false,
                role:{
                    name:'',
                    nameZh:''
                },
                allMenus:[],
                roles:[],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                selectedMenus:[],
                activeName: -1
            }
        },
        mounted() {
            this.initRoles();
        },
        methods:{
            deleteRole(role){
                this.$confirm('此操作将永久删除【'+role.nameZh+'】角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/permiss/role/'+role.id).then(resp=>{
                        if (resp){
                            this.initRoles();//刷新表格
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            doAddRole(){
                if (this.role.name&&this.role.nameZh){
                    this.globalLoading = true;
                    this.postRequest('/system/basic/permiss/role',this.role).then(resp=>{
                        this.globalLoading = false;
                        if (resp){
                            this.role.name='';
                            this.role.nameZh='';
                            this.initRoles();
                        }
                    })
                }else {
                    this.$message.error("数据不可以为空！！");
                }

            },
            cancelUpdate(){
              this.activeName = -1;//关闭所有的树
            },
            doUpdate(rid,index){
                let tree = this.$refs.tree[index];//获取第几个树
                let selectedKeys = tree.getCheckedKeys(true);//获取选择的节点，除了根节点和一级菜单之外的
                let url = '/system/basic/permiss/?rid='+rid;
                selectedKeys.forEach(key=>{
                    url+='&mids='+key;//循环拼接请求地址
                });
                this.putRequest(url).then(resp=>{
                    if (resp){
                        this.initRoles();
                        this.activeName = -1;
                    }
                })
            },
            change(rid){
                if (rid){
                    this.initAllMenus();
                    this.initSelectedMenus(rid)
                }
            },
            initSelectedMenus(rid){
                this.getRequest('/system/basic/permiss/mids/'+rid).then(resp=>{
                    if (resp){
                        this.selectedMenus = resp;
                    }
                })
            },
            initAllMenus(){
              this.getRequest('/system/basic/permiss/menus').then(resp=>{
                  if (resp){
                      this.allMenus = resp;
                  }
              })
            },
            initRoles(){
                this.loading = true;
                this.getRequest('/system/basic/permiss/').then(resp=>{
                    this.loading = false;
                    if (resp){
                        this.roles=resp;
                    }
                })
            }
        }
    }
</script>

<style>
    .permissManaTool .el-input{
        width: 300px;
        margin-right: 5px;
    }
    .permissManaTool{
        display: flex;
        justify-content: flex-start;
    }
    .permissManaMain{
        margin-top: 10px;
        width: 700px;
    }
</style>