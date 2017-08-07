package com.fast.sd4324530.fastweixin.api;

import com.fast.sd4324530.fastweixin.api.config.ApiConfig;
import com.fast.sd4324530.fastweixin.api.enums.ResultType;
import com.fast.sd4324530.fastweixin.api.response.BaseResponse;
import com.fast.sd4324530.fastweixin.api.response.GetSendMessageResponse;
import com.fast.sd4324530.fastweixin.util.BeanUtil;
import com.fast.sd4324530.fastweixin.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息相关API
 *
 * @author peiyu, Nottyjay
 * @since 1.3
 */
public class MessageAPI extends BaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(MessageAPI.class);

    public MessageAPI(ApiConfig config) {
        super(config);
    }

    /**
     * 群发消息给用户。
     * 本方法调用需要账户为微信已认证账户
     * @param message 消息主体
     * @param isToAll 是否发送给全部用户。false时需要填写groupId，true时可忽略groupId树形
     * @param groupId 群组ID
     * @param openIds 群发用户
     * @return 群发结果
     * @deprecated 微信不再建议使用群组概念,用标签代替
     */
    @Deprecated
    public GetSendMessageResponse sendMessageToUser(com.fast.sd4324530.fastweixin.message.BaseMsg message, boolean isToAll, String groupId, String[] openIds){
        BeanUtil.requireNonNull(message, "message is null");
        LOG.debug("群发消息......");
        String url = BASE_API_URL + "cgi-bin/message/mass/sendall?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("is_to_all", isToAll);
        if(!isToAll){
            BeanUtil.requireNonNull(groupId, "groupId is null");
            filterMap.put("group_id", groupId);
        }
        params.put("filter", filterMap);
        if(message instanceof com.fast.sd4324530.fastweixin.message.MpNewsMsg){
            params.put("msgtype", "mpnews");
            com.fast.sd4324530.fastweixin.message.MpNewsMsg msg = (com.fast.sd4324530.fastweixin.message.MpNewsMsg)message;
            Map<String, Object> mpNews = new HashMap<String, Object>();
            mpNews.put("media_id", msg.getMediaId());
            params.put("mpnews", mpNews);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.TextMsg){
            params.put("msgtype", "text");
            com.fast.sd4324530.fastweixin.message.TextMsg msg = (com.fast.sd4324530.fastweixin.message.TextMsg)message;
            Map<String ,Object> text = new HashMap<String, Object>();
            text.put("content", msg.getContent());
            params.put("text", text);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.VoiceMsg){
            params.put("msgtype", "voice");
            com.fast.sd4324530.fastweixin.message.VoiceMsg msg = (com.fast.sd4324530.fastweixin.message.VoiceMsg)message;
            Map<String, Object> voice = new HashMap<String ,Object>();
            voice.put("media_id", msg.getMediaId());
            params.put("voice", voice);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.ImageMsg){
            params.put("msgtype", "image");
            com.fast.sd4324530.fastweixin.message.ImageMsg msg = (com.fast.sd4324530.fastweixin.message.ImageMsg)message;
            Map<String, Object> image = new HashMap<String, Object>();
            image.put("media_id", msg.getMediaId());
            params.put("image", image);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.VideoMsg){
            // TODO 此处方法特别
        }
        BaseResponse response = executePost(url, JSONUtil.toJson(params));
        String resultJson = isSuccess(response.getErrcode()) ? response.getErrmsg() : response.toJsonString();
        return JSONUtil.toBean(resultJson, GetSendMessageResponse.class);
    }

    /**
     * 群发消息给用户。
     * 本方法调用需要账户为微信已认证账户
     * @param message 消息主体
     * @param isToAll 是否发送给全部用户。false时需要填写tagId，true时可忽略tagId树形
     * @param tagId 标签ID
     * @return 群发结果
     */
    public GetSendMessageResponse sendMessageToUser(com.fast.sd4324530.fastweixin.message.BaseMsg message, boolean isToAll, Integer tagId){
        BeanUtil.requireNonNull(message, "message is null");
        LOG.debug("群发消息......");
        String url = BASE_API_URL + "cgi-bin/message/mass/sendall?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("is_to_all", isToAll);
        if(!isToAll){
            BeanUtil.requireNonNull(tagId, "tagId is null");
            filterMap.put("tag_id", tagId);
        }
        params.put("filter", filterMap);
        if(message instanceof com.fast.sd4324530.fastweixin.message.MpNewsMsg){
            params.put("msgtype", "mpnews");
            com.fast.sd4324530.fastweixin.message.MpNewsMsg msg = (com.fast.sd4324530.fastweixin.message.MpNewsMsg)message;
            Map<String, Object> mpNews = new HashMap<String, Object>();
            mpNews.put("media_id", msg.getMediaId());
            params.put("mpnews", mpNews);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.TextMsg){
            params.put("msgtype", "text");
            com.fast.sd4324530.fastweixin.message.TextMsg msg = (com.fast.sd4324530.fastweixin.message.TextMsg)message;
            Map<String ,Object> text = new HashMap<String, Object>();
            text.put("content", msg.getContent());
            params.put("text", text);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.VoiceMsg){
            params.put("msgtype", "voice");
            com.fast.sd4324530.fastweixin.message.VoiceMsg msg = (com.fast.sd4324530.fastweixin.message.VoiceMsg)message;
            Map<String, Object> voice = new HashMap<String ,Object>();
            voice.put("media_id", msg.getMediaId());
            params.put("voice", voice);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.ImageMsg){
            params.put("msgtype", "image");
            com.fast.sd4324530.fastweixin.message.ImageMsg msg = (com.fast.sd4324530.fastweixin.message.ImageMsg)message;
            Map<String, Object> image = new HashMap<String, Object>();
            image.put("media_id", msg.getMediaId());
            params.put("image", image);
        }else if(message instanceof com.fast.sd4324530.fastweixin.message.VideoMsg){
            // TODO 此处方法特别
        }
        BaseResponse response = executePost(url, JSONUtil.toJson(params));
        String resultJson = isSuccess(response.getErrcode()) ? response.getErrmsg() : response.toJsonString();
        return JSONUtil.toBean(resultJson, GetSendMessageResponse.class);
    }

    /**
     * 发布客服消息
     *
     * @param openid  关注者ID
     * @param message 消息对象，支持各种消息类型
     * @return 调用结果
     * @deprecated 此方法已经不再建议使用，使用CustomAPI中方法代替
     */
    @Deprecated
    public ResultType sendCustomMessage(String openid, com.fast.sd4324530.fastweixin.message.BaseMsg message) {
        BeanUtil.requireNonNull(openid, "openid is null");
        BeanUtil.requireNonNull(message, "message is null");
        LOG.debug("发布客服消息......");
        String url = BASE_API_URL + "cgi-bin/message/custom/send?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("touser", openid);
        if (message instanceof com.fast.sd4324530.fastweixin.message.TextMsg) {
            com.fast.sd4324530.fastweixin.message.TextMsg msg = (com.fast.sd4324530.fastweixin.message.TextMsg) message;
            params.put("msgtype", "text");
            Map<String, String> text = new HashMap<String, String>();
            text.put("content", msg.getContent());
            params.put("text", text);
        } else if (message instanceof com.fast.sd4324530.fastweixin.message.ImageMsg) {
            com.fast.sd4324530.fastweixin.message.ImageMsg msg = (com.fast.sd4324530.fastweixin.message.ImageMsg) message;
            params.put("msgtype", "image");
            Map<String, String> image = new HashMap<String, String>();
            image.put("media_id", msg.getMediaId());
            params.put("image", image);
        } else if (message instanceof com.fast.sd4324530.fastweixin.message.VoiceMsg) {
            com.fast.sd4324530.fastweixin.message.VoiceMsg msg = (com.fast.sd4324530.fastweixin.message.VoiceMsg) message;
            params.put("msgtype", "voice");
            Map<String, String> voice = new HashMap<String, String>();
            voice.put("media_id", msg.getMediaId());
            params.put("voice", voice);
        } else if (message instanceof com.fast.sd4324530.fastweixin.message.VideoMsg) {
            com.fast.sd4324530.fastweixin.message.VideoMsg msg = (com.fast.sd4324530.fastweixin.message.VideoMsg) message;
            params.put("msgtype", "video");
            Map<String, String> video = new HashMap<String, String>();
            video.put("media_id", msg.getMediaId());
            video.put("thumb_media_id", msg.getMediaId());
            video.put("title", msg.getTitle());
            video.put("description", msg.getDescription());
            params.put("video", video);
        } else if (message instanceof com.fast.sd4324530.fastweixin.message.MusicMsg) {
            com.fast.sd4324530.fastweixin.message.MusicMsg msg = (com.fast.sd4324530.fastweixin.message.MusicMsg) message;
            params.put("msgtype", "music");
            Map<String, String> music = new HashMap<String, String>();
            music.put("thumb_media_id", msg.getThumbMediaId());
            music.put("title", msg.getTitle());
            music.put("description", msg.getDescription());
            music.put("musicurl", msg.getMusicUrl());
            music.put("hqmusicurl", msg.getHqMusicUrl());
            params.put("music", music);
        } else if (message instanceof com.fast.sd4324530.fastweixin.message.NewsMsg) {
            com.fast.sd4324530.fastweixin.message.NewsMsg msg = (com.fast.sd4324530.fastweixin.message.NewsMsg) message;
            params.put("msgtype", "news");
            Map<String, Object> news = new HashMap<String, Object>();
            List<Object> articles = new ArrayList<Object>();
            List<com.fast.sd4324530.fastweixin.message.Article> list = msg.getArticles();
            for (com.fast.sd4324530.fastweixin.message.Article article : list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("title", article.getTitle());
                map.put("description", article.getDescription());
                map.put("url", article.getUrl());
                map.put("picurl", article.getPicUrl());
                articles.add(map);
            }
            news.put("articles", articles);
            params.put("news", news);
        }
        BaseResponse response = executePost(url, JSONUtil.toJson(params));
        return ResultType.get(response.getErrcode());
    }
}
