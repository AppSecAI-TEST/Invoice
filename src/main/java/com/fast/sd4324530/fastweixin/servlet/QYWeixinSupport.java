package com.fast.sd4324530.fastweixin.servlet;

import com.fast.sd4324530.fastweixin.company.handle.QYMessageHandle;
import com.fast.sd4324530.fastweixin.company.message.resp.QYBaseRespMsg;
import com.fast.sd4324530.fastweixin.message.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * ====================================================================
 * 上海聚攒软件开发有限公司
 * --------------------------------------------------------------------
 *
 * @author Nottyjay
 * @version 1.0.beta
 * ====================================================================
 */
public abstract class QYWeixinSupport{

    private static final Logger LOG = LoggerFactory.getLogger(QYWeixinSupport.class);

    private static final Object LOCK = new Object();

//    protected String fromUserName, toUserName;

//    private WXBizMsgCrypt wxcpt;

    /**
     * 子类提供token用于绑定微信企业平台
     *
     * @return token
     */
    protected abstract String getToken();

    /**
     * 子类提供cropId用于绑定微信企业平台
     *
     * @return cropId
     */
    protected abstract String getCropId();

    /**
     * 加密的密钥，消息解密时需要
     *
     * @return 密钥
     */
    protected abstract String getAESKey();

    /**
     * 微信消息处理器列表
     */
    private static List<QYMessageHandle> messageHandles;

    /**
     * 微信事件处理器列表
     */
    private static List<com.fast.sd4324530.fastweixin.company.handle.QYEventHandle> eventHandles;

    /**
     * 子类重写，加入自定义的微信消息处理器，细化消息的处理
     *
     * @return 微信消息处理器列表
     */
    protected List<QYMessageHandle> initMessageHandles(){
        return null;
    }

    /**
     * 子类重写，加入自定义的微信事件处理器，细化消息的处理
     *
     * @return 微信事件处理器列表
     */
    protected List<com.fast.sd4324530.fastweixin.company.handle.QYEventHandle> initEventHandles(){
        return null;
    }

