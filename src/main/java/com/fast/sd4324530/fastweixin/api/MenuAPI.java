package com.fast.sd4324530.fastweixin.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.fast.sd4324530.fastweixin.api.response.BaseResponse;
import com.fast.sd4324530.fastweixin.api.response.GetMenuResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单相关API
 * 1.3.7支持个性化菜单
 *
 * @author peiyu
 * @since 1.2
 */
public class MenuAPI extends BaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(MenuAPI.class);

    public MenuAPI(com.fast.sd4324530.fastweixin.api.config.ApiConfig config) {
        super(config);
    }

    /**
     * 创建菜单
     * 1.3.7开始支持个性化菜单
     *
     * @param menu 菜单对象
     * @return 调用结果
     */
    public com.fast.sd4324530.fastweixin.api.enums.ResultType createMenu(com.fast.sd4324530.fastweixin.api.entity.Menu menu) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(menu, "menu is null");
        String url = BASE_API_URL;
        if (com.fast.sd4324530.fastweixin.util.BeanUtil.isNull(menu.getMatchrule())) {
            //普通菜单
            LOG.debug("创建普通菜单.....");
            url += "cgi-bin/menu/create?access_token=#";
        } else {
            //个性化菜单
            LOG.debug("创建个性化菜单.....");
            url += "cgi-bin/menu/addconditional?access_token=#";
        }
        BaseResponse response = executePost(url, menu.toJsonString());
        return com.fast.sd4324530.fastweixin.api.enums.ResultType.get(response.getErrcode());
    }

    /**
     * 获取所有菜单
     *
     * @return 菜单列表对象
     */
    public GetMenuResponse getMenu() {
        GetMenuResponse response;
        LOG.debug("获取菜单信息.....");
        String url = BASE_API_URL + "cgi-bin/menu/get?access_token=#";

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
                    } else {
                        Object type = JSONPath.eval(button, "$.type");
                        JSONPath.set(button, "$.type", type.toString().toUpperCase());
                    }
                }
            }
            response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonObject.toJSONString(), GetMenuResponse.class);
        } else {
            response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(r.toJsonString(), GetMenuResponse.class);
        }
        return response;
    }

    /**
     * 删除所有菜单，包括个性化菜单
     *
     * @return 调用结果
     */
    public com.fast.sd4324530.fastweixin.api.enums.ResultType deleteMenu() {
        LOG.debug("删除菜单.....");
        String url = BASE_API_URL + "cgi-bin/menu/delete?access_token=#";
        BaseResponse response = executeGet(url);
        return com.fast.sd4324530.fastweixin.api.enums.ResultType.get(response.getErrcode());
    }

    /**
     * 删除个性化菜单
     *
     * @param menuId 个性化菜单ID
     * @return 调用结果
     * @since 1.3.7
     */
    public com.fast.sd4324530.fastweixin.api.enums.ResultType deleteConditionalMenu(String menuId) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(menuId, "menuid is null");
        LOG.debug("删除个性化菜单.....");
        String url = BASE_API_URL + "cgi-bin/menu/delconditional?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("menuid", menuId);
        BaseResponse response = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
        return com.fast.sd4324530.fastweixin.api.enums.ResultType.get(response.getErrcode());
    }

    /**
     * 测试个性化菜单
     *
     * @param userId 可以是粉丝的OpenID，也可以是粉丝的微信号
     * @return 该用户可以看到的菜单
     * @since 1.3.7
     */
    public GetMenuResponse tryMatchMenu(String userId) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(userId, "userId is null");
        LOG.debug("测试个性化菜单.....");
        GetMenuResponse response;
        String url = BASE_API_URL + "cgi-bin/menu/trymatch?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
//        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
//        response = JSONUtil.toBean(resultJson, GetMenuResponse.class);
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
                    } else {
                        Object type = JSONPath.eval(button, "$.type");
                        JSONPath.set(button, "$.type", type.toString().toUpperCase());
                    }
                }
            }
            response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(jsonObject.toJSONString(), GetMenuResponse.class);
        } else {
            response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(r.toJsonString(), GetMenuResponse.class);
        }
        return response;
    }
}
