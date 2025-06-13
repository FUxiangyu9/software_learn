package com.ctsw.recruit.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public class  JwtUtil {
    //  加密公钥
    private static final String KEY = "114514.会员制software.recruit";
	
	//接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        String uid = claims.get("uid").toString();
        claims.put("uid", uid);
        return  JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .sign(Algorithm.HMAC256(KEY));
    }

	//接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        Map<String, Object> claims = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
        BigInteger uid = new BigInteger(claims.get("uid").toString());
        claims.put("uid", uid);
        return claims;
    }

}
