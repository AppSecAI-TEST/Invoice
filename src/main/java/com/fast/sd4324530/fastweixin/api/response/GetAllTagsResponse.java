package com.fast.sd4324530.fastweixin.api.response;

import java.util.List;

/**
 * @author peiyu
 * @since 1.3.9
 */
public class GetAllTagsResponse extends BaseResponse {

    private List<com.fast.sd4324530.fastweixin.api.entity.Tag> tags;

    public List<com.fast.sd4324530.fastweixin.api.entity.Tag> getTags() {
        return tags;
    }

    public void setTags(List<com.fast.sd4324530.fastweixin.api.entity.Tag> tags) {
        this.tags = tags;
    }
}
