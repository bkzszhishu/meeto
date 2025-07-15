package com.yusi.yusimeetobackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yusi.yusimeetobackend.model.dto.user.UserQueryRequest;
import com.yusi.yusimeetobackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yusi.yusimeetobackend.model.vo.LoginUserVO;
import com.yusi.yusimeetobackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 52494
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-07-02 19:43:37
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    String getEncryptPassword(String userPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     *
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 传入没有脱敏的用户信息，转换成脱敏的用户信息
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
