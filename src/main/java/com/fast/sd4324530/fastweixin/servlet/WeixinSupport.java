package com.fast.sd4324530.fastweixin.servlet;

import com.fast.sd4324530.fastweixin.handle.EventHandle;
import com.fast.sd4324530.fastweixin.message.Article;
import com.fast.sd4324530.fastweixin.message.BaseMsg;
import com.fast.sd4324530.fastweixin.message.NewsMsg;
import com.fast.sd4324530.fastweixin.message.aes.AesException;
import com.fast.sd4324530.fastweixin.message.aes.WXBizMsgCrypt;
import com.fast.sd4324530.fastweixin.util.MessageUtil;
import com.fast.sd4324530.fastweixin.util.SignUtil;
import com.fast.sd4324530.fastweixin.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static com.fast.sd4324530.fastweixin.util.BeanUtil.isNull;
import static com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull;
import static com.fast.sd4324530.fastweixin.util.CollectionUtil.isEmpty;
import static com.fast.sd4324530.fastweixin.util.CollectionUtil.isNotEmpty;
import static com.fast.sd4324530.fastweixin.util.StrUtil.isNotBlank;

/**
 * 将微信处理通用部分再抽象一层，使用其他框架框架的同学可以自行继承此类集成微信
 *
 * @author peiyu
 * @since 1.1
 */
public abstract class WeixinSupport {

    private static final Logger LOG  = LoggerFactory.getLogger(WeixinSupport.class);
    //充当锁
    private static final Object LOCK = new Object();

//    protected String fromUserName, toUserName;

    /**
     * 微信消息处理器列表
     */
    private static List<com.fast.sd4324530.fastweixin.handle.MessageHandle> messageHandles;
    /**
     * 微信事件处理器列表
     */
    private static List<EventHandle>   eventHandles;

    /**
     * 子类重写，加入自定义的微信消息处理器，细化消息的处理
     *
     * @return 微信消息处理器列表
     */
    protected List<com.fast.sd4324530.fastweixin.handle.MessageHandle> initMessageHandles() {
        return null;
    }

    /**
     * 子类重写，加入自定义的微信事件处理器，细化消息的处理
     *
     * @return 微信事件处理器列表
     */
    protected List<EventHandle> initEventHandles() {
        return null;
    }

    /**
     * 子类提供token用于绑定微信公众平台
     *
     * @return token值
     */
    protected abstract String getToken();

    /**
     * 公众号APPID，使用消息加密模式时用户自行设置
     *
     * @return 微信公众平台提供的appid
     */
    protected String getAppId() {
        return null;
    }

    /**
     * 加密的密钥，使用消息加密模式时用户自行设置
     *
     * @return 用户自定义的密钥
     */
    protected String getAESKey() {
        return null;
    }

    /**
     * 绑定服务器的方法
     * @param request 请求
     * @param response 响应
     */
    public void bindServer(HttpServletRequest request, HttpServletResponse response) {
        if (isLegal(request)) {
            try {
                PrintWriter pw = response.getWriter();
                pw.write(request.getParameter("echostr"));
                pw.flush();
                pw.close();
            } catch (Exception e) {
                LOG.error("绑定服务器异常", e);
            }
        }
    }

