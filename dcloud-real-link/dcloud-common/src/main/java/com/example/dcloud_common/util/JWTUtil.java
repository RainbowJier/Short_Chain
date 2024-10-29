package com.example.dcloud_common.util;

import com.example.dcloud_common.entity.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/10/5 19:59
 * @Version: 1.0
 */

@Slf4j
public class JWTUtil {

    /**
     * 主题
     */
    private static final String SUBJECT = "xdclass";

    /**
     * 秘钥
     */
    private static final String SECRET = "xdclass.net168";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "dcloud-link";

    /**
     * 过期时间，七天
     */
    private static final long EXPIRED = 1000 * 60 * 60 * 24 * 7;


    /**
     * 生成ytoken
     */
    public static String generateJsonToken(LoginUser loginUser) {
        if (loginUser == null) {
            throw new NullPointerException("用户不存在");
        }

        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("head_img", loginUser.getHeadImg())
                .claim("account_no", loginUser.getAccountNo())
                .claim("username", loginUser.getUsername())
                .claim("mail", loginUser.getMail())
                .claim("phone", loginUser.getPhone())
                .claim("auth", loginUser.getAuth())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRED))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        token = TOKEN_PREFIX + token;
        return token;
    }


    /**
     * 校验token
     */
    public static Claims checkJWT(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();

            return claims;
        }catch (Exception e){
            log.error("jwt校验异常:{}",e.getMessage());
            return null;
        }
    }



}
