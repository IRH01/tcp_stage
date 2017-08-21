package com.irh.core.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtUtils
 *
 * @author sunrh
 */
public final class JwtUtil {

    private static final String DEFAULT_SECRET = "fdiDLE_31kdio3.23nd92kzLIjhemD";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private JwtUtil() {

    }

    /**
     * 获取密文，加入超时
     *
     * @param context 原文
     * @param secret  密码
     * @param min     超时时间（单位：分）
     * @return
     */
    public static String genJwtTextExpiration(String context, String secret, Integer min) {

        Map<String, Object> head = new HashMap<>();
        head.put("typ", "JWT");

        Key key = new SecretKeySpec(secret.getBytes(), SIGNATURE_ALGORITHM.getValue());
        JwtBuilder jwtBuilder = Jwts.builder().setHeader(head).setSubject(context);
        if (min != 0) {
            Date date = new Date(System.currentTimeMillis() + 1000 * 60 * min);
            jwtBuilder = jwtBuilder.setExpiration(date);
        }
        return jwtBuilder.signWith(SIGNATURE_ALGORITHM, key).compact();
    }

    /**
     * 获取Jwt密文
     *
     * @param context 原文
     * @param secret  密码
     * @return
     */
    public static String genJwtText(String context, String secret) {
        return genJwtTextExpiration(context, secret, 0);
    }

    /**
     * 从密文中解析出原文
     *
     * @param encode 密文
     * @param secret 密码
     * @return
     */
    public static String resolveJwtText(String encode, String secret) throws Exception {
        Key key = new SecretKeySpec(secret.getBytes(), SIGNATURE_ALGORITHM.getValue());
        return Jwts.parser().setSigningKey(key).parseClaimsJws(encode).getBody().getSubject();
    }

}
