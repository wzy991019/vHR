package com.wang.controller.salary;

import com.wang.bean.ResponseBean;
import com.wang.bean.Salary;
import com.wang.service.SalaryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Resource
    SalaryService salaryService;

    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.getAllSalaries();
    }

    @PostMapping("/")
    public ResponseBean addSalary(@RequestBody Salary salary){
        if (salaryService.addSalary(salary)==1){
            return ResponseBean.ok("添加成功！");
        }
        return ResponseBean.error("添加失败！");
    }

    @DeleteMapping("/{id}")
    public ResponseBean deleteSalaryById(@PathVariable Integer id){
        if (salaryService.deleteSalaryById(id)==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }

    @PutMapping("/")
    public ResponseBean updateSalaryById(@RequestBody Salary salary){
        if (salaryService.updateSalaryById(salary)==1){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }
}
