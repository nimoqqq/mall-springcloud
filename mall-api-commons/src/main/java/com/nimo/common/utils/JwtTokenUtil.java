package com.nimo.common.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimo.common.constants.JWTConstants;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * JwtToken生成的工具类
 * Created by macro on 2018/4/26.
 */
@Slf4j
public class JwtTokenUtil {
    /**
     * @methodName：createToken
     * @description：创建token
     * @author：tanyp
     * @dateTime：2020/11/24 10:20
     * @Params： [user]
     * @Return： java.lang.String
     * @editNote：
     */
    public static String createToken(String payload) {
        // 创建密钥
        MACSigner macSigner = null;
        try {
            macSigner = new MACSigner(JWTConstants.SECRET);
        } catch (KeyLengthException e) {
            log.error("生成 token 异常", e);
        }

        // payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("subject")
                .claim("payload", payload)
                .expirationTime(new Date(System.currentTimeMillis() + JWTConstants.EXPIRE_TIME))
                .build();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        // 创建签名的JWT
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
        try {
            signedJWT.sign(macSigner);
        } catch (JOSEException e) {
            log.error("生成 token 异常", e);
        }

        // 生成token
        String jwtToken = signedJWT.serialize();
        return jwtToken;
    }

    /**
     * @methodName：verifierToken
     * @description：验证token
     * @author：tanyp
     * @dateTime：2020/11/24 9:35
     * @Params： [headerToken]
     * @Return： boolean
     * @editNote：
     */
    public static boolean verifierToken(String headerToken) {
        try {
            SignedJWT jwt = getSignedJWT(headerToken);
            JWSVerifier verifier = new MACVerifier(JWTConstants.SECRET);
            // 校验是否有效
            if (!jwt.verify(verifier)) {
                log.error("token不合法，检测不过关");
                return false;
            }

            // 校验超时
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            if (new Date().after(expirationTime)) {
                log.error("token已经过期");
                return false;
            }
            // 获取载体中的数据
            return true;
        } catch (ParseException | JOSEException e) {
            log.error("token校验出错", e);
        }
        return false;
    }

    public static SignedJWT getSignedJWT(String headerToken) throws ParseException {
        String token = headerToken.replace(JWTConstants.TOKEN_PREFIX, "");
        log.info("token is {}", token);
        return SignedJWT.parse(token);
    }
}
