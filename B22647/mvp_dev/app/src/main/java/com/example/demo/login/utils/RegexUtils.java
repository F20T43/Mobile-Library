package com.example.demo.login.utils;

import com.example.demo.login.bean.UserData;

/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/3/16
 * @desc 正则表达式工具类，提供一些常用的正则表达式。
 */
public class RegexUtils {

    private RegexUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 匹配密码的正则表达式(不能以数字开头,6-15位,并且是包含数字和字母的组合)
     */
    public static final String PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";

    public static boolean checkPassword(String password){
        return password.matches(PASSWORD);
    }
    public static UserData sUser;
    public static String objectId="xxx";
    public static String username="bob";
    public static String usertype="Teacher";
    public static String useremail="123@163.com";
    public static String userpwd="123";
    public static String topic ="123";
    public static String grade="123";
    public static String cohort ="123";
}
