package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 */
public class GetUpstreamMsgResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsg> list;

    public List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsg> getList() {
        return list;
    }

    public void setList(List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsg> list) {
        this.list = list;
    }
}
