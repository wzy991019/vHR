package com.wang.controller.salary;

import com.wang.bean.Employee;
import com.wang.bean.ResponseBean;
import com.wang.bean.ResponsePageBean;
import com.wang.bean.Salary;
import com.wang.service.EmployeeService;
import com.wang.service.SalaryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/salary/sobcfg")
public class SobConfigController {

    @Resource
    EmployeeService employeeService;

    @Resource
    SalaryService salaryService;

    @GetMapping("/")
    public ResponsePageBean getEmployeeByPageWithSalary(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeByPageWithSalary(page,size);
    }

    @GetMapping("/salaries")
    public List<Salary> getAllSalaries(){
        return salaryService.getAllSalaries();
    }

    @PutMapping("/")
    public ResponseBean updateEmployeeSalaryById(Integer eid,Integer sid){
        Integer result = employeeService.updateEmployeeSalaryById(eid, sid);
        if (result ==1 || result==2){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }
}
