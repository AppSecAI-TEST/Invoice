package com.fast.sd4324530.fastweixin.api.response;

/**
 * @author peiyu
 * @since 1.3.9
 */
public class CreateTagResponse extends BaseResponse {

    private com.fast.sd4324530.fastweixin.api.entity.Tag tag;

    public com.fast.sd4324530.fastweixin.api.entity.Tag getTag() {
        return tag;
    }

    public void setTag(com.fast.sd4324530.fastweixin.api.entity.Tag tag) {
        this.tag = tag;
    }
}
