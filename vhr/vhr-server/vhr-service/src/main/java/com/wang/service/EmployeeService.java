package com.wang.service;

import com.wang.bean.Employee;
import com.wang.bean.ResponseBean;
import com.wang.bean.ResponsePageBean;
import com.wang.mapper.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {

    @Resource
    EmployeeMapper employeeMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    public final static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public ResponsePageBean getEmployeeByPage(Integer page, Integer size,Employee employee,Date[] beginDateScope) {
        if (page!=null && size!=null){
            page = ( page-1 ) * size;
        }
        List<Employee> data = employeeMapper.getEmployeeByPage(page,size,employee,beginDateScope);
        Long total = employeeMapper.getTotal(employee,beginDateScope);
        ResponsePageBean bean = new ResponsePageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        double month = (Double.parseDouble(yearFormat.format(endContract))-Double.parseDouble(yearFormat.format(beginContract)))*12
                +(Double.parseDouble(monthFormat.format(endContract))-Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(month/12)));
        int result = employeeMapper.insertSelective(employee);
        //由于ID为自增，则此时需要主键回填才能获得添加之后的ID   --->useGeneratedKeys="true" keyProperty="id"在xml里加上
        if (result==1){
            Employee emp = employeeMapper.getEmployeeById(employee.getId());
            logger.info("emp:"+emp.toString());
            rabbitTemplate.convertAndSend("wang.mail.welcome",emp);
        }
        return result;
    }

    public Integer maxWorkID() {
        return employeeMapper.maxWorkID();
    }

    public Integer deleteEmpById(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmp(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public Integer addEmps(List<Employee> list) {
        return employeeMapper.addEmps(list);
    }

    public ResponsePageBean getEmployeeByPageWithSalary(Integer page, Integer size) {
        if (page!=null && size!=null){
            page=(page-1)*size;
        }
        List<Employee> list = employeeMapper.getEmployeeByPageWithSalary(page,size);
        ResponsePageBean responsePageBean = new ResponsePageBean();
        responsePageBean.setData(list);
        responsePageBean.setTotal(employeeMapper.getTotal(null,null));
        return responsePageBean;
    }

    public Integer updateEmployeeSalaryById(Integer eid, Integer sid) {
        return employeeMapper.updateEmployeeSalaryById(eid,sid);
    }
}
