package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.LoginVo;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-29
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){

        ucenterMemberService.register(registerVo);

        return R.ok();
    }

    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){

        String token = ucenterMemberService.login(loginVo);

        return R.ok().data("token",token);
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwt工具类，获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    @GetMapping("getMemberInfoById/{id}")
    public UcenterMemberOrder getMemberInfoById(@PathVariable String id){
//        System.out.println(id);
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberOrder memeberPay = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,memeberPay);
        return memeberPay;
    }

    //统计某一天的注册人数
    @GetMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        Integer count = ucenterMemberService.registerCountByDay(day);
        return R.ok().data("registerCount",count);
    }



}

