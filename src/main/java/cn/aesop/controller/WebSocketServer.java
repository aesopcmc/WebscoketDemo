package cn.aesop.controller;

import cn.aesop.domain.User;
import cn.aesop.result.MessageType;
import cn.aesop.result.ResultMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 2018年5月18日16:11:48
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocketServer为对象保存起来，一个WebSocketServer对象代表一个用户身份
     */
    private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String username="";

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
        //TODO 验证token,取出用户名，若没有登录，直接发送到前端关闭连接，注意不要回到onClose

        DecodedJWT jwt;
        try{
            jwt = JWT.decode(token);
        }catch (JWTDecodeException e) {
            log.warn("token无效", e);
            session.getAsyncRemote().sendText(ResultMessage.sendTokenError());
            return;
        }

        onlineNumber++;
        this.username = jwt.getSubject();
        this.session = session;
        log.info("现在来连接的客户id："+session.getId()+"用户名："+username);

        log.info("有新连接加入！ 当前在线人数" + onlineNumber);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            //先给所有人发送通知，说我上线了
            // Map<String,Object> map1 = new HashMap<>();
            // map1.put("messageType",1);
            // map1.put("username",username);
            // sendMessageAll(JSON.toJSONString(map1));

            //先给所有人发送通知，说我上线了
            sendMessageAll(ResultMessage.sendOnline(username));

            // //给自己发一条消息：告诉自己现在都有谁在线
            // Map<String,Object> map2 = new HashMap<>();
            // map2.put("messageType",3);
            // //移除掉自己
            // Set<String> set = clients.keySet();
            // map2.put("onlineUsers", set);
            // sendMessageTo(JSON.toJSONString(map2), username);

            // 把自己的信息加入到map当中去
            clients.put(username, this);

            //当前在线用户，排除自己
            Set<String> onlineUsers = clients.keySet();

            //给自己发一条消息：告诉自己现在都有谁在线
            sendMessageTo(ResultMessage.sendOnlineList(onlineUsers), username);
        }
        catch (IOException e){
            log.info(username+"上线的时候通知所有人发生了错误");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("服务端发生了错误"+error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        if(clients.remove(username) == null) return;

        onlineNumber--;
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            // Map<String,Object> map1 = new HashMap<>();
            // map1.put("messageType",2);
            // map1.put("onlineUsers",clients.keySet());
            // map1.put("username",username);
            // sendMessageAll(JSON.toJSONString(map1),username);
            //
            sendMessageAll(ResultMessage.sendOffline(username, clients.keySet()));
        } catch (IOException e){
            log.info(username+"下线的时候通知所有人发生了错误");
        }
        log.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息：" + message+"客户端的id是："+session.getId());
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("message");
            String fromusername = jsonObject.getString("username");
            String tousername = jsonObject.getString("to");
            //如果不是发给所有，那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            // Map<String,Object> map1 = new HashMap<>();
            // map1.put("messageType",4);
            // map1.put("textMessage",textMessage);
            // map1.put("fromusername",fromusername);
            if(tousername.equals("All")){
                // map1.put("tousername","所有人");
                // sendMessageAll(JSON.toJSONString(map1),fromusername);
                sendMessageAll(ResultMessage.sendMessageAll(username, textMessage));
            }
            else{
                // map1.put("tousername",tousername);
                // sendMessageTo(JSON.toJSONString(map1),tousername);
                sendMessageTo(ResultMessage.sendMessageOne(username,tousername, textMessage), tousername);
            }
        }
        catch (Exception e){
            log.info("发生了错误了");
        }

    }

    public void sendMessageTo(String message, String toUsername) throws IOException {
        for (WebSocketServer item : clients.values()) {
            if (item.username.equals(toUsername) ) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }


    public void sendMessageAll(String message) throws IOException {
        for (WebSocketServer item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}