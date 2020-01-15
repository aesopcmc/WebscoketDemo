package cn.aesop.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * messageType后天发送的消息类型 ：1代表上线 2代表下线 3代表在线名单 4代表普通消息 5.token无效
 * @author Aesop(chao_c_c @ 163.com)
 * @date 2020/1/14 10:11
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    ONLINE(1, "上线"),
    OFFLINE(2, "下线"),
    ONLINE_LIST(3, "在线名单"),
    MESSAGE(4, "普通消息"),
    ERROR_TOKEN_INVALID(5, "TOKEN无效");

    private Integer index;
    private String name;
}
