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
    public static String ROOT = "http://118.89.109.106:8080/YKTJK/";//原
    //    public static String ROOT = "http://205.168.1.102:8082/";

    //用户登录
    public static String LOGINURL            = ROOT + "login";
    //用户注册
    public static String REGISTERURL         = ROOT + "register";
    //修改密码
    public static String MODIFYPSW           = ROOT + "modifyPsw";
    //首页轮播图
    public static String BANNERURL           = ROOT + "bannerList";
    //绑定项目-公司查询
    public static String PROJECT             = ROOT + "project";
    //认证信息
    public static String AUTHENTICATION      = ROOT + "identify";
    //发送验证码
    public static String SENDMSG             = ROOT + "sendMsg";
    //上传图片
    public static String UPLOADBASE64        = ROOT + "uploadBase64";
    //获取个人资料
    public static String PERSONALDATA        = ROOT + "serDetailInfo";
    //菜品展示
    public static String GOODSLIST           = ROOT + "serGoodsList";
    //菜品详情
    public static String GOODSDETAIL         = ROOT + "GoodsDetail";
    //获取车牌
    public static String GETPLATE            = ROOT + "getplate";
    //添加车牌
    public static String ADDPLATE            = ROOT + "addplate";
    //删除车牌
    public static String DELPLATE            = ROOT + "delplate";
    //更改默认车牌
    public static String CHANGEPLATE         = ROOT + "changeplate";
    //获取消息
    public static String MEETINGSEARCH       = ROOT + "meetingSearch";
    //消息详情
    public static String MEETINGDETAIL       = ROOT + "MeetingDetail";
    //获取个人绑定的项目id
    public static String PROJECTBYTEL        = ROOT + "projectByTel";
    //提交车位预约信息
    public static String PARKRESERVE         = ROOT + "parkReserve";
    //门禁数据
    public static String EGDETAIL            = ROOT + "EgDetail";
    //访客记录
    public static String FKRECORD            = ROOT + "FkRecord";
    //缴费记录
    public static String PAYRECORD           = ROOT + "payRecord";
    //邀请二维码
    public static String INVITE              = ROOT + "invite";
    //二维码扫描
    public static String QRCODE              = ROOT + "qrcode";
    //获取已入场的车位 锁定状态
    public static String GETLOCKSTATE        = ROOT + "getStatus";
    //车位锁定
    public static String LOCKPLATE           = ROOT + "lock";
    //停车收费
    public static String PARKINGRECORDSEARCH = ROOT + "parkingRecordSearch";
    //支付下单接口
    public static String UNIFIEDORDER        = ROOT + "unifiedOrder";
    //支付宝异步回调支付完成接口
    public static String Alipayurl           = "118.89.109.106:8080/YKTJK/AliPay";
    //蓝牙刷卡信息包
    public static String USERCARD            = ROOT + "usercard";
}
