package com.poxiao.cloud.controller;

import com.poxiao.cloud.constant.Constants;
import com.poxiao.cloud.entity.AjaxResult;
import com.poxiao.cloud.entity.LoginUser;
import com.poxiao.cloud.entity.SysMenu;
import com.poxiao.cloud.entity.SysUser;
import com.poxiao.cloud.service.ISysMenuService;
import com.poxiao.cloud.service.SysLoginService;
import com.poxiao.cloud.service.SysPermissionService;
import com.poxiao.cloud.service.TokenService;
import com.poxiao.cloud.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author qq
 * @date 2021/1/28
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @Resource
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Resource
    private ISysMenuService menuService;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(String username, String password, String code, String uuid) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(username, password, code, uuid);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        // <1> 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // <2> 角色标识的集合
        Set<String> roles = permissionService.getRolePermission(user);
        // <3> 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        // <4> 返回结果
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 获得用户的 SysMenu 数组
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        // 构建路由 RouterVo 数组。可用于 Vue 构建管理后台的左边菜单
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
