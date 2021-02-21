package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author xiaocai
 * @since 2021-02-06
 */
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("pageOrder/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<Order> pageOrder = new Page<>(page,limit);
        orderService.page(pageOrder,null);

        return R.ok().data("items",pageOrder.getRecords()).data("total",pageOrder.getTotal());
    }

    //创建订单
    @GetMapping("creatOrder/{courseId}")
    public R creatOrder(@PathVariable String courseId, HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId == null){
            return R.error().code(28004).message("请登录");
        }

        String orderNo = orderService.creatOrders(courseId,memberId);
        return R.ok().data("orderNo",orderNo);
    }

    //查询订单状态
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo){

        Order order = orderService.selectOrders(orderNo);
        return R.ok().data("item",order);
    }

    @GetMapping("isBuyCourse/{memberid}/{id}")
    public boolean isBuyCourse(@PathVariable String memberid,
                               @PathVariable String id) {
        //订单状态是1表示支付成功
        boolean flag = orderService.isBuyCourse(memberid, id);
        return flag;
    }

}

