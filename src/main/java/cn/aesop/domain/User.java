package cn.aesop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Aesop (chao_c_c@163.com)
 * @Date 2020/1/10 11:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long Id;
    String username;
    String password;
    String avatar;
}