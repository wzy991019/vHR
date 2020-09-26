package com.wang.controller.system.basic;

import com.wang.bean.Position;
import com.wang.bean.ResponseBean;
import com.wang.service.PositionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Resource
    PositionService positionService;

    @GetMapping("/")
    public List<Position> getAllPositions(){
        return positionService.getAllPositions();
    }

    @PostMapping("/")
    public ResponseBean addPosition(@RequestBody Position position){
        if (positionService.addPosition(position)==1){
            return ResponseBean.ok("添加成功！");
        }
        return ResponseBean.error("添加失败！");
    }

    @PutMapping("/")
    public ResponseBean updatePositions(@RequestBody Position position){
        if (positionService.updatePositions(position)==1){
            return ResponseBean.ok("更新成功！");
        }
        return ResponseBean.error("更新失败！");
    }

    @DeleteMapping("/{id}")
    public ResponseBean deletePositionById(@PathVariable("id") Integer id){
        if (positionService.deletePositionById(id)==1){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！此职位还有人！！");
    }

    //批量删除
    @DeleteMapping("/")
    public ResponseBean deletePositionByIds(Integer[] ids){
        if (positionService.deletePositionByIds(ids)==ids.length){
            return ResponseBean.ok("删除成功！");
        }
        return ResponseBean.error("删除失败！");
    }
}
