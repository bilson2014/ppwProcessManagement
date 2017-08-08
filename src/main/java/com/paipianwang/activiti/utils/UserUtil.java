package com.paipianwang.activiti.utils;

import javax.servlet.http.HttpSession;

import org.activiti.engine.identity.User;

/**
 * 用户工具类
 *
 * @author HenryYan
 */
public class UserUtil {

    public static final String USER = "user";

    /**
     * 设置用户到session
     *
     * @param session
     * @param user
     */
    public static void saveUserToSession(HttpSession session, User user) {
        session.setAttribute(USER, user);
    }

    /**
     * 从Session获取当前用户信息
     *
     * @param session
     * @return
     */
    public static User getUserFromSession(HttpSession session) {
        Object attribute = session.getAttribute(USER);
        return attribute == null ? null : (User) attribute;
    }
    
    /**
     * 从Session删除当前用户信息
     * @param session
     */
    public static void removeUserSession(HttpSession session) {
    	session.removeAttribute(USER);
    }

}
