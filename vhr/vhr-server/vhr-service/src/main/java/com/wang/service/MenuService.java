package com.wang.service;

import com.wang.bean.Hr;
import com.wang.bean.Menu;
import com.wang.mapper.MenuMapper;
import com.wang.mapper.MenuRoleMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuService {

    @Resource
    MenuMapper menuMapper;

    @Resource
    MenuRoleMapper menuRoleMapper;

    // 获取动态菜单
    public List<Menu> getMenusByHrId() {
        return menuMapper.getMenusByHrId(((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

//    @Cacheable//可以将此查询数据放进redis
    public List<Menu> getAllMenuWithRole(){
        return menuMapper.getAllMenuWithRole();
    }

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    public List<Integer> getMidsByRid(Integer rid) {
        return menuMapper.getMidsByRid(rid);
    }

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        if (mids==null||mids.length==0){
            return true;
        }
        Integer result = menuRoleMapper.insertRecord(rid,mids);
        return result==mids.length;
    }
}
