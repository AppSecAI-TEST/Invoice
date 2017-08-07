package com.fast.sd4324530.fastweixin.company.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.fast.sd4324530.fastweixin.api.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *  自定义菜单管理
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  @since 1.3.6
 *  ====================================================================
 */
public class QYMenuAPI extends QYBaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(QYMenuAPI.class);

    /**
     * 构造方法，设置apiConfig
     *
     * @param config 微信API配置对象
     */
    public QYMenuAPI(com.fast.sd4324530.fastweixin.company.api.config.QYAPIConfig config) {
        super(config);
    }

    /**
     * 创建自定义菜单。
     * @param menu 自定义菜单
     * @param agentId 需要生成菜单的应用ID
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.company.api.enums.QYResultType create(com.fast.sd4324530.fastweixin.company.api.entity.QYMenu menu, String agentId){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(menu, "菜单不能为空！");
        String url = BASE_API_URL + "cgi-bin/menu/create?access_token=#&agentid=" + agentId;
        BaseResponse response = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(menu));
        return com.fast.sd4324530.fastweixin.company.api.enums.QYResultType.get(response.getErrcode());
    }

    /**
     * 获取菜单列表。
     * @param agentId 目标应用的ID
     * @return QYMenu。与创建菜单时的对象一致。
     */
    public com.fast.sd4324530.fastweixin.company.api.response.GetQYMenuResponse list(String agentId){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(agentId, "应用ID不能为空");
        com.fast.sd4324530.fastweixin.company.api.response.GetQYMenuResponse response;
        String url = BASE_API_URL + "cgi-bin/menu/get?access_token=#&agentid=" + agentId;
        BaseResponse r = executeGet(url);
        if (isSuccess(r.getErrcode())) {
            JSONObject jsonObject = com.fast.sd4324530.fastweixin.util.JSONUtil.getJSONFromString(r.getErrmsg());
            //通过jsonpath不断修改type的值，才能正常解析- -
            List buttonList = (List) JSONPath.eval(jsonObject, "$.menu.button");
            if (com.fast.sd4324530.fastweixin.util.CollectionUtil.isNotEmpty(buttonList)) {
                for (Object button : buttonList) {
                    List subList = (List) JSONPath.eval(button, "$.sub_button");
                    if (com.fast.sd4324530.fastweixin.util.CollectionUtil.isNotEmpty(subList)) {
                        for (Object sub : subList) {
                            Object type = JSONPath.eval(sub, "$.type");
                            JSONPath.set(sub, "$.type", type.toString().toUpperCase());
                        }
                    }else{
                        Object type = JSONPath.eval(button, "$.type");
                        JSONPath.set(button, "$.type", type.toString().toUpperCase());
                    }
                }
            }
            response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonObject.toJSONString(), com.fast.sd4324530.fastweixin.company.api.response.GetQYMenuResponse.class);
        } else {
            response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(r.toJsonString(), com.fast.sd4324530.fastweixin.company.api.response.GetQYMenuResponse.class);
        }
        return response;
    }

    /**
     * 删除自定义菜单
     * @param agentId 目标应用ID
     * @return 操作结果
     */
    public com.fast.sd4324530.fastweixin.company.api.enums.QYResultType delete(String agentId){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(agentId, "AgentId不能为空");
        String url = BASE_API_URL + "cgi-bin/menu/delete?access_token=#&agentid=" + agentId;
        BaseResponse response = executeGet(url);
        return com.fast.sd4324530.fastweixin.company.api.enums.QYResultType.get(response.getErrcode());
    }
}
