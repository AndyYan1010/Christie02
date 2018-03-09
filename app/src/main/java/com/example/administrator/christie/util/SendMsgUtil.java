package com.example.administrator.christie.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class SendMsgUtil {
    //数字集合
    public static final char[] chars={'1','2','3','4','5','6','7','8','9','0'};
    //随机数
    public static Random random=new Random();
    /**
     * 随机生成6位数字验证码
     * @return
     */
    public static String getRandomString(){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<=5;i++){
            sb.append(chars[random.nextInt(chars.length)]);;
        }
        return sb.toString();
    }


    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    public static void MsgSend(String code,String mobile){

    }
}