    /**
     * 绑定服务器的方法
     * @param request 请求
     * @param response 响应
     */
    public void bindServer(HttpServletRequest request, HttpServletResponse response){
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(com.fast.sd4324530.fastweixin.util.StrUtil.isBlank(getToken()) || com.fast.sd4324530.fastweixin.util.StrUtil.isBlank(getAESKey()) || com.fast.sd4324530.fastweixin.util.StrUtil.isBlank(getCropId())){
            pw.write("");
            pw.flush();
            pw.close();
        }
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(getToken(), getAESKey(), getCropId());
            String echoStr = pc.verifyUrl(request.getParameter("msg_signature"), request.getParameter("timestamp"), request.getParameter("nonce"), request.getParameter("echostr"));
            pw.write(echoStr);
            pw.flush();
            pw.close();
        } catch (com.fast.sd4324530.fastweixin.message.aes.AesException e) {
            e.printStackTrace();
            pw.write("");
            pw.flush();
            pw.close();
        }
    }

    /**
     * 处理微信服务器发来的请求方法
     *
     * @param request http请求对象
     * @return 处理消息的结果，已经是接口要求的XML的报文了
     */
    public String processRequest(HttpServletRequest request){
        Map<String, Object> reqMap = com.fast.sd4324530.fastweixin.util.MessageUtil.parseXml(request, getToken(), getCropId(), getAESKey());
        String fromUserName = (String)reqMap.get("FromUserName");
        String toUserName = (String)reqMap.get("ToUserName");
        String msgType = (String)reqMap.get("MsgType");

        LOG.debug("收到消息，消息类型：{}", msgType);

        QYBaseRespMsg msg = null;

        if(msgType.equals(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.EVENT)){
            String eventType = (String)reqMap.get("Event");
            LOG.debug("收到消息，事件类型：{} " , eventType);

            if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.SUBSCRIBE.equalsIgnoreCase(eventType)){
                com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent();
                buildBasicEvent(reqMap, event);
                msg = handleSubScribe(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }

            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.UNSUBSCRIBE.equalsIgnoreCase(eventType)){
                com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent();
                buildBasicEvent(reqMap, event);
                msg = handleUnsubscribe(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.CLICK.equalsIgnoreCase(eventType)){
                String eventKey = (String)reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                com.fast.sd4324530.fastweixin.company.message.req.QYMenuEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYMenuEvent(eventKey);
                buildBasicEvent(reqMap, event);
                msg = handleMenuClickEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.VIEW.equalsIgnoreCase(eventType)){
                String eventKey = (String)reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                com.fast.sd4324530.fastweixin.company.message.req.QYMenuEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYMenuEvent(eventKey);
                buildBasicEvent(reqMap, event);
                msg = handleMenuViewEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.LOCATION.equalsIgnoreCase(eventType)){
                double latitude = Double.parseDouble((String)reqMap.get("Latitude"));
                double longitude = Double.parseDouble((String)reqMap.get("Longitude"));
                double precision = Double.parseDouble((String)reqMap.get("Precision"));
                com.fast.sd4324530.fastweixin.company.message.req.QYLocationEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYLocationEvent(latitude, longitude, precision);
                buildBasicEvent(reqMap, event);
                msg = handleLocationEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.SCANCODEPUSH.equalsIgnoreCase(eventType) || com.fast.sd4324530.fastweixin.company.message.req.QYEventType.SCANCODEWAITMSG.equalsIgnoreCase(eventType)){
                String eventKey = (String)reqMap.get("EventKey");
                Map<String, Object> scanCodeInfo = (Map<String, Object>)reqMap.get("ScanCodeInfo");
                String scanType = (String)scanCodeInfo.get("ScanType");
                String scanResult = (String)scanCodeInfo.get("ScanResult");
                com.fast.sd4324530.fastweixin.company.message.req.QYScanCodeEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYScanCodeEvent(eventKey, scanType, scanResult);
                buildBasicEvent(reqMap, event);
                msg = handleScanCodeEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.PICPHOTOORALBUM.equalsIgnoreCase(eventType) || com.fast.sd4324530.fastweixin.company.message.req.QYEventType.PICSYSPHOTO.equalsIgnoreCase(eventType) || com.fast.sd4324530.fastweixin.company.message.req.QYEventType.PICWEIXIN.equalsIgnoreCase(eventType)){
                String eventKey = (String)reqMap.get("EventKey");
                Map<String, Object> sendPicsInfo = (Map<String, Object>)reqMap.get("SendPicsInfo");
                int count = Integer.parseInt((String)sendPicsInfo.get("Count"));
                List<Map> picList = (List)sendPicsInfo.get("PicList");
                com.fast.sd4324530.fastweixin.company.message.req.QYSendPicInfoEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYSendPicInfoEvent(eventKey, count, picList);
                buildBasicEvent(reqMap, event);
                msg = handleSendPicsInfoEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.ENTERAGENT.equalsIgnoreCase(eventType)){
                com.fast.sd4324530.fastweixin.company.message.req.QYEnterAgentEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYEnterAgentEvent();
                buildBasicEvent(reqMap, event);
                msg = handleEnterAgentEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYEventType.BATCHJOBRESULT.equalsIgnoreCase(eventType)){
                Map<String, Object> batchJob = (Map<String, Object>)reqMap.get("BatchJob");
                String jobId = (String)batchJob.get("JobId");
                String jobType = (String)batchJob.get("JobType");
                int errCode = (Integer)batchJob.get("ErrCode");
                String errMsg = (String)batchJob.get("ErrMsg");
                com.fast.sd4324530.fastweixin.company.message.req.QYBatchJobEvent event = new com.fast.sd4324530.fastweixin.company.message.req.QYBatchJobEvent(jobId, jobType, errCode, errMsg);
                buildBasicEvent(reqMap, event);
                msg = handleBatchJobEvent(event);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processEventHandle(event);
                }
            }
        }else{
            if(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.TEXT.equalsIgnoreCase(msgType)){
                String content = (String)reqMap.get("Content");
                LOG.debug("文本消息内容：{}", content);
                com.fast.sd4324530.fastweixin.company.message.req.QYTextReqMsg textReqMsg = new com.fast.sd4324530.fastweixin.company.message.req.QYTextReqMsg(content);
                buildBasicReqMsg(reqMap, textReqMsg);
                msg = handleTextMsg(textReqMsg);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processMessageHandle(textReqMsg);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.IMAGE.equalsIgnoreCase(msgType)){
                String picUrl = (String)reqMap.get("PicUrl");
                String mediaId = (String)reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.company.message.req.QYImageReqMsg imageReqMsg = new com.fast.sd4324530.fastweixin.company.message.req.QYImageReqMsg(picUrl, mediaId);
                buildBasicReqMsg(reqMap, imageReqMsg);
                msg = handleImageMsg(imageReqMsg);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processMessageHandle(imageReqMsg);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.VOICE.equalsIgnoreCase(msgType)){
                String format = (String)reqMap.get("Format");
                String mediaId = (String)reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.company.message.req.QYVoiceReqMsg voiceReqMsg = new com.fast.sd4324530.fastweixin.company.message.req.QYVoiceReqMsg(mediaId, format);
                buildBasicReqMsg(reqMap, voiceReqMsg);
                msg = handleVoiceMsg(voiceReqMsg);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processMessageHandle(voiceReqMsg);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.VIDEO.equalsIgnoreCase(msgType)){
                String thumbMediaId = (String)reqMap.get("ThumbMediaId");
                String mediaId = (String)reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.company.message.req.QYVideoReqMsg videoReqMsg = new com.fast.sd4324530.fastweixin.company.message.req.QYVideoReqMsg(mediaId, thumbMediaId);
                buildBasicReqMsg(reqMap, videoReqMsg);
                msg = handleVideoMsg(videoReqMsg);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processMessageHandle(videoReqMsg);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.SHORT_VIDEO.equalsIgnoreCase(msgType)){
                String thumbMediaId = (String)reqMap.get("ThumbMediaId");
                String mediaId = (String)reqMap.get("MediaId");
                com.fast.sd4324530.fastweixin.company.message.req.QYVideoReqMsg videoReqMsg = new com.fast.sd4324530.fastweixin.company.message.req.QYVideoReqMsg(mediaId, thumbMediaId);
                buildBasicReqMsg(reqMap, videoReqMsg);
                msg = handleShortVideoMsg(videoReqMsg);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processMessageHandle(videoReqMsg);
                }
            }else if(com.fast.sd4324530.fastweixin.company.message.req.QYReqType.LOCATION.equalsIgnoreCase(msgType)){
                double locationX = Double.parseDouble((String) reqMap.get("Location_X"));
                double locationY = Double.parseDouble((String)reqMap.get("Location_Y"));
                int scale = Integer.parseInt((String)reqMap.get("Scale"));
                String label = (String)reqMap.get("Label");
                com.fast.sd4324530.fastweixin.company.message.req.QYLocationReqMsg locationReqMsg = new com.fast.sd4324530.fastweixin.company.message.req.QYLocationReqMsg(locationX, locationY, scale, label);
                buildBasicReqMsg(reqMap, locationReqMsg);
                msg = handleLocationMsg(locationReqMsg);
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(msg)){
                    msg = processMessageHandle(locationReqMsg);
                }
            }
        }

        String result = "";
        if(com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull(msg)){
            msg.setFromUserName(toUserName);
            msg.setToUserName(fromUserName);
            result = msg.toXml();
            try{
                WXBizMsgCrypt pc = new WXBizMsgCrypt(getToken(), getAESKey(), getCropId());
                result = pc.encryptMsg(result, request.getParameter("timestamp"), request.getParameter("nonce"));
                LOG.debug("加密后密文：{}", result);
            }catch (com.fast.sd4324530.fastweixin.message.aes.AesException e){
                LOG.error("加密异常", e);
            }
        }
        return result;
    }

    /**
     * 自定义的消息事件处理
     * @param msg 微信消息
     * @return 响应结果
     */
    private QYBaseRespMsg processMessageHandle(com.fast.sd4324530.fastweixin.company.message.req.QYBaseReqMsg msg){
        if(com.fast.sd4324530.fastweixin.util.CollectionUtil.isEmpty(messageHandles)){
            synchronized (LOCK){
                messageHandles = this.initMessageHandles();
            }
        }
        if(com.fast.sd4324530.fastweixin.util.CollectionUtil.isNotEmpty(messageHandles)){
            for(QYMessageHandle messageHandle : messageHandles){
                QYBaseRespMsg resultMsg = null;
                boolean result;
                try{
                    result = messageHandle.beforeHandle(msg);
                }catch (Exception e){
                    result = false;
                }
                if(result){
                    resultMsg = messageHandle.handle(msg);
                }
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull(resultMsg)){
                    return resultMsg;
                }
            }
        }
        return null;
    }

    /**
     * 自定义的消息事件处理
     * @param event 事件
     * @return 响应结果
     */
    private QYBaseRespMsg processEventHandle(com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event){
        if(com.fast.sd4324530.fastweixin.util.CollectionUtil.isEmpty(eventHandles)){
            synchronized (LOCK){
                eventHandles = this.initEventHandles();
            }
        }
        if(com.fast.sd4324530.fastweixin.util.CollectionUtil.isNotEmpty(eventHandles)){
            for(com.fast.sd4324530.fastweixin.company.handle.QYEventHandle eventHandle : eventHandles){
                QYBaseRespMsg resultMsg = null;
                boolean result;
                try{
                    result = eventHandle.beforeHandle(event);
                }catch (Exception e){
                    result = false;
                }
                if(result){
                    resultMsg = eventHandle.handle(event);
                }
                if(com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull(resultMsg)){
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
    protected QYBaseRespMsg handleTextMsg(com.fast.sd4324530.fastweixin.company.message.req.QYTextReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理图片消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleImageMsg(com.fast.sd4324530.fastweixin.company.message.req.QYImageReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理语音消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleVoiceMsg(com.fast.sd4324530.fastweixin.company.message.req.QYVoiceReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理视频消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleVideoMsg(com.fast.sd4324530.fastweixin.company.message.req.QYVideoReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理小视频消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleShortVideoMsg(com.fast.sd4324530.fastweixin.company.message.req.QYVideoReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理地理位置消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleLocationMsg(com.fast.sd4324530.fastweixin.company.message.req.QYLocationReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理地理位置事件，有需要时子类重写
     *
     * @param event 地理位置事件对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleLocationEvent(com.fast.sd4324530.fastweixin.company.message.req.QYLocationEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单点击事件，有需要时子类重写
     *
     * @param event 菜单点击事件对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleMenuClickEvent(com.fast.sd4324530.fastweixin.company.message.req.QYMenuEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单跳转事件，有需要时子类重写
     *
     * @param event 菜单跳转事件对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleMenuViewEvent(com.fast.sd4324530.fastweixin.company.message.req.QYMenuEvent event){
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单扫描推事件，有需要时子类重写
     * @param event
     * @return
     */
    protected QYBaseRespMsg handleScanCodeEvent(com.fast.sd4324530.fastweixin.company.message.req.QYScanCodeEvent event){
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单弹出相册事件，有需要时子类重写
     *
     * @param event 菜单弹出相册事件
     * @return 响应的消息对象
     */
    protected QYBaseRespMsg handleSendPicsInfoEvent(com.fast.sd4324530.fastweixin.company.message.req.QYSendPicInfoEvent event){
        return handleDefaultEvent(event);
    }

    /**
     * 处理用户进入应用事件，有需要时子类重写
     *
     * @param event 添加应用事件对象
     * @return 响应的消息对象
     */
    protected QYBaseRespMsg handleEnterAgentEvent(com.fast.sd4324530.fastweixin.company.message.req.QYEnterAgentEvent event){
        return handleDefaultEvent(event);
    }

    /**
     * 处理异步任务通知事件，有需要时子类重写
     *
     * @param event 添加通知事件对象
     * @return 响应的消息对象
     */
    protected QYBaseRespMsg handleBatchJobEvent(com.fast.sd4324530.fastweixin.company.message.req.QYBatchJobEvent event){
        return handleDefaultEvent(event);
    }

    /**
     * 处理添加关注事件，有需要时子类重写
     *
     * @param event 添加关注事件对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleSubScribe(com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event){
        return new com.fast.sd4324530.fastweixin.company.message.resp.QYTextRespMsg("感谢您的关注");
    }

    /**
     * 处理取消关注事件，有需要时子类重写
     *
     * @param event 取消关注事件对象
     * @return 响应消息对象
     */
    protected QYBaseRespMsg handleUnsubscribe(com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event){
        return null;
    }

    protected QYBaseRespMsg handleDefaultMsg(com.fast.sd4324530.fastweixin.company.message.req.QYBaseReqMsg msg) {
        return null;
    }

    protected QYBaseRespMsg handleDefaultEvent(com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event) {
        return null;
    }

    private void buildBasicReqMsg(Map<String, Object> reqMap, com.fast.sd4324530.fastweixin.company.message.req.QYBaseReqMsg reqMsg) {
        addBasicReqParams(reqMap, reqMsg);
        reqMsg.setMsgId((String) reqMap.get("MsgId"));
    }

    private void buildBasicEvent(Map<String, Object> reqMap, com.fast.sd4324530.fastweixin.company.message.req.QYBaseEvent event) {
        addBasicReqParams(reqMap, event);
        event.setEvent((String) reqMap.get("Event"));
    }

    private void addBasicReqParams(Map<String, Object> reqMap, com.fast.sd4324530.fastweixin.company.message.req.QYBaseReq req) {
        req.setMsgType((String) reqMap.get("MsgType"));
        req.setFromUserName((String) reqMap.get("FromUserName"));
        req.setToUserName((String) reqMap.get("ToUserName"));
        req.setCreateTime(Long.parseLong((String) reqMap.get("CreateTime")));
        req.setAgentId((String) reqMap.get("AgentID"));
    }

}
