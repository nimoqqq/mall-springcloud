package com.nimo.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jwt.SignedJWT;
import com.nimo.common.api.CommonResult;
import com.nimo.common.constants.JWTConstants;
import com.nimo.common.constants.RedisConstants;
import com.nimo.common.enums.AuthEnum;
import com.nimo.common.utils.JwtTokenUtil;
import com.nimo.common.utils.Md5Utils;
import com.nimo.common.vo.Authority;
import com.nimo.common.vo.UmsAdmin;
import com.nimo.gateway.config.ExclusionUrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;

/**
 * @program: mall-springcloud
 * @ClassName: AuthGlobalFilter
 * @description:
 * @author: chuf
 * @create: 2022-03-21 13:38
 **/
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private ExclusionUrlConfig exclusionUrlConfig;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String headerToken = request.getHeaders().getFirst(JWTConstants.TOKEN_HEADER);
        log.info("headerToken:{}", headerToken);

        // ???????????????token??? ???????????????Token????????????
        if (!ObjectUtils.isEmpty(headerToken) && !JwtTokenUtil.verifierToken(headerToken)) {
            return getVoidMono(response, AuthEnum.AUTH_NO_TOKEN.getKey(), AuthEnum.AUTH_NO_TOKEN.getValue());
        }
        String path = request.getURI().getPath();
        log.info("request path:{}", path);

        // ????????????????????????????????? ??????????????????
        if (isExclusionUrl(path)) {
            return chain.filter(exchange);
        }

        // ???????????????URL???????????????
        boolean permission = hasPermission(headerToken, path);
        if (!permission) {
            return getVoidMono(response, AuthEnum.AUTH_NO_ACCESS.getKey(), AuthEnum.AUTH_NO_ACCESS.getValue());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> getVoidMono(ServerHttpResponse response, Integer i, String msg) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        byte[] bits = JSON.toJSONString(CommonResult.error(i, msg)).getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        return response.writeWith(Mono.just(buffer));
    }

    private boolean isExclusionUrl(String path) {
        List<String> exclusions = exclusionUrlConfig.getUrl();
        if (exclusions.size() == 0) {
            return false;
        }
        return exclusions.stream().anyMatch(action -> antPathMatcher.match(action, path));
    }

    /**
     * @methodName???hasPermission
     * @description??????????????????URL???????????????
     * @author???tanyp
     * @dateTime???2020/11/24 9:38
     * @Params??? [headerToken, path]
     * @Return??? boolean
     * @editNote???
     */
    private boolean hasPermission(String headerToken, String path) {
        try {
            if (ObjectUtils.isEmpty(headerToken)) {
                return false;
            }

            SignedJWT jwt = JwtTokenUtil.getSignedJWT(headerToken);
            Object payload = jwt.getJWTClaimsSet().getClaim("payload");
            UmsAdmin user = JSON.parseObject(payload.toString(), UmsAdmin.class);
            // ??????Key??? ??????????????????redis???
            String keyPrefix = RedisConstants.TOKEN_KEY_PREFIX + user.getUsername() + ":";
            String token = headerToken.replace(JWTConstants.TOKEN_PREFIX, "");
            String keySuffix = Md5Utils.getMD5(token.getBytes());
            String key = keyPrefix + keySuffix;
            String authKey = key + RedisConstants.AUTH_KEY;

            String authStr = redisTemplate.opsForValue().get(authKey);
            if (StringUtils.isEmpty(authStr)) {
                return false;
            }

            List<Authority> authorities = JSON.parseArray(authStr, Authority.class);
            return authorities.stream().anyMatch(authority -> antPathMatcher.match(authority.getAuthority(), path));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
