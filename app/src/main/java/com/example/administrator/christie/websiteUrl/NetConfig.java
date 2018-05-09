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
    //    public static String ROOT           = "http://118.89.109.106:8080/YKTJK/";
    public static String ROOT           = "http://205.168.1.113:8082/";
    //用户登录
    public static String LOGINURL       = ROOT + "login";
    //用户注册
    public static String REGISTERURL    = ROOT + "register";
    //修改密码
    public static String MODIFYPSW      = ROOT + "modifyPsw";
    //绑定项目-公司查询
    public static String PROJECT        = ROOT + "project";
    //认证信息
    public static String AUTHENTICATION = ROOT + "identify";
    //发送验证码
    public static String SENDMSG        = ROOT + "sendMsg";
    //上传图片
    public static String UPLOADBASE64   = ROOT + "uploadBase64";
    //获取个人资料
    public static String PERSONALDATA   = ROOT + "serDetailInfo";
    //菜品展示
    public static String GOODSLIST      = ROOT + "serGoodsList";
    //获取车牌
    public static String GETPLATE       = ROOT + "getplate";
    //添加车牌
    public static String ADDPLATE       = ROOT + "addplate";
    //删除车牌
    public static String DELPLATE       = ROOT + "delplate";
    //更改默认车牌
    public static String CHANGEPLATE    = ROOT + "changeplate";
    //获取消息
    public static String MEETINGSEARCH  = ROOT + "meetingSearch";
    //获取个人绑定的项目id
    public static String PROJECTBYTEL   = ROOT + "projectByTel";
    //提交车位预约信息
    public static String PARKRESERVE    = ROOT + "parkReserve";
}
