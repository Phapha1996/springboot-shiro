package org.fage.springbootshiro.config;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.fage.springbootshiro.bean.Response;
import org.fage.springbootshiro.bean.ResponseEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一参数校验入口，统一异常处理
 *
 * @author Caizhf
 * @version 1.0
 * @date 2018/5/16 8:40
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    //-------------------------------shiro-------------------------------//
    /**
     * 用户被禁用
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(DisabledAccountException.class)
    @ResponseBody
    public Response handleDisabledAccountException(HttpServletRequest req, DisabledAccountException e){
        log.error("请求地址: {}， 业务异常，用户已经被禁用：{}", req.getRequestURI(), e.getMessage());
        return Response.failed(ResponseEnum.BUSI_FAILED_USER_AUTH,"用户已经被禁用");
    }

    /**
     * 用户被锁定
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(LockedAccountException.class)
    @ResponseBody
    public Response handleLockedAccountException(HttpServletRequest req, LockedAccountException e){
        log.error("请求地址: {}， 业务异常，用户已经被锁定：{}", req.getRequestURI(), e.getMessage());
        return Response.failed(ResponseEnum.BUSI_FAILED_USER_AUTH, "用户已经被锁定");
    }

    /**
     * 账号错误（账号未知）
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(UnknownAccountException.class)
    @ResponseBody
    public Response handleUnknownAccountException(HttpServletRequest req, UnknownAccountException e){
        log.error("请求地址: {}， 业务异常，账号错误：{}", req.getRequestURI(), e.getMessage());
        return Response.failed(ResponseEnum.BUSI_FAILED_USER_AUTH, "账号/密码错误");
    }

    /**
     * 密码错误
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    @ResponseBody
    public Response handleIncorrectCredentialsException(HttpServletRequest req, IncorrectCredentialsException e){
        log.error("请求地址: {}， 业务异常，密码错误：{}", req.getRequestURI(), e.getMessage());
        return Response.failed(ResponseEnum.BUSI_FAILED_USER_AUTH, "账号/密码错误");
    }

    /**
     * 登录失败次数过多
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(ExcessiveAttemptsException.class)
    @ResponseBody
    public Response handleExcessiveAttemptsException(HttpServletRequest req, ExcessiveAttemptsException e){
        log.error("请求地址: {}， 业务异常，认证失败次数过多：{}", req.getRequestURI(), e.getMessage());
        return Response.failed(ResponseEnum.BUSI_FAILED_USER_AUTH, "认证失败次数过多，请稍后再试");
    }

    /**
     * 凭证过期
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(ExpiredCredentialsException.class)
    @ResponseBody
    public Response handleExpiredCredentialsException(HttpServletRequest req, ExpiredCredentialsException e){
        log.error("请求地址: {}， 业务异常，凭证已过期：{}", req.getRequestURI(), e.getMessage());
        return Response.failed(ResponseEnum.BUSI_FAILED_USER_AUTH, "凭证已过期，请重新登录");
    }

    /**
     * 总体shiro认证错误
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Response handleAuthenticationException(HttpServletRequest req, AuthenticationException e){
        log.error("请求地址: {}， 系统异常，未知的shiro错误：{}", req.getRequestURI(), Throwables.getStackTraceAsString(e));
        return Response.failed(ResponseEnum.ERROR_UNKNOWN, "未知的shiro错误");
    }

    //-------------------------------All-------------------------------//

    /**
     * 全局统一异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(HttpServletRequest req, Exception e){
        log.error("请求地址: {}， 系统异常，未知错误：{}", req.getRequestURI(), Throwables.getStackTraceAsString(e));
        return Response.failed(ResponseEnum.ERROR_UNKNOWN);
    }
}
