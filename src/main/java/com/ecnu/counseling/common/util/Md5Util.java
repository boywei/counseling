package com.ecnu.counseling.common.util;

import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 9:43 下午
 */
public class Md5Util {

    public static void main(String[] args) {
        System.out.println(encryptPassword("wuhongbin", "12345"));
        System.out.println(encryptPassword("wuhongbin", "12345"));
        System.out.println(encryptPassword("wuhongbin", "12345"));
        System.out.println(encryptPasswordPlus("wuhongbin", "12345", "  "));
        System.out.println(encryptPassword("wuhongbin", "12346"));
    }

    /**
     * MD5简易加密
     *
     * @param account 登陆唯一标识
     * @param password 密码
     * @return
     */
    public static String encryptPassword(String account, String password) {
        return encryptPasswordPlus(account, password, Strings.EMPTY);
    }

    /**
     * MD5密码加密；加盐
     *
     * @param account
     * @param password
     * @param salt
     * @return
     */
    public static String encryptPasswordPlus(String account, String password, String salt) {
        return new Md5Hash(account + password + salt).toHex();
    }
}
