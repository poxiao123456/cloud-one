package com.poxiao.cloud.handler;

import com.alibaba.fastjson.JSON;
import com.poxiao.cloud.constant.Constants;
import com.poxiao.cloud.constant.HttpStatus;
import com.poxiao.cloud.entity.AjaxResult;
import com.poxiao.cloud.entity.LoginUser;
import com.poxiao.cloud.manager.AsyncFactory;
import com.poxiao.cloud.manager.AsyncManager;
import com.poxiao.cloud.service.TokenService;
import com.poxiao.cloud.util.ServletUtils;
import com.poxiao.cloud.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qq
 * @date 2021/1/27
 * 自定义退出处理类 返回成功
 * 主动删除 LoginUser 在 Redis 中的缓存
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // <1> 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 如果有登陆的情况下
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // <2> 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // <3> 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        // <4> 响应退出成功
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }

}
