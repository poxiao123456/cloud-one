package com.poxiao.cloud.service;

import com.poxiao.cloud.constant.Constants;
import com.poxiao.cloud.entity.LoginUser;
import com.poxiao.cloud.exception.CaptchaException;
import com.poxiao.cloud.exception.CaptchaExpireException;
import com.poxiao.cloud.exception.CustomException;
import com.poxiao.cloud.exception.UserPasswordNotMatchException;
import com.poxiao.cloud.manager.AsyncFactory;
import com.poxiao.cloud.manager.AsyncManager;
import com.poxiao.cloud.redis.RedisCache;
import com.poxiao.cloud.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qq
 * @date 2021/1/28
 */
@Service
public class SysLoginService {


    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // <1> 验证图片验证码的正确性
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid; // uuid 的作用，是获得对应的图片验证码
        String captcha = redisCache.getCacheObject(verifyKey); // 从 Redis 中，获得图片验证码
        redisCache.deleteObject(verifyKey); // 从 Redis 中，删除图片验证码
        if (captcha == null) { // 图片验证码不存在
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) { // 图片验证码不正确
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaException();
        }
        // <2> 用户验证
        Authentication authentication;
        try {
            // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            // <2.1> 发生异常，说明验证不通过，记录相应的登陆失败日志
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        // <2.2> 验证通过，记录相应的登陆成功日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        // <3> 生成 Token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }
}
