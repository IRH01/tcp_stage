package com.irh.core.util;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Random;

public class CaptchaUtil {
    private static Logger log = Logger.getLogger(CaptchaUtil.class);
    public static final String SMS_URL = "app.cloopen.com";
    public static final String SMS_PORT = "8883";
    public static final String SMS_SID = "8a216da8555d110e015571c838531a35";
    public static final String SMS_TOKEN = "ff3ba0b9a88245a4bdf17def184c7ef7";
    public static final String SMS_APPID = "8a216da8555d110e015571c838be1a3b";

    private static final int DEFAULT_CAPTCHA_LENGTH = 6;

    private static String getRadomNumber(int count) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static String getRandomCaptcha() {
        return getRadomNumber(DEFAULT_CAPTCHA_LENGTH);
    }

    //发送信息
    public static void sendSMS(String param, String phone, String template) throws Exception {
        sendSms(phone, template, param);
    }

    public static void sendSms(String phone, String templateId, String... params) {
        HashMap<String, Object> result = null;

        //初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

        //******************************注释*********************************************
        //*初始化服务器地址和端口                                                       *
        //*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
        //*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
        //*******************************************************************************
        restAPI.init(SMS_URL, SMS_PORT);

        //******************************注释*********************************************
        //*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
        //*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
        //*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
        //*******************************************************************************
        restAPI.setAccount(SMS_SID, SMS_TOKEN);


        //******************************注释*********************************************
        //*初始化应用ID                                                                 *
        //*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
        //*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
        //*******************************************************************************
        restAPI.setAppId(SMS_APPID);


        //******************************注释****************************************************************
        //*调用发送模板短信的接口发送短信                                                                  *
        //*参数顺序说明：                                                                                  *
        //*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
        //*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
        //*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
        //*第三个参数是要替换的内容数组。																														       *
        //**************************************************************************************************

        //**************************************举例说明***********************************************************************
        //*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
        //*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
        //*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
        //*********************************************************************************************************************
        result = restAPI.sendTemplateSMS(phone, templateId, params);

        log.info("SDKTestGetSubAccounts result=" + result);
        if (!"000000".equals(result.get("statusCode"))) {
            //异常返回输出错误码和错误信息
            log.error("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }
    }

}
