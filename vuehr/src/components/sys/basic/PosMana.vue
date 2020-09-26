<template>
    <div>
        <el-input size="small" class="addPosInput" v-model="pos.name" placeholder="添加职位..." @keydown.enter.native="addPosition" prefix-icon="el-icon-plus"></el-input>
        <el-button icon="el-icon-plus" size="small" type="primary" @click="addPosition" >添加</el-button>
        <div class="posManaMain">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在加载.."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="positions" border stripe style="width: 80%" size="small" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column prop="id" label="编号" width="55">
                </el-table-column>
                <el-table-column prop="name" label="职位名称" width="150">
                </el-table-column>
                <el-table-column prop="createDate" label="创建时间" width="300">
                </el-table-column>
                <el-table-column label="是否启用" width="80">
                    <template slot-scope="scope">
                        <el-tag size="small" type="success" v-if="scope.row.enabled">已启用</el-tag>
                        <el-tag size="small" type="danger" v-else>未启用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="showEditView(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button @click="deleteMany" type="danger" size="small" style="margin-top: 10px" :disabled="multipleSelection.length==0">批量删除</el-button>
        </div>
        <el-dialog title="修改职位" :visible.sync="dialogVisible" width="30%">
            <div>
                <div>
                    <el-tag>职位名称</el-tag>
                    <el-input class="updatePosInput" size="small" v-model="updatePos.name"></el-input>
                </div>
                <div>
                    <el-tag>是否启用</el-tag>
                    <el-switch class="updatePosInput" v-model="updatePos.enabled" active-text="启用" inactive-text="禁用"></el-switch>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" @click="dialogVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PosMana",
        data(){
            return {
                loading:false,
                pos:{
                    name:''
                },
                positions: [],
                dialogVisible: false,
                updatePos:{
                    name:'',
                    enabled:''
                },
                multipleSelection:[]
            }
        },
        mounted() {
            this.initPositions();//在页面初始化时候进行方法的调用
        },
        methods:{
            deleteMany(){
                this.$confirm('此操作将永久删除【'+this.multipleSelection.length+'】条记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '?';
                    this.multipleSelection.forEach(item=>{
                        ids+='ids='+item.id+'&';//进行数组的拼接（要删除的id）
                    })
                    this.deleteRequest('/system/basic/pos/'+ids).then(resp=>{
                        if (resp){
                            this.initPositions();//刷新表格
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleSelectionChange(val){
              this.multipleSelection = val;
            },
            doUpdate(){
              this.putRequest('/system/basic/pos/',this.updatePos).then(resp=>{
                  if (resp){
                      this.initPositions();
                      this.updatePos.name='';
                      this.dialogVisible = false;
                  }
              })
            },
            addPosition(){
              //判断用户是否输入了值
                if (this.pos.name){
                    this.postRequest('/system/basic/pos/',this.pos).then(resp=>{//对象上传，不是只上传一个name属性
                        if (resp){
                            this.initPositions();//刷新表格
                            // this.pos.name='';
                        }
                    })
                }else {
                    this.$message.error("职位名称不可以为空！！")
                }
            },
            showEditView(index,data){
                this.dialogVisible = true;
                Object.assign(this.updatePos,data);//做属性的拷贝
            },
            handleDelete(index,data){
                this.$confirm('此操作将永久删除【'+data.name+'】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/pos/'+data.id).then(resp=>{
                        if (resp){
                            this.initPositions();//刷新表格
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            initPositions(){
                this.loading = true;
                this.getRequest("/system/basic/pos/").then(resp=>{
                    this.loading = false;
                    if (resp){
                        this.positions = resp;
                    }
                })
            }
        }
    }
</script>

<style>
.addPosInput{
    width: 300px;
    margin-right: 10px;
}
    .posManaMain{
        margin-top: 10px;
    }
    .updatePosInput{
        width: 250px;
        margin-left: 10px;
    }
</style>