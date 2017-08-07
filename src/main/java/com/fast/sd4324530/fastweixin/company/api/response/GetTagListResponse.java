package com.fast.sd4324530.fastweixin.company.api.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fast.sd4324530.fastweixin.api.response.BaseResponse;

import java.util.List;

/**
 *  Response -- 获取标签列表
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  @since 1.3.6
 *  ====================================================================
 */
public class GetTagListResponse extends BaseResponse {

    @JSONField(name = "taglist")
    private List<com.fast.sd4324530.fastweixin.company.api.entity.QYTag> tags;

    public List<com.fast.sd4324530.fastweixin.company.api.entity.QYTag> getTags() {
        return tags;
    }

    public void setTags(List<com.fast.sd4324530.fastweixin.company.api.entity.QYTag> tags) {
        this.tags = tags;
    }
}
