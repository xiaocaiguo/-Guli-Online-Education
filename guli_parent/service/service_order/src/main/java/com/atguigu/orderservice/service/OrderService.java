package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author xiaocai
 * @since 2021-02-06
 */
public interface OrderService extends IService<Order> {

    String creatOrders(String courseId, String memberId);

    Order selectOrders(String orderNo);

    boolean isBuyCourse(String memberid, String id);
}
