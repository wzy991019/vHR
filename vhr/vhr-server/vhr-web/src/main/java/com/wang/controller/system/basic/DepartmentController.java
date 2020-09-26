package com.wang.controller.system.basic;

import com.wang.bean.Department;
import com.wang.bean.ResponseBean;
import com.wang.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Resource
    DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    public ResponseBean addDep(@RequestBody Department department){
        departmentService.addDep(department);
        if (department.getResult() == 1){
            return ResponseBean.ok("添加成功!",department);
        }
        return ResponseBean.error("添加失败！");
    }

    @DeleteMapping("/{id}")
    public ResponseBean deleteDepById(@PathVariable Integer id){
        Department department = new Department();
        department.setId(id);
        departmentService.deleteDepById(department);
        if (department.getResult()==-2){
            return ResponseBean.error("该部门下有子部门，删除失败！");
        }else if (department.getResult()==-1){
            return ResponseBean.error("该部门下有员工，删除失败！");
        }else if (department.getResult()==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }
}
