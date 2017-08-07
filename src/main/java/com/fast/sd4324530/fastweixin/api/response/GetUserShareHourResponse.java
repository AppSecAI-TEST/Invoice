package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 */
public class GetUserShareHourResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.UserShareHour> list;

    public List<com.fast.sd4324530.fastweixin.api.entity.UserShareHour> getList() {
        return list;
    }

    public void setList(List<com.fast.sd4324530.fastweixin.api.entity.UserShareHour> list) {
        this.list = list;
    }
}
