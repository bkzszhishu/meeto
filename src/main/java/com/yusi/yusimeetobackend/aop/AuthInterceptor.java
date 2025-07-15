package com.yusi.yusimeetobackend.aop;

import com.yusi.yusimeetobackend.annotation.AuthCheck;
import com.yusi.yusimeetobackend.exception.BusinessException;
import com.yusi.yusimeetobackend.exception.ErrorCode;
import com.yusi.yusimeetobackend.model.entity.User;
import com.yusi.yusimeetobackend.model.enums.UserRoleEnum;
import com.yusi.yusimeetobackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/14 21:50
 * @Description:
 */
@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     * @param joinPoint 切点表达式
     * @param authCheck 可以获取 authCheck 中的属性
     * @return
     */
    @Around("@annotation(authCheck)") //规定带有 authCheck 注解的方法都要执行这个切面方法
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //如果在某一个方法上指定该注解，可以通过此方法获取该注解的值
        String mustRole = authCheck.mustRole();
        //获取当前登录用户信息
        RequestAttributes currentRequestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) currentRequestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);
        //获取当前登录用户的角色
        String userRole = loginUser.getUserRole();
        //获取规定角色的枚举类
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //获取当前用户角色的枚举类
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(userRole);
        //如果注解里没有指定需要角色，则直接通过
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        //当前用户没有权限，返回异常
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //如果当前注解指定是管理员权限，但是当前用户不是管理员权限，抛出异常
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //放行
        return joinPoint.proceed();
    }
}
