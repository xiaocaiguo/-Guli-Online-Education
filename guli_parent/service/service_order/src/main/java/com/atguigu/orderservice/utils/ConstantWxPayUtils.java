package com.atguigu.orderservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantWxPayUtils implements InitializingBean {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerKey;

    @Value("${notifyurl}")
    private String notifyUrl;

    public static String ACCESS_APPID;
    public static String ACCESS_PARTNER;
    public static String ACCESS_PARTNER_KYE;
    public static String ACCESS_NOTIFY_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_APPID = appid;
        ACCESS_PARTNER = partner;
        ACCESS_PARTNER_KYE = partnerKey;
        ACCESS_NOTIFY_URL = notifyUrl;
    }
}
