package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.LoginVo;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-29
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"注册信息不为空");
        }

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new GuliException(20001,"该号码已被注册");
        }

        String mobleCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(mobleCode)){
            throw new GuliException(20001,"验证码错误");
        }

        UcenterMember member = new UcenterMember();
        registerVo.setPassword(MD5.encrypt(password));
        BeanUtils.copyProperties(registerVo,member);
        member.setAvatar("https://edu-xiao.oss-cn-guangzhou.aliyuncs.com/2021/01/21/f138959362c54065ba24c1e12f7c5d84file.png");
        baseMapper.insert(member);

    }

    @Override
    public String login(LoginVo loginVo) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"注册信息不为空");
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("mobile",mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        if(member == null){
            throw new GuliException(20001,"没有该用户，请注册");
        }

        if (!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(20001,"密码错误");
        }

        if(member.getIsDisabled()){
            throw new GuliException(20001,"该用户已被禁用，请联系管理员");
        }

        //使用Jwt生成token
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
        return token;
    }

    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer registerCountByDay(String day) {
        Integer count = baseMapper.selectRegisterCount(day);
        return count;
    }
}
