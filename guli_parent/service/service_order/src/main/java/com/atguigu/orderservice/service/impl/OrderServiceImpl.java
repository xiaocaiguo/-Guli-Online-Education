package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.vo.CourseWebVoOrder;
import com.atguigu.commonutils.vo.UcenterMemberOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author xiaocai
 * @since 2021-02-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String creatOrders(String courseId, String memberId) {
        CourseWebVoOrder courseWebVo = eduClient.getCourseInfoNacos(courseId);
        UcenterMemberOrder ucenterMember = ucenterClient.getMemberInfoById(memberId);

        if(courseWebVo == null || ucenterMember ==null){
            throw new GuliException(20001,"远程服务器出现问题");
        }
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseWebVo.getTitle());
        order.setCourseCover(courseWebVo.getCover());
        order.setTeacherName(courseWebVo.getTeacherName());
        order.setTotalFee(courseWebVo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMember.getMobile());
        order.setNickname(ucenterMember.getNickname());
        order.setStatus(0);//订单状态  0未支付   1已支付
        order.setPayType(1);//支付类型   1微信
        baseMapper.insert(order);

        return order.getOrderNo();
    }

    @Override
    public Order selectOrders(String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);

        Order order = baseMapper.selectOne(wrapper);

        return order;
    }

    @Override
    public boolean isBuyCourse(String memberid, String id) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberid);
        wrapper.eq("course_id", id);
        wrapper.eq("status", 1);
        int count = baseMapper.selectCount(wrapper);

        if(count>0) {
            return true;
        } else {
            return false;
        }
    }
}
