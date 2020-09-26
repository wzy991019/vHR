package com.wang.controller.system;

import com.wang.bean.Hr;
import com.wang.bean.ResponseBean;
import com.wang.bean.Role;
import com.wang.service.HrService;
import com.wang.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/hr")
public class HrController {

    @Resource
    HrService hrService;

    @Resource
    RoleService roleService;

    @GetMapping("/")
    public List<Hr> getAllHrs(String keywords){
        return hrService.getAllHrs(keywords);
    }

    @PutMapping("/")
    public ResponseBean updateHr(@RequestBody Hr hr){
        if (hrService.updateHr(hr)==1){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public ResponseBean updateHrRole(Integer hrid,Integer[] rids){
        if (hrService.updateHrRole(hrid,rids)){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }

    @DeleteMapping("/{id}")
    public ResponseBean deleteHrById(@PathVariable Integer id){
        if (hrService.deleteHrById(id) ==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }
}
