package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 */
public class GetInterfaceSummaryResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.InterfaceSummary> list;

    public List<com.fast.sd4324530.fastweixin.api.entity.InterfaceSummary> getList() {
        return list;
    }

    public void setList(List<com.fast.sd4324530.fastweixin.api.entity.InterfaceSummary> list) {
        this.list = list;
    }
}
