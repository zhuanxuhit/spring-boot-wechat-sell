package com.babyfs.controller;

import com.babyfs.config.ProjectUrlConfig;
import com.babyfs.enums.ResultEnum;
import com.babyfs.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author 颛顼
 * @Description: 获取openid
 * @date 2018-03-24 上午11:06
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/authorize")
    public String auth(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
//        log.info("输入参数：{}", returnUrl);
        String url = projectUrlConfig.getWechatMpAuthorize() + "/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url,
                WxConsts.OAuth2Scope.SNSAPI_BASE,
                URLEncoder.encode(returnUrl, "UTF-8"));
        log.info("【微信网页授权获取 code】, result={}", redirectUrl);
        return "redirect:" + redirectUrl;
    }
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException ex) {
            log.error("【微信网页授权】error:{}", ex.toString());
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), ex.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("opiedId: " + openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }

}
