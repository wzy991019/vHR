package com.wang.controller;

import com.wang.bean.ChatMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;

@Controller
public class WsController {
    @Resource
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleMsg(Principal principal, ChatMsg chatMsg){
        chatMsg.setFrom(principal.getName());
        chatMsg.setDate(new Date());
        //1、发给谁。2、接收到的前端请求。3、消息对象。
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
    }
}
