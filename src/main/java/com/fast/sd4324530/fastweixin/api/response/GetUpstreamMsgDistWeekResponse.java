package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 */
public class GetUpstreamMsgDistWeekResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsgDistWeek> list;

    public List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsgDistWeek> getList() {
        return list;
    }

    public void setList(List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsgDistWeek> list) {
        this.list = list;
    }
}
