package com.wang.controller.system.basic;

import com.wang.bean.Menu;
import com.wang.bean.ResponseBean;
import com.wang.bean.Role;
import com.wang.service.MenuService;
import com.wang.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {

    @Resource
    RoleService roleService;

    @Resource
    MenuService menuService;

    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @GetMapping("/mids/{rid}")
    public List<Integer> getMidsByRid(@PathVariable Integer rid){
        return menuService.getMidsByRid(rid);
    }

    @PutMapping("/")
    public ResponseBean updateMenuRole(Integer rid,Integer[] mids){
        if (menuService.updateMenuRole(rid,mids)){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }

    @PostMapping("/role")
    public ResponseBean addRole(@RequestBody Role role){
        if (roleService.addRole(role)==1){
            return ResponseBean.ok("添加成功！");
        }
        return ResponseBean.error("添加失败！");
    }

    @DeleteMapping("/role/{rid}")
    public ResponseBean deleteRoleById(@PathVariable Integer rid){
        if (roleService.deleteRoleById(rid)==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }
}
