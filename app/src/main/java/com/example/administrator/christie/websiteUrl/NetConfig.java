package com.example.administrator.christie.websiteUrl;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/26 10:11
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class NetConfig {
    //服务器总地址
    public static String ROOT           = "http://118.89.109.106:8080/YKTJK/";
    //用户登录
    public static String LOGINURL       = ROOT + "login";
    //用户注册
    public static String REGISTERURL    = ROOT + "register";
    //绑定项目-公司查询
    public static String PROJECT        = ROOT + "project";
    //认证信息
    public static String AUTHENTICATION = ROOT + "register1";
    //发送验证码
    public static String SENDMSG        = ROOT + "sendMsg";
    //上传图片
    public static String UPLOADBASE64   = ROOT + "uploadBase64";
    //获取个人资料
    public static String PERSONALDATA   = ROOT + "detail";
}
