package com.king.bio.web.startup;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.log.Log4jLog;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import com.king.bio.common.model._MappingKit;
import com.king.bio.web.common.interceptors.TokenInterceptor;
import com.king.bio.web.controller.base.HtmlHandler;
import com.king.bio.web.controller.miniapp.wjt.config.WxMaConfiguration;
import com.king.bio.web.controller.miniapp.wjt.config.WxMaProperties;
import com.king.bio.web.route.RouteConfigBio;

/**
 * API引导式配置
 */
public class JFinalStartup extends JFinalConfig {
    final static Log log = Log4jLog.getLog(JFinalStartup.class);
    /**
     * 供Shiro插件使用。
     */
    private Routes routes;
    private String env = "prod";

    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        String config = "config_" + env + ".properties";
        System.out.println(config);
        //
        PropKit.use(config);
        //
        boolean devMode = PropKit.getBoolean("devMode", true);
        me.setDevMode(devMode);
        me.setViewType(ViewType.FREE_MARKER);
        me.setEncoding("UTF-8");
        me.setBaseUploadPath("/");
        //
        WxaConfig wxaConfig = new WxaConfig();
        wxaConfig.setAppId(PropKit.get("wechat.miniapp.appid"));
        wxaConfig.setAppSecret(PropKit.get("wechat.miniapp.secret"));
        wxaConfig.setToken(PropKit.get("wechat.miniapp.token"));
        wxaConfig.setEncodingAesKey(PropKit.get("wechat.miniapp.aesKey"));
        wxaConfig.setMessageEncrypt(PropKit.getBoolean("wechat.miniapp.encryptMessage", false));
        //
        WxaConfigKit.setWxaConfig(wxaConfig);
        WxaConfigKit.useJsonMsgParser();
        WxaConfigKit.setDevMode(devMode);
        //
        WxMaProperties wxaConfig2 = new WxMaProperties();
        wxaConfig2.setAppid(PropKit.get("wechat.miniapp.appid"));
        wxaConfig2.setSecret(PropKit.get("wechat.miniapp.secret"));
        wxaConfig2.setToken(PropKit.get("wechat.miniapp.token"));
        wxaConfig2.setAesKey(PropKit.get("wechat.miniapp.aesKey"));
        wxaConfig2.setMsgDataFormat(PropKit.get("wechat.miniapp.msgDataFormat"));
        WxMaConfiguration.setProperties(wxaConfig2);
        //
        log.info(String.format("##config.properties=%s##", config));
        //
        //
        me.setError401View("/static/html/401.html");// 没登录
        me.setError403View("/static/html/403.html");// 没权限
        me.setError404View("/static/html/404.html");
        me.setError500View("/static/html/500.html");
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes me) {
        this.routes = me;
        me.setBaseViewPath("/WEB-INF/pages/");
        RouteConfigBio.config(me);
    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins me) {
        /**
         * 配置C3p0数据库连接池插件 C3p0Plugin c3p0Plugin = new
         * C3p0Plugin(getProperty("jdbc.url"), getProperty("jdbc.username"),
         * getProperty("jdbc.password").trim()); me.add(c3p0Plugin);
         */
        // 配置alibaba数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(
                PropKit.get("jdbc.url"),
                PropKit.get("jdbc.username"), 
                PropKit.get("jdbc.password"),
                PropKit.get("jdbc.driver"));
        WallFilter wall = new WallFilter();
        wall.setDbType(PropKit.get("jdbc.dbType"));
        druidPlugin.addFilter(wall);
        druidPlugin.addFilter(new StatFilter());
        me.add(druidPlugin);
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setDevMode(PropKit.getBoolean("devMode"));
        log.info(String.format("##showSql=%s##", PropKit.getBoolean("showSql")));
        arp.setShowSql(PropKit.getBoolean("showSql"));
        me.add(arp);
        // 配置models
        _MappingKit.mapping(arp);
        // try {
        // // mongodb
        // MongoJFinalPlugin mongodbPlugin = new MongoJFinalPlugin();
        // mongodbPlugin.add(PropKit.get("mongodb.host"),PropKit.getInt("mongodb.port"));
        // mongodbPlugin.setDatabase(PropKit.get("mongodb.db"));
        // mongodbPlugin.auth(PropKit.get("mongodb.username"),PropKit.get("mongodb.password"));
        // me.add(mongodbPlugin);
        // } catch (Exception e) {
        // System.out.println("初始化mongodb失败，忽略~~~");
        // }

        // 配置shiro插件
        me.add(new ShiroPlugin(this.routes));
        // fastdfs
        // try {
        // ClientGlobal.initByProperties("fastdfs-client.properties");
        // log.info("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
        // } catch (IOException e) {
        // //e.printStackTrace();
        // log.error(e.getMessage());
        // } catch (MyException e) {
        // //e.printStackTrace();
        // log.error(e.getMessage());
        // }
    }

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors me) {
        // shiro权限拦截器配置
        me.add(new ShiroInterceptor());
        // 让freemarker可以使用session
        me.add(new SessionInViewInterceptor());
        // API令牌
        me.add(new TokenInterceptor());
    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler());
        DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
        me.add(dvh);
        me.add(new HtmlHandler());
    }

    /**
     * 
     */
    @Override
    public void configEngine(Engine arg0) {
    }
    
    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
        ApiConfig ac = new ApiConfig();
        // 配置微信 API 相关参数
        ac.setAppId(PropKit.get("wechat.miniapp.appid"));
        ac.setAppSecret(PropKit.get("wechat.miniapp.secret"));
        ac.setToken(PropKit.get("wechat.miniapp.token"));
        ac.setEncryptMessage(PropKit.getBoolean("wechat.miniapp.encryptMessage", false));
        ac.setEncodingAesKey(PropKit.get("wechat.miniapp.aesKey"));
        /**
         * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
         */
        ApiConfigKit.putApiConfig(ac);
    }
    /**
     * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main
     * 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        if (args != null && args.length >= 4) {
            JFinal.main(args);
        } else {
            JFinal.start("target/bio", 8080, "/", 5);
        }
    }

}
