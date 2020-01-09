package cn.aesop.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @program: test
 * @description: websocket控制器
 * @author: xingcheng
 * @create: 2019-06-01 15:13
 **/
@Log4j
@Controller
public class WebController {
    @Value("${server.port}")
    private String port;

    @Value("${server.host}")
    private String host;

    @RequestMapping("/websocket/{name}")
    public String webSocket(@PathVariable String name, Model model){
        try{
            log.info("跳转到websocket的页面上");
            model.addAttribute("username",name);
            model.addAttribute("domain", host+":"+port);
            return "websocket";
        } catch (Exception e){
            log.info("跳转到websocket的页面上发生异常，异常信息是："+e.getMessage());
            return "error";
        }
    }

}