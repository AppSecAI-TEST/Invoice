package com.fast.sd4324530.fastweixin.company.api;

import com.fast.sd4324530.fastweixin.api.response.BaseResponse;
import com.fast.sd4324530.fastweixin.exception.WeixinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  标签管理
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  @since 1.3.6
 *  ====================================================================
 */
public class QYTagAPI extends QYBaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(QYTagAPI.class);

    /**
     * 构造方法，设置apiConfig
     *
     * @param config 微信API配置对象
     */
    public QYTagAPI(com.fast.sd4324530.fastweixin.company.api.config.QYAPIConfig config) {
        super(config);
    }

    /**
     * 创建新标签
     * @param tag 新标签信息。tagname必须，tagid不填则默认递增
     * @return 新的Tag信息
     */
    public com.fast.sd4324530.fastweixin.company.api.response.CreateTagResponse create(com.fast.sd4324530.fastweixin.company.api.entity.QYTag tag){
        com.fast.sd4324530.fastweixin.company.api.response.CreateTagResponse response;
        String url = BASE_API_URL + "cgi-bin/tag/create?access_token=#";
        if(com.fast.sd4324530.fastweixin.util.StrUtil.isBlank(tag.getTagname())){
            throw new WeixinException("标签名称必须填写!");
        }
        if(LOG.isDebugEnabled()){
            LOG.debug("Tag content: {}", tag.toJsonString());
        }
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("tagname", tag.getTagname());
        if(tag.getTagid() != null){
            params.put("tagid", tag.getTagid());
        }
        BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
        String jsonResult = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonResult, com.fast.sd4324530.fastweixin.company.api.response.CreateTagResponse.class);
        return response;
    }

    /**
     * 删除标签
     * @param tagid 准备删除的tagid。不可为空
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.company.api.enums.QYResultType delete(Integer tagid){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(tagid, "tagid不能为空！");
        String url = BASE_API_URL + "cgi-bin/tag/delete?access_token=#&tagid=" + tagid;
        BaseResponse response = executeGet(url);
        return com.fast.sd4324530.fastweixin.company.api.enums.QYResultType.get(response.getErrcode());
    }

    /**
     * 获取标签成员
     * @param tagid 目标标签的tagid。不可为空
     * @return 标签内信息
     */
    public com.fast.sd4324530.fastweixin.company.api.response.GetTagInfoResponse get(Integer tagid){
        com.fast.sd4324530.fastweixin.company.api.response.GetTagInfoResponse response;
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(tagid, "tagid不能为空！");
        String url = BASE_API_URL + "cgi-bin/tag/get?access_token=#&tagid=" + tagid;
        BaseResponse r = executeGet(url);
        String jsonResult = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonResult, com.fast.sd4324530.fastweixin.company.api.response.GetTagInfoResponse.class);
        return response;
    }

    /**
     * 增加标签成员。userlist与partylist非必须，但不能同时为空
     * @param tagid 目标标签id。必填
     * @param users 企业成员ID列表。单次请求长度不能超过1000
     * @param partys 企业部门ID列表。单次请求长度不能超过100
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.company.api.response.AddTagUsersResponse addTagUsers(Integer tagid, List<String> users, List<Integer> partys){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(tagid, "tagid不能为空！");
        if((users == null || users.size() == 0) && (partys == null || partys.size() == 0)){
            throw new WeixinException("userlist、partylist不能同时为空！");
        }
        if(users != null && users.size() > 1000){
            throw new WeixinException("userlist单次请求长度不能大于1000");
        }
        if(partys != null && partys.size() > 100){
            throw new WeixinException("partylist单次请求长度不能大于100");
        }
        com.fast.sd4324530.fastweixin.company.api.response.AddTagUsersResponse response;
        String url = BASE_API_URL + "cgi-bin/tag/addtagusers?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("tagid", tagid);
        params.put("userlist", users);
        params.put("partylist", partys);
        BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
        String jsonResult = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonResult, com.fast.sd4324530.fastweixin.company.api.response.AddTagUsersResponse.class);
        return response;
    }

    /**
     * 删除标签内成员。userlist与partylist非必须，但不能同时为空
     * @param tagid 目标标签id。必填
     * @param users 企业成员ID列表。
     * @param partys 企业部门ID列表
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.company.api.response.DelTagUsersResponse delTagUsers(Integer tagid, List<String> users, List<Integer> partys){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(tagid, "tagid不能为空！");
        if((users == null || users.size() == 0) && (partys == null || partys.size() == 0)){
            throw new WeixinException("userlist、partylist不能同时为空！");
        }
        com.fast.sd4324530.fastweixin.company.api.response.DelTagUsersResponse response;
        String url = BASE_API_URL + "cgi-bin/tag/deltagusers?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("tagid", tagid);
        params.put("userlist", users);
        params.put("partylist", partys);
        BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
        String jsonResult = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonResult, com.fast.sd4324530.fastweixin.company.api.response.DelTagUsersResponse.class);
        return response;
    }

    /**
     * 获取标签列表
     * @return 标签列表
     */
    public com.fast.sd4324530.fastweixin.company.api.response.GetTagListResponse list(){
        com.fast.sd4324530.fastweixin.company.api.response.GetTagListResponse response;
        String url = BASE_API_URL + "cgi-bin/tag/list?access_token=#";
        BaseResponse r = executeGet(url);
        String jsonResult = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonResult, com.fast.sd4324530.fastweixin.company.api.response.GetTagListResponse.class);
        return response;
    }
}
