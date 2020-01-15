package cn.aesop.service;

import cn.aesop.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aesop(chao_c_c @ 163.com)
 * @date 2020/1/10 11:49
 */
@Service
public class UserService {
    private static final Map<Long, User> USER_MAP = new HashMap<>();
    static {
        //模拟3个用户数据
        USER_MAP.put(111L, new User(111L, "大卫", "123456", null));
        USER_MAP.put(222L, new User(222L, "汤姆", "123456", null));
        USER_MAP.put(333L, new User(333L, "杰瑞", "123456", null));
    }

    public List<User> getAll() {
        return new ArrayList<>(USER_MAP.values());
    }

    /**
     * 根据ID查找用户
     * @param id
     * @return
     */
    public User findById(Long id) {
        return USER_MAP.get(id);
    }

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        for (User u : USER_MAP.values()) {
            if(u.getUsername().equals(username)) {
                return u;
            }
        }

        return null;
    }
}
