package com.github.weixin.demo.config;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * Created by FirenzesEagle on 2016/6/1 0001.
 * Email:liumingbo2008@gmail.com
 */
public class MenuConfig {

    protected static WxMenu getMenu2(){
        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setType(MenuButtonType.VIEW);
        button1.setName("在线预约");
//        button1.setUrl("http://sbnqwx.natappfree.cc/pages/index.html");
        button1.setUrl("http://x7ysrv.natappfree.cc/");
        menu.getButtons().add(button1);
        return menu;
    }

    /**
     * 运行此main函数即可生成公众号自定义菜单，注意要修改MainConfig构造方法行代码的对应四个参数为实际值
     *
     * @param args
     */
    public static void main(String[] args) {
//       #公众号 APP_ID
        String appId="wxf31ae09f9e37eeb0";
//#公众号 APP_SECRET
        String appSecret="58687f28def4d9630f1b13a66e5e637a";
//#公众号 TOKEN
        String token= "";
//#公众号 AES_KEY
        String aesKey="";

        MainConfig mainConfig = new MainConfig(appId, appSecret, token, aesKey);
        WxMpService wxMpService = mainConfig.wxMpService();
        try {
            wxMpService.getMenuService().menuDelete();
            wxMpService.getMenuService().menuCreate(getMenu2());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义菜单结构
     *
     * @return
     */
    protected static WxMenu getMenu() {

        MainConfig mainConfig = new MainConfig("appid", "appsecret", "token", "aesKey");
        WxMpService wxMpService = mainConfig.wxMpService();

        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setType(MenuButtonType.VIEW);
        button1.setName("买家订单");
        button1.setUrl(wxMpService.oauth2buildAuthorizationUrl("", "snsapi_base", ""));

        WxMenuButton button2 = new WxMenuButton();
        button2.setName("我是卖家");

        WxMenuButton button21 = new WxMenuButton();
        button21.setType(MenuButtonType.VIEW);
        button21.setName("我的订单");
        button21.setUrl(wxMpService.oauth2buildAuthorizationUrl("", "snsapi_base", ""));

        WxMenuButton button22 = new WxMenuButton();
        button22.setType(MenuButtonType.VIEW);
        button22.setName("收入统计");
        button22.setUrl(wxMpService.oauth2buildAuthorizationUrl("", "snsapi_base", ""));

        WxMenuButton button23 = new WxMenuButton();
        button23.setType(MenuButtonType.VIEW);
        button23.setName("发布商品");
        button23.setUrl(wxMpService.oauth2buildAuthorizationUrl("", "snsapi_base", ""));

        WxMenuButton button24 = new WxMenuButton();
        button24.setType(MenuButtonType.VIEW);
        button24.setName("商品管理");
        button24.setUrl(wxMpService.oauth2buildAuthorizationUrl("", "snsapi_base", ""));

        button2.getSubButtons().add(button21);
        button2.getSubButtons().add(button22);
        button2.getSubButtons().add(button23);
        button2.getSubButtons().add(button24);

        WxMenuButton button3 = new WxMenuButton();
        button3.setType(MenuButtonType.CLICK);
        button3.setName("使用帮助");
        button3.setKey("help");

        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);

        return menu;
    }


    public static class MainConfig {

        private String appId;
        private String appSecret;
        private String token;
        private String aesKey;

        /**
         * 为了生成自定义菜单使用的构造函数
         *
         * @param appId
         * @param appSecret
         * @param token
         * @param aesKey
         */
        protected MainConfig(String appId, String appSecret, String token, String aesKey) {
            this.appId = appId;
            this.appSecret = appSecret;
            this.token = token;
            this.aesKey = aesKey;
        }

        public WxMpConfigStorage wxMpConfigStorage() {
            WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
            configStorage.setAppId(this.appId);
            configStorage.setSecret(this.appSecret);
            configStorage.setToken(this.token);
            configStorage.setAesKey(this.aesKey);
            return configStorage;
        }

        public WxMpService wxMpService() {
            WxMpService wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
            return wxMpService;
        }

    }



}
