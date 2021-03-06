package com.nimo.security.component;

import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimo.common.api.CommonResult;
import com.nimo.common.constants.JWTConstants;
import com.nimo.common.utils.JwtTokenUtil;
import com.nimo.security.dto.AdminUserDetails;
import com.nimo.security.mbg.model.UmsAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * @program: mall-springcloud
 * @ClassName: JwtAuthenticationTokenFilter
 * @description: JWT登录授权过滤器
 * @author: chuf
 * @create: 2022-03-14 13:54
 **/
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JWTConstants.TOKEN_HEADER);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(JWTConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            onSuccessfulAuthentication(request, response, authentication);
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            onUnsuccessfulAuthentication(request, response, new AccountExpiredException(e.getMessage()));
        }
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        LOGGER.info("=============Token 验证成功=================");
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        LOGGER.error("================token校验失败=======================");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         int scUnauthorized = HttpServletResponse.SC_UNAUTHORIZED;
        response.getWriter().write(JSONObject.toJSONString(CommonResult.error(scUnauthorized, failed.getMessage())));
    }

    /**
     * @methodName：getAuthentication
     * @description：这里从token中获取用户信息并新建一个token
     * @author：tanyp
     * @dateTime：2020/11/19 13:37
     * @Params： [tokenHeader]
     * @Return： org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     * @editNote：
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) throws ParseException, JOSEException {
        String token = tokenHeader.replace(JWTConstants.TOKEN_PREFIX, "");
        SignedJWT jwt = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(JWTConstants.SECRET);

        // 校验是否有效
        if (!jwt.verify(verifier)) {
            throw new AccountExpiredException(JWTConstants.TOKEN_INVALID);
        }

        // 校验超时
        Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
        if (new Date().after(expirationTime)) {
            throw new AccountExpiredException(JWTConstants.TOKEN_EXPIRE);
        }

        // 获取载体中的数据
        Object account = jwt.getJWTClaimsSet().getClaim("payload");
        if (account != null) {
            AdminUserDetails user = JSONObject.parseObject(account.toString(), AdminUserDetails.class);
            return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        }
        return null;
    }
}
