package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 */
public class GetUpstreamMsgHourResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsgHour> list;

    public List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsgHour> getList() {
        return list;
    }

    public void setList(List<com.fast.sd4324530.fastweixin.api.entity.UpstreamMsgHour> list) {
        this.list = list;
    }
}
