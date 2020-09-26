package com.wang.exception;

import com.wang.bean.ResponseBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理全局异常
    @ExceptionHandler(SQLException.class)
    public ResponseBean sqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){//处理有外键字段数据的删除的异常
            return ResponseBean.error("该数据有关联数据，操作失败！");
        }
        return ResponseBean.error("数据库异常，操作失败！");
    }
}
