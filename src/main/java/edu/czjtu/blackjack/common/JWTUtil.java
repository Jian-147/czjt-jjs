package edu.czjtu.blackjack.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

//验证用户登录功能
@Component
public class JWTUtil {
    // token过期时间 7天
    static public int expiresTime = 1000 * 60 * 60 * 24 * 7;

    private static RedisTemplate<String, String> redisTemplate;

    private static String redisKeyPrefix = "TOKEN:";

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        JWTUtil.redisTemplate = redisTemplate;
    }

    // 密钥
    static public String secret = "czjtu";

    public static String generateToken(Map<String, Object> claims) {
        String token = JWT.create()
                .withClaim("Claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresTime))
                .sign(Algorithm.HMAC256(secret));
        redisTemplate.opsForValue().set(redisKeyPrefix + token, "true");
        return token;
    }

    //用于验证JWT的有效性并解析出其中的用户信息
    public static Map<String, Object> parseToken(String token) throws Exception {
        String redisToken = redisTemplate.opsForValue().get(redisKeyPrefix + token);
        if (redisToken == null) {
            throw new Exception("用户未登录。");
        }
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("Claims")
                .asMap();
    }

    //从Redis中删除指定的token，用于用户登出时调用，使该token失效
    public static void deleteToken(String token) {
        redisTemplate.delete(redisKeyPrefix + token);
    }
}
