package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {

        try {
            //得到授权临时票据code
            System.out.println("code = " + code);
            System.out.println("state = " + state);

            //从redis中将state获取出来，和当前传入的state作比较
            //如果一致则放行，如果不一致则抛出异常：非法访问

            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);
////        模拟浏览器请求返回微信access_token访问凭证，openid每个微信唯一标识
            String result = HttpClientUtils.get(accessTokenUrl);
            Gson gson = new Gson();
            HashMap map = gson.fromJson(result,HashMap.class);
            String access_token = (String) map.get("access_token");
            String openid = (String) map.get("openid");

            //查询数据库是否有该微信openid
            UcenterMember member = memberService.getByOpenid(openid);
            if (member == null){
                //该微信未注册，则访问微信的组员服务器获取微信个人信息进行注册
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultUserInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userMap = new HashMap();
                HashMap mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
                String nickname = (String) mapUserInfo.get("nickname");
                String headimgurl = (String) mapUserInfo.get("headimgurl");

                member = new UcenterMember();
                member.setAvatar(headimgurl);
                member.setOpenid(openid);
                member.setNickname(nickname);
                memberService.save(member);
            }

            // 生成jwt
            String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());

            //存入cookie
            //CookieUtils.setCookie(request, response, "guli_jwt_token", token);

            //因为端口号不同存在蛞蝓问题，cookie不能跨域，所以这里使用url重写
            return "redirect:http://localhost:3000?token=" + token;


        }catch (Exception e){
            throw new GuliException(20001,"登陆失败");
        }
    }


    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;
    }
}
