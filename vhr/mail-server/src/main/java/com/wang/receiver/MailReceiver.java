package com.wang.receiver;

import com.wang.bean.Employee;
import com.wang.bean.Hr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MailReceiver {

    public static final Logger logger = LoggerFactory.getLogger(MailReceiver.class);

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    MailProperties mailProperties;

    @Resource
    TemplateEngine templateEngine;

    @RabbitListener(queues = "wang.mail.welcome")
    public void handler(Employee employee){
        logger.info("employee:"+employee.toString());
        //收到消息，发送邮件
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(employee.getEmail());//设置接受邮箱
            helper.setFrom(mailProperties.getUsername());//设置发送人
            helper.setSubject("入职欢迎");//设置主题
            helper.setSentDate(new Date());//设置发送日期
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJobLevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("邮件发送失败！"+e.getMessage());
        }
    }
}
