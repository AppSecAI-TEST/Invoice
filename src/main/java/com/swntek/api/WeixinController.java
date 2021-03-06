package com.swntek.api;

import com.fast.sd4324530.fastweixin.api.OauthAPI;
import com.fast.sd4324530.fastweixin.api.config.ApiConfig;
import com.fast.sd4324530.fastweixin.api.enums.OauthScope;
import com.fast.sd4324530.fastweixin.api.response.OauthGetTokenResponse;
import com.fast.sd4324530.fastweixin.handle.EventHandle;
import com.fast.sd4324530.fastweixin.handle.MessageHandle;
import com.fast.sd4324530.fastweixin.message.Article;
import com.fast.sd4324530.fastweixin.message.BaseMsg;
import com.fast.sd4324530.fastweixin.message.NewsMsg;
import com.fast.sd4324530.fastweixin.message.TextMsg;
import com.fast.sd4324530.fastweixin.message.req.BaseEvent;
import com.fast.sd4324530.fastweixin.message.req.QrCodeEvent;
import com.fast.sd4324530.fastweixin.message.req.ScanCodeEvent;
import com.fast.sd4324530.fastweixin.message.req.TextReqMsg;
import com.fast.sd4324530.fastweixin.servlet.WeixinControllerSupport;
import com.swntek.Utils.Constact;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: WeixinController
 * @date 2017年6月5日 下午9:23:31
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController extends WeixinControllerSupport {
    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);
    private static final String TOKEN = "ImwMGG0dY9p4rm3145POZ0PDvGM5r97d";
//    private static final String APPID = "wx4409ac4d64290eac";
//    private static final String APPSECRET = "61e128d4a18aaea7bc08b9fbe1db484a";
    private static final String APPID = "wxab36b73feffe277c";
    private static final String APPSECRET = "227aa8ab2f33798f7ac6b09b138a360c";


    public static String getTOKEN() {
        return TOKEN;
    }

    public static String getAPPID() {
        return APPID;
    }

    public static String getAPPSECRET() {
        return APPSECRET;
    }


    //设置TOKEN，用于绑定微信服务器
    @Override
    protected String getToken() {
        return TOKEN;
    }

    //使用安全模式时设置：APPID
    //不再强制重写，有加密需要时自行重写该方法
    @Override
    protected String getAppId() {
        return APPID;
    }

    //使用安全模式时设置：密钥
    //不再强制重写，有加密需要时自行重写该方法
    @Override
    protected String getAESKey() {
        return "gI6SXIK4JDYn21Bpg7UYUf7CZt2Zwv3lC0fXyn8cMtI";
    }

    //重写父类方法，处理对应的微信消息 返回null则表示不处理
    @Override
    protected BaseMsg handleTextMsg(TextReqMsg msg) {
        String content = msg.getContent();
        log.debug("用户发送到服务器的内容:{}", content);
        System.out.println("content:" + content);
        return new TextMsg("欢迎关注好开");
    }

    @Override
    protected BaseMsg handleScanCodeEvent(ScanCodeEvent event) {
        System.out.println("扫苗成功ScanResult:"+event.getScanResult());
        return new TextMsg("好开");
    }
    //参数二维码 已关注扫码
    @Override
    protected BaseMsg handleQrCodeEvent(QrCodeEvent event) {
//        String shopid = event.getEventKey();
//        String openid=event.getFromUserName();
//        System.out.println("event.getEventKey():"+ shopid);
//        Article article=new Article("填写发票信息","填写发票信息","http://pic.58pic.com/58pic/12/21/22/54P58PICBkX.jpg", Constact.baseurl+"register.html?openid="+openid+"shopid="+shopid);
//        NewsMsg newsMsg=new NewsMsg();
//        newsMsg.add(article);
//        return newsMsg;
        return null;
    }
    //参数二维码 未关注扫码
    @Override
    protected BaseMsg handleSubscribe(BaseEvent event) {
        //交给handler
//        QrCodeEvent qrcodeevent =(QrCodeEvent) event;
//        String eventKey = qrcodeevent.getEventKey();
//        String shopid=eventKey.split("_")[1];
//        String openid=event.getFromUserName();
//        Article article=new Article("填写发票信息","填写发票信息","http://pic.58pic.com/58pic/12/21/22/54P58PICBkX.jpg", Constact.baseurl+"register.html?openid="+openid+"shopid="+shopid);
//        NewsMsg newsMsg=new NewsMsg();
//        newsMsg.add(article);
//        System.out.println("event.getEventKey():"+ eventKey);
//        return newsMsg;
        return null;
    }

    /*1.1版本新增，重写父类方法，加入自定义微信消息处理器
             *不是必须的，上面的方法是统一处理所有的文本消息，如果业务觉复杂，上面的会显得比较乱
             *这个机制就是为了应对这种情况，每个MessageHandle就是一个业务，只处理指定的那部分消息
             */
    @Override
    protected List<MessageHandle> initMessageHandles() {
        List<MessageHandle> handles = new ArrayList<MessageHandle>();
        return handles;
    }

    //1.1版本新增，重写父类方法，加入自定义微信事件处理器，同上
    @Override
    protected List<EventHandle> initEventHandles() {
        List<EventHandle> handles = new ArrayList<EventHandle>();
        //关注事件处理类添加进来
//        handles.add(subscribeHandler);
        return handles;
    }

    @RequestMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String shopid=request.getParameter("shopid");
        ApiConfig apiConfig = new ApiConfig(getAPPID(), getAPPSECRET());
        OauthAPI oauthAPI = new OauthAPI(apiConfig);
        System.out.println("code"+code+"shopid:"+shopid);
        if(code ==null) {
            String wxauthurl = oauthAPI.getOauthPageUrl(Constact.baseurl+"weixin/auth", OauthScope.SNSAPI_BASE, shopid);
            System.out.println("wxauthurl"+wxauthurl);
            response.sendRedirect(wxauthurl);
            return;
        }else{
            OauthGetTokenResponse token = oauthAPI.getToken(code);
            String openid = token.getOpenid();
            shopid = request.getParameter("state");
            System.out.println("getopenid():"+openid+"shopid:"+shopid);
            response.sendRedirect(Constact.baseurl+"addInvoice.html?openid="+openid+"&shopid="+shopid);
            return;
        }
    }
}