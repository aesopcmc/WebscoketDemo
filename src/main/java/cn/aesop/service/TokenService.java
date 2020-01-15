package cn.aesop.service;

import cn.aesop.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Aesop(chao_c_c @ 163.com)
 * @date 2020/1/10 11:48
 */
@Service("TokenService")
public class TokenService {
    public String getToken(User user) {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date expiresTime = null;//失效时间
        // try {
        //     expiresTime = sdf.parse("2020-01-15 10:50:00");
        // } catch (ParseException e) {
        //     e.printStackTrace();
        //     return null;
        // }

        String token = JWT.create()
                .withAudience(user.getId().toString())// 将 user id   保存到 token 里面
                .withSubject(user.getUsername())// 将 username保存到token里面
                // .withExpiresAt(expiresTime)//设置失效时间
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
