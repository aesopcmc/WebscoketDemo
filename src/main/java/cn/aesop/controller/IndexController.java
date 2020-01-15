package cn.aesop.controller;


import cn.aesop.domain.User;
import cn.aesop.service.TokenService;
import cn.aesop.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: test
 * @description: websocket控制器
 * @author: xingcheng
 * @create: 2019-06-01 15:13
 **/
@Log4j
@Controller
public class IndexController {
    @Value("${server.port}")
    private String port;

    @Value("${server.host}")
    private String host;

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    /**
     * 跳转登录页
     * @return
     */
    @GetMapping({"/", "/index"})
    public String toLogin(Model model) {
        List<User> all = userService.getAll();
        model.addAttribute("users", all);
        return "login";
    }


    @PostMapping("/websocket/doLogin")
    @ResponseBody
    public Object doWsLogin(@RequestBody User user) {
        JSONObject jsonObject=new JSONObject();
        User userForBase=userService.findByUsername(user.getUsername());
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            jsonObject.put("code", -1);
            return jsonObject;
        }else {
            if (!userForBase.getPassword().equals(user.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                jsonObject.put("code", -1);
                return jsonObject;
            }else {
                //登录通过，生成token，返回
                String token = tokenService.getToken(userForBase);
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);

                jsonObject.put("code", 0);
                jsonObject.put("message","登录成功");
                jsonObject.put("data", data);
                return jsonObject;
            }
        }
    }

    @RequestMapping("/websocket")
    public String webSocket(Model model){
        model.addAttribute("domain", host+":"+port);
        return "websocket";
    }
}