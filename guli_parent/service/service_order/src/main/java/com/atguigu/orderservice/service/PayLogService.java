package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author xiaocai
 * @since 2021-02-06
 */
public interface PayLogService extends IService<PayLog> {

    Map creatcreateNative(String orderNo);

    Map<String ,String> queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);
}
