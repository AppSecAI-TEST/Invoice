package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 */
public class GetUserShareResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.UserShare> list;

    public List<com.fast.sd4324530.fastweixin.api.entity.UserShare> getList() {
        return list;
    }

    public void setList(List<com.fast.sd4324530.fastweixin.api.entity.UserShare> list) {
        this.list = list;
    }
}