    /**
     * 处理微信服务器发来的请求方法
     *
     * @param request http请求对象
     * @return 处理消息的结果，已经是接口要求的xml报文了
     */
    public String processRequest(HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> reqMap = MessageUtil.parseXml(request, getToken(), getAppId(), getAESKey());
        String fromUserName = (String) reqMap.get("FromUserName");
        String toUserName = (String) reqMap.get("ToUserName");
        String msgType = (String) reqMap.get("MsgType");

        LOG.debug("收到消息,消息类型:{}", msgType);

        BaseMsg msg = null;

        if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.EVENT)) {
            String eventType = (String) reqMap.get("Event");
            String ticket = (String) reqMap.get("Ticket");
            com.fast.sd4324530.fastweixin.message.req.QrCodeEvent qrCodeEvent = null;
            if (isNotBlank(ticket)) {
                String eventKey = (String) reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                LOG.debug("ticket:{}", ticket);
                qrCodeEvent = new com.fast.sd4324530.fastweixin.message.req.QrCodeEvent(eventKey, ticket);
                buildBasicEvent(reqMap, qrCodeEvent);
                if (eventType.equals(com.fast.sd4324530.fastweixin.message.req.EventType.SCAN)) {
                    msg = handleQrCodeEvent(qrCodeEvent);
                    if(msg!=null&&msg instanceof NewsMsg){
                       NewsMsg newsmsg= (NewsMsg) msg;
                        Article article = newsmsg.getArticles().get(0);
                        if(article.getTitle().equals("")){
                            try {
                                System.out.println("页面跳转"+article.getUrl());
                                response.sendRedirect(article.getUrl());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }
                    if (isNull(msg)) {
                        msg = processEventHandle(qrCodeEvent);
                    }
                }
            }
            if (eventType.equals(com.fast.sd4324530.fastweixin.message.req.EventType.SUBSCRIBE)) {
                com.fast.sd4324530.fastweixin.message.req.BaseEvent event = new com.fast.sd4324530.fastweixin.message.req.BaseEvent();
                if (qrCodeEvent != null) {
                    event = qrCodeEvent;
                } else {
                    buildBasicEvent(reqMap, event);
                }
                msg = handleSubscribe(event);
                if(msg!=null&&msg instanceof NewsMsg){
                    NewsMsg newsmsg= (NewsMsg) msg;
                    Article article = newsmsg.getArticles().get(0);
                    if(article.getTitle().equals("")){
                        try {
                            System.out.println("页面跳转url"+article.getUrl());
                            response.sendRedirect(article.getUrl());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(com.fast.sd4324530.fastweixin.message.req.EventType.UNSUBSCRIBE)) {
                com.fast.sd4324530.fastweixin.message.req.BaseEvent event = new com.fast.sd4324530.fastweixin.message.req.BaseEvent();
                buildBasicEvent(reqMap, event);
                msg = handleUnsubscribe(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(com.fast.sd4324530.fastweixin.message.req.EventType.CLICK)) {
                String eventKey = (String) reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                com.fast.sd4324530.fastweixin.message.req.MenuEvent event = new com.fast.sd4324530.fastweixin.message.req.MenuEvent(eventKey);
                buildBasicEvent(reqMap, event);
                msg = handleMenuClickEvent(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(com.fast.sd4324530.fastweixin.message.req.EventType.VIEW)) {
                String eventKey = (String) reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                com.fast.sd4324530.fastweixin.message.req.MenuEvent event = new com.fast.sd4324530.fastweixin.message.req.MenuEvent(eventKey);
                buildBasicEvent(reqMap, event);
                msg = handleMenuViewEvent(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(com.fast.sd4324530.fastweixin.message.req.EventType.LOCATION)) {
                double latitude = Double.parseDouble((String) reqMap.get("Latitude"));
                double longitude = Double.parseDouble((String) reqMap.get("Longitude"));
                double precision = Double.parseDouble((String) reqMap.get("Precision"));
                com.fast.sd4324530.fastweixin.message.req.LocationEvent event = new com.fast.sd4324530.fastweixin.message.req.LocationEvent(latitude, longitude,
                        precision);
                buildBasicEvent(reqMap, event);
                msg = handleLocationEvent(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (com.fast.sd4324530.fastweixin.message.req.EventType.SCANCODEPUSH.equals(eventType) || com.fast.sd4324530.fastweixin.message.req.EventType.SCANCODEWAITMSG.equals(eventType)) {
                String eventKey = (String) reqMap.get("EventKey");
                Map<String, Object> scanCodeInfo = (Map<String, Object>)reqMap.get("ScanCodeInfo");
                String scanType = (String) scanCodeInfo.get("ScanType");
                String scanResult = (String) scanCodeInfo.get("ScanResult");
                com.fast.sd4324530.fastweixin.message.req.ScanCodeEvent event = new com.fast.sd4324530.fastweixin.message.req.ScanCodeEvent(eventKey, scanType, scanResult);
                buildBasicEvent(reqMap, event);
                msg = handleScanCodeEvent(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (com.fast.sd4324530.fastweixin.message.req.EventType.PICPHOTOORALBUM.equals(eventType) || com.fast.sd4324530.fastweixin.message.req.EventType.PICSYSPHOTO.equals(eventType) || com.fast.sd4324530.fastweixin.message.req.EventType.PICWEIXIN.equals(eventType)) {
                String eventKey = (String) reqMap.get("EventKey");
                Map<String, Object> sendPicsInfo = (Map<String, Object>)reqMap.get("SendPicsInfo");
                int count = Integer.parseInt((String) sendPicsInfo.get("Count"));
                List<Map> picList = (List) sendPicsInfo.get("PicList");
                com.fast.sd4324530.fastweixin.message.req.SendPicsInfoEvent event = new com.fast.sd4324530.fastweixin.message.req.SendPicsInfoEvent(eventKey, count, picList);
                buildBasicEvent(reqMap, event);
                msg = handlePSendPicsInfoEvent(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            } else if (com.fast.sd4324530.fastweixin.message.req.EventType.TEMPLATESENDJOBFINISH.equals(eventType)) {
                String msgId = (String) reqMap.get("MsgID");
                String status = (String) reqMap.get("Status");
                com.fast.sd4324530.fastweixin.message.req.TemplateMsgEvent event = new com.fast.sd4324530.fastweixin.message.req.TemplateMsgEvent(msgId,status);
                buildBasicEvent(reqMap, event);
                msg = handleTemplateMsgEvent(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.message.req.EventType.MASSSENDJOBFINISH.equals(eventType)){
                String msgId=(String)reqMap.get("MsgID");
                String status=(String)reqMap.get("Status");
                Integer TotalCount=Integer.valueOf(String.valueOf(reqMap.get("TotalCount")));
                Integer filterCount=Integer.valueOf(String.valueOf(reqMap.get("FilterCount")));
                Integer sentCount=Integer.valueOf(String.valueOf(reqMap.get("SentCount")));
                Integer errorCount=Integer.valueOf(String.valueOf(reqMap.get("ErrorCount")));
                com.fast.sd4324530.fastweixin.message.req.SendMessageEvent event=new com.fast.sd4324530.fastweixin.message.req.SendMessageEvent(msgId,status,TotalCount,filterCount,sentCount,errorCount);
                buildBasicEvent(reqMap, event);
                msg=callBackAllMessage(event);
                if (isNull(msg)) {
                    msg = processEventHandle(event);
                }
            }
        } else {
            if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.TEXT)) {
                String content = (String) reqMap.get("Content");
                LOG.debug("文本消息内容:{}", content);
                com.fast.sd4324530.fastweixin.message.req.TextReqMsg textReqMsg = new com.fast.sd4324530.fastweixin.message.req.TextReqMsg(content);
                buildBasicReqMsg(reqMap, textReqMsg);
                msg = handleTextMsg(textReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(textReqMsg);
                }
            } else if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.IMAGE)) {
                String picUrl = (String) reqMap.get("PicUrl");
                String mediaId = (String) reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.message.req.ImageReqMsg imageReqMsg = new com.fast.sd4324530.fastweixin.message.req.ImageReqMsg(picUrl, mediaId);
                buildBasicReqMsg(reqMap, imageReqMsg);
                msg = handleImageMsg(imageReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(imageReqMsg);
                }
            } else if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.VOICE)) {
                String format = (String) reqMap.get("Format");
                String mediaId = (String) reqMap.get("MediaId");
                String recognition = (String) reqMap.get("Recognition");
                com.fast.sd4324530.fastweixin.message.req.VoiceReqMsg voiceReqMsg = new com.fast.sd4324530.fastweixin.message.req.VoiceReqMsg(mediaId, format,
                        recognition);
                buildBasicReqMsg(reqMap, voiceReqMsg);
                msg = handleVoiceMsg(voiceReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(voiceReqMsg);
                }
            } else if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.VIDEO)) {
                String thumbMediaId = (String) reqMap.get("ThumbMediaId");
                String mediaId = (String) reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.message.req.VideoReqMsg videoReqMsg = new com.fast.sd4324530.fastweixin.message.req.VideoReqMsg(mediaId, thumbMediaId);
                buildBasicReqMsg(reqMap, videoReqMsg);
                msg = handleVideoMsg(videoReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(videoReqMsg);
                }
            } else if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.SHORT_VIDEO)) {
                String thumbMediaId = (String) reqMap.get("ThumbMediaId");
                String mediaId = (String) reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.message.req.VideoReqMsg videoReqMsg = new com.fast.sd4324530.fastweixin.message.req.VideoReqMsg(mediaId, thumbMediaId);
                buildBasicReqMsg(reqMap, videoReqMsg);
                msg = hadnleShortVideoMsg(videoReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(videoReqMsg);
                }
            } else if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.LOCATION)) {
                double locationX = Double.parseDouble((String) reqMap.get("Location_X"));
                double locationY = Double.parseDouble((String) reqMap.get("Location_Y"));
                int scale = Integer.parseInt((String) reqMap.get("Scale"));
                String label = (String) reqMap.get("Label");
                com.fast.sd4324530.fastweixin.message.req.LocationReqMsg locationReqMsg = new com.fast.sd4324530.fastweixin.message.req.LocationReqMsg(locationX,
                        locationY, scale, label);
                buildBasicReqMsg(reqMap, locationReqMsg);
                msg = handleLocationMsg(locationReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(locationReqMsg);
                }
            } else if (msgType.equals(com.fast.sd4324530.fastweixin.message.req.ReqType.LINK)) {
                String title = (String) reqMap.get("Title");
                String description = (String) reqMap.get("Description");
                String url = (String) reqMap.get("Url");
                LOG.debug("链接消息地址:{}", url);
                com.fast.sd4324530.fastweixin.message.req.LinkReqMsg linkReqMsg = new com.fast.sd4324530.fastweixin.message.req.LinkReqMsg(title, description, url);
                buildBasicReqMsg(reqMap, linkReqMsg);
                msg = handleLinkMsg(linkReqMsg);
                if (isNull(msg)) {
                    msg = processMessageHandle(linkReqMsg);
                }
            }
        }
        String result = "";
        if (nonNull(msg)) {
            msg.setFromUserName(toUserName);
            msg.setToUserName(fromUserName);
            result = msg.toXml();
            if (StrUtil.isNotBlank(getAESKey())) {
                try {
                    WXBizMsgCrypt pc = new WXBizMsgCrypt(getToken(), getAESKey(), getAppId());
                    result = pc.encryptMsg(result, request.getParameter("timestamp"), request.getParameter("nonce"));
                    LOG.debug("加密后密文:{}", result);
                } catch (AesException e) {
                    LOG.error("加密异常", e);
                }
            }
        }
        return result;
    }

    private BaseMsg processMessageHandle(com.fast.sd4324530.fastweixin.message.req.BaseReqMsg msg) {
        if (isEmpty(messageHandles)) {
            synchronized (LOCK) {
                messageHandles = this.initMessageHandles();
            }
        }
        if (isNotEmpty(messageHandles)) {
            for (com.fast.sd4324530.fastweixin.handle.MessageHandle messageHandle : messageHandles) {
                BaseMsg resultMsg = null;
                boolean result;
                try {
                    result = messageHandle.beforeHandle(msg);
                } catch (Exception e) {
                    result = false;
                }
                if (result) {
                    resultMsg = messageHandle.handle(msg);
                }
                if (nonNull(resultMsg)) {
                    return resultMsg;
                }
            }
        }
        return null;
    }

    private BaseMsg processEventHandle(com.fast.sd4324530.fastweixin.message.req.BaseEvent event) {
        if (isEmpty(eventHandles)) {
            synchronized (LOCK) {
                eventHandles = this.initEventHandles();
            }
        }
        if (isNotEmpty(eventHandles)) {
            for (EventHandle eventHandle : eventHandles) {
                BaseMsg resultMsg = null;
                boolean result;
                try {
                    result = eventHandle.beforeHandle(event);
                } catch (Exception e) {
                    result = false;
                }
                if (result) {
                    resultMsg = eventHandle.handle(event);
                }
                if (nonNull(resultMsg)) {
                    return resultMsg;
                }
            }
        }
        return null;
    }

    /**
     * 处理文本消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleTextMsg(com.fast.sd4324530.fastweixin.message.req.TextReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理图片消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleImageMsg(com.fast.sd4324530.fastweixin.message.req.ImageReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理语音消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleVoiceMsg(com.fast.sd4324530.fastweixin.message.req.VoiceReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理视频消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleVideoMsg(com.fast.sd4324530.fastweixin.message.req.VideoReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理小视频消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg hadnleShortVideoMsg(com.fast.sd4324530.fastweixin.message.req.VideoReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理地理位置消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleLocationMsg(com.fast.sd4324530.fastweixin.message.req.LocationReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理链接消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleLinkMsg(com.fast.sd4324530.fastweixin.message.req.LinkReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理扫描二维码事件，有需要时子类重写
     *
     * @param event 扫描二维码事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleQrCodeEvent(com.fast.sd4324530.fastweixin.message.req.QrCodeEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理地理位置事件，有需要时子类重写
     *
     * @param event 地理位置事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleLocationEvent(com.fast.sd4324530.fastweixin.message.req.LocationEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单点击事件，有需要时子类重写
     *
     * @param event 菜单点击事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleMenuClickEvent(com.fast.sd4324530.fastweixin.message.req.MenuEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单跳转事件，有需要时子类重写
     *
     * @param event 菜单跳转事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleMenuViewEvent(com.fast.sd4324530.fastweixin.message.req.MenuEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单扫描推事件，有需要时子类重写
     *
     * @param event 菜单扫描推事件对象
     * @return 响应的消息对象
     */
    protected BaseMsg handleScanCodeEvent(com.fast.sd4324530.fastweixin.message.req.ScanCodeEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单弹出相册事件，有需要时子类重写
     *
     * @param event 菜单弹出相册事件
     * @return 响应的消息对象
     */
    protected BaseMsg handlePSendPicsInfoEvent(com.fast.sd4324530.fastweixin.message.req.SendPicsInfoEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理模版消息发送事件，有需要时子类重写
     *
     * @param event 菜单弹出相册事件
     * @return 响应的消息对象
     */
    protected BaseMsg handleTemplateMsgEvent(com.fast.sd4324530.fastweixin.message.req.TemplateMsgEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理添加关注事件，有需要时子类重写
     *
     * @param event 添加关注事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleSubscribe(com.fast.sd4324530.fastweixin.message.req.BaseEvent event) {
        return null;
    }

    /**
     * 接收群发消息的回调方法
     *
     * @param event 群发回调方法
     * @return 响应消息对象
     */
    protected  BaseMsg callBackAllMessage(com.fast.sd4324530.fastweixin.message.req.SendMessageEvent event){return  handleDefaultEvent(event);}

    /**
     * 处理取消关注事件，有需要时子类重写
     *
     * @param event 取消关注事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleUnsubscribe(com.fast.sd4324530.fastweixin.message.req.BaseEvent event) {
        return null;
    }

    protected BaseMsg handleDefaultMsg(com.fast.sd4324530.fastweixin.message.req.BaseReqMsg msg) {
        return null;
    }

    protected BaseMsg handleDefaultEvent(com.fast.sd4324530.fastweixin.message.req.BaseEvent event) {
        return null;
    }

    private void buildBasicReqMsg(Map<String, Object> reqMap, com.fast.sd4324530.fastweixin.message.req.BaseReqMsg reqMsg) {
        addBasicReqParams(reqMap, reqMsg);
        reqMsg.setMsgId((String) reqMap.get("MsgId"));
    }

    private void buildBasicEvent(Map<String, Object> reqMap, com.fast.sd4324530.fastweixin.message.req.BaseEvent event) {
        addBasicReqParams(reqMap, event);
        event.setEvent((String) reqMap.get("Event"));
    }

    private void addBasicReqParams(Map<String, Object> reqMap, com.fast.sd4324530.fastweixin.message.req.BaseReq req) {
        req.setMsgType((String) reqMap.get("MsgType"));
        req.setFromUserName((String) reqMap.get("FromUserName"));
        req.setToUserName((String) reqMap.get("ToUserName"));
        req.setCreateTime(Long.parseLong((String) reqMap.get("CreateTime")));
    }

    protected boolean isLegal(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        return SignUtil.checkSignature(getToken(), signature, timestamp, nonce);
    }
}