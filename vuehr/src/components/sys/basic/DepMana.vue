<template>
    <div style="width: 500px">
        <el-input
                placeholder="请输入部门名称进行搜索..."
                v-model="filterText"
                prefix-icon="el-icon-search">
        </el-input>

        <el-tree
                class="filter-tree"
                :data="deps"
                :props="defaultProps"
                :expand-on-click-node="false"
                :filter-node-method="filterNode"
                ref="tree">
            <span class="custom-tree-node" style="display: flex;justify-content: space-between;width: 100%" slot-scope="{ node, data }">
                <span>{{ data.name }}</span>
                <span>
                  <el-button
                          class="depBtn"
                          type="primary"
                          size="mini"
                          @click="() => showAddDepView(data)">
                    添加部门
                  </el-button>
                  <el-button
                          class="depBtn"
                          type="danger"
                          size="mini"
                          @click="() => deleteDep(data)">
                    删除部门
                  </el-button>
                </span>
            </span>
        </el-tree>
        <el-dialog
                title="添加部门"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table>
                    <tr>
                        <td><el-tag>上级部门</el-tag></td>
                        <td>{{pname}}</td>
                    </tr>
                    <tr>
                        <td><el-tag>部门名称</el-tag></td>
                        <td><el-input v-model="dep.name" placeholder="请输入部门名称..."></el-input></td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="doAddDep">确 定</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "DepMana",
        data(){
            return{
                dialogVisible:false,
                filterText:'',
                deps:[],
                dep:{
                    name:'',
                    parentId: -1,
                },
                pname:'',
                defaultProps: {
                    children: 'children',
                    label: 'name'
                }
            }
        },
        watch: {
            filterText(val) {//监控filterText，也就是监控搜索框的属性
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDeps();
        },
        methods:{
            initDep(){
              this.dep = {
                  name:'',
                  parentId: -1,
              }
              this.pname = ''
            },
            addDep2Deps(deps,dep){
                for(let i=0;i<deps.length;i++){
                    let  d = deps[i];
                    if (d.id == dep.parentId){
                        d.children = d.children.concat(dep);//动态添加数据
                        if (d.children.length>0){
                            d.parent=true;
                        }
                        return;
                    }else {
                        this.addDep2Deps(d.children,dep)
                    }
                }
            },
            doAddDep(){
              this.postRequest('/system/basic/department/',this.dep).then(resp=>{
                  if (resp){
                      this.addDep2Deps(this.deps,resp.obj);
                      this.dialogVisible = false;
                      //初始化页面
                      this.initDep();
                  }
              })
            },
            showAddDepView(data){
                this.pname = data.name;
                this.dep.parentId = data.id;
                this.dialogVisible = true;
            },
            removeDepFromDeps(p,deps,id){
                for (let i = 0;i<deps.length;i++){
                    let d=deps[i];
                    if (d.id==id){
                        deps.splice(i,1);
                        if (deps.length == 0){
                            p.parent=false;
                        }
                        return;
                    }else {
                        this.removeDepFromDeps(d,d.children,id);
                    }
                }
            },
            deleteDep(data){
                if (data.parent){
                    this.$message.error('此部门为父部门，删除失败！')
                }else {
                    this.$confirm('此操作将永久删除【'+data.name+'】部门, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.deleteRequest('/system/basic/department/'+data.id).then(resp=>{
                            if (resp){
                                this.removeDepFromDeps(null,this.deps,data.id)
                            }
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                }
            },
            initDeps(){
                this.getRequest('/system/basic/department/').then(resp=>{
                    if (resp){
                        this.deps = resp;
                    }
                })
            },
            filterNode(value, data) {//value是你自己传进来的值，data就是得到的结果的json数组中的每一项
                if (!value) return true;//表示没有输入数据的时候保留data
                return data.name.indexOf(value) !== -1;
            }
        }
    }
</script>

<style>
.depBtn{
    padding: 2px;
}
</style>