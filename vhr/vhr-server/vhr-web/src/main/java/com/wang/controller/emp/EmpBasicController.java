package com.wang.controller.emp;

import com.wang.bean.*;
import com.wang.service.*;
import com.wang.utils.POIUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {

    @Resource
    EmployeeService employeeService;

    @Resource
    NationService nationService;

    @Resource
    PoliticsstatusService politicsstatusService;

    @Resource
    JobLevelService jobLevelService;

    @Resource
    PositionService positionService;

    @Resource
    DepartmentService departmentService;

    @GetMapping("/")
    public ResponsePageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              Employee employee,
                                              Date[] beginDateScope){
//        System.out.println(employee);
//        System.out.println(Arrays.toString(beginDateScope));
        return employeeService.getEmployeeByPage(page,size,employee,beginDateScope);
    }

    @PostMapping("/")
    public ResponseBean addEmp(@RequestBody Employee employee){
        if (employeeService.addEmp(employee)==1){
            return ResponseBean.ok("添加成功！");
        }
        return ResponseBean.error("添加失败！");
    }

    @GetMapping("/nations")
    public List<Nation> getAllNations(){
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus(){
        return politicsstatusService.getAllPoliticsstatus();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevels(){
        return jobLevelService.getAllJobLevel();
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions(){
        return positionService.getAllPositions();
    }

    @GetMapping("/maxWordID")
    public ResponseBean maxWorkID(){
        ResponseBean responseBean = ResponseBean.build().setStatus(200).setObj(String.format("%08d", employeeService.maxWorkID() + 1));
        return responseBean;
    }

    @GetMapping("/deps")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @DeleteMapping("/{id}")
    public ResponseBean deleteEmpById(@PathVariable Integer id){
        if (employeeService.deleteEmpById(id)==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }

    @PutMapping("/")
    public ResponseBean updateEmp(@RequestBody Employee employee){
        if (employeeService.updateEmp(employee)==1){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }

    //Excel导出
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(){
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null,null,null,null).getData();
        return POIUtils.employee2Excel(list);
    }

    @PostMapping("/import")
    public ResponseBean importData(MultipartFile file) throws IOException {
        List<Employee> list = POIUtils.excel2Employee(file,nationService.getAllNations(),politicsstatusService.getAllPoliticsstatus(),departmentService.getAllDepartmentsWithOutChildren(),positionService.getAllPositions(),jobLevelService.getAllJobLevel());
//        for (Employee employee : list) {
//            System.out.println(employee.toString());
//        }
        if (employeeService.addEmps(list)==list.size()){
            return ResponseBean.ok("上传成功！");
        }
        return ResponseBean.error("上传失败！");
    }
}
