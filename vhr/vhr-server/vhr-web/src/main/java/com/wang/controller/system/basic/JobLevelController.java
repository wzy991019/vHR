package com.wang.controller.system.basic;

import com.wang.bean.JobLevel;
import com.wang.bean.ResponseBean;
import com.wang.service.JobLevelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/basic/joblevel")
public class JobLevelController {

    @Resource
    JobLevelService jobLevelService;

    @GetMapping("/")
    public List<JobLevel> getAllJobLevel(){
        return jobLevelService.getAllJobLevel();
    }

    @PostMapping("/")
    public ResponseBean addJobLevel(@RequestBody JobLevel jobLevel){
        if (jobLevelService.addJobLevel(jobLevel)==1){
            return ResponseBean.ok("添加成功！");
        }
        return ResponseBean.error("添加失败！");
    }

    @PutMapping("/")
    public ResponseBean updateJobLevel(@RequestBody JobLevel jobLevel){
        if (jobLevelService.updateJobLevel(jobLevel)==1){
            return ResponseBean.ok("修改成功！");
        }
        return ResponseBean.error("修改失败！");
    }

    @DeleteMapping("/{id}")
    public ResponseBean deleteJobLevelById(@PathVariable Integer id){
        if (jobLevelService.deleteJobLevelById(id)==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }

    @DeleteMapping("/")
    public ResponseBean deleteJobLevelsByIds(Integer[] ids){
        if (jobLevelService.deleteJobLevelsByIds(ids)==ids.length){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }
}
