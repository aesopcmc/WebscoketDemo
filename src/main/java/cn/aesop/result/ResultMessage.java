package cn.aesop.result;

import cn.aesop.domain.User;
import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * @author Aesop(chao_c_c @ 163.com)
 * @date 2020/1/14 10:10
 */
@Getter
public class ResultMessage {
    private MessageType messageType;
    private String textMessage;
    private String fromUsername;
    private String toUsername;
    private Set<String> onlineUsers;

    private ResultMessage(MessageType messageType, String textMessage, String fromUsername, String toUsername, Set<String> onlineUsers) {
        this.messageType = messageType;
        this.textMessage = textMessage;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.onlineUsers = onlineUsers;
    }

    public static String sendOnline(String fromUsername) {
        return new ResultMessage(MessageType.ONLINE, null, fromUsername, null, null).toJsonString();
    }

    public static String sendOffline(String fromUsername, Set<String> onlineUsers) {
        return new ResultMessage(MessageType.OFFLINE, null, fromUsername, null, onlineUsers).toJsonString();
    }

    public static String sendOnlineList(Set<String> onlineUsers) {
        return new ResultMessage(MessageType.ONLINE_LIST, null, null, null, onlineUsers).toJsonString();
    }

    public static String sendMessageOne(String fromUsername, String toUsername, String message) {
        return new ResultMessage(MessageType.MESSAGE, message, fromUsername, toUsername, null).toJsonString();
    }

    public static String sendMessageAll(String fromUsername, String message) {
        return new ResultMessage(MessageType.MESSAGE, message, fromUsername, "所有人", null).toJsonString();
    }

    public static String sendTokenError() {
        return new ResultMessage(MessageType.ERROR_TOKEN_INVALID, MessageType.ERROR_TOKEN_INVALID.getName(), null, null, null).toJsonString();
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
