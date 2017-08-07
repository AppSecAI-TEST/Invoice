package com.fast.sd4324530.fastweixin.api;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.fast.sd4324530.fastweixin.api.response.BaseResponse;

/**
 * 模版消息 api
 */
public class TemplateMsgAPI extends BaseAPI {
    private static final Logger LOG = LoggerFactory.getLogger(TemplateMsgAPI.class);

    public TemplateMsgAPI(com.fast.sd4324530.fastweixin.api.config.ApiConfig config) {
        super(config);
    }

    /**
     * 设置行业
     *
     * @param industry 行业参数
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.api.enums.ResultType setIndustry(com.fast.sd4324530.fastweixin.api.entity.Industry industry) {
        LOG.debug("设置行业......");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(industry, "行业对象为空");
        String url = BASE_API_URL + "cgi-bin/template/api_set_industry?access_token=#";
        BaseResponse response = executePost(url, industry.toJsonString());
        return com.fast.sd4324530.fastweixin.api.enums.ResultType.get(response.getErrcode());
    }

    /**
     * 添加模版
     *
     * @param shortTemplateId 模版短id
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.api.response.AddTemplateResponse addTemplate(String shortTemplateId) {
        LOG.debug("添加模版......");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(shortTemplateId, "短模版id必填");
        String url = BASE_API_URL + "cgi-bin/template/api_add_template?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("template_id_short", shortTemplateId);
        BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        com.fast.sd4324530.fastweixin.api.response.AddTemplateResponse result = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.AddTemplateResponse.class);
        return result;
    }

    /**
     * 发送模版消息
     *
     * @param msg 消息
     * @return 发送结果
     */
    public com.fast.sd4324530.fastweixin.api.response.SendTemplateResponse send(com.fast.sd4324530.fastweixin.api.entity.TemplateMsg msg) {
        LOG.debug("发送模版消息......");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(msg.getTouser(), "openid is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(msg.getTemplateId(), "template_id is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(msg.getData(), "data is null");
//        BeanUtil.requireNonNull(msg.getTopcolor(), "top color is null");
//        BeanUtil.requireNonNull(msg.getUrl(), "url is null");
        String url = BASE_API_URL + "cgi-bin/message/template/send?access_token=#";
        BaseResponse r = executePost(url, msg.toJsonString());
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        com.fast.sd4324530.fastweixin.api.response.SendTemplateResponse result = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.SendTemplateResponse.class);
        return result;
    }

    /**
     * 获取已添加至帐号下所有模板列表
     *
     * @return 所有模板
     */
    public com.fast.sd4324530.fastweixin.api.response.PrivateTemplate[] getAllPrivateTemplate() {
        LOG.debug("获取已添加至帐号下所有模板列表......");
        String url = BASE_API_URL + "cgi-bin/template/get_all_private_template?access_token=#";
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        com.fast.sd4324530.fastweixin.api.response.PrivateTemplate[] templates = JSONArray.toJavaObject((JSONArray) com.fast.sd4324530.fastweixin.util.JSONUtil.getJSONFromString(resultJson).get("template_list"), com.fast.sd4324530.fastweixin.api.response.PrivateTemplate[].class);
        return templates;
    }

    /**
     * 删除模板
     *
     * @param templateId 模板ID
     * @return 删除结果
     */
    public BaseResponse delTemplate(String templateId) {
        LOG.debug("删除模板......");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(templateId, "templateId is null");
        String url = BASE_API_URL + "cgi-bin/template/del_private_template?access_token=#";
        Map<String, String> map = new HashMap<String, String>();
        map.put("template_id", templateId);
        BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(map));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        return com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, BaseResponse.class);
    }

}
