package com.fast.sd4324530.fastweixin.api.entity;

/**
 * 抽象实体类
 *
 * @author peiyu
 */
public abstract class BaseModel implements Model {
    public String toJsonString() {
        return com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(this);
    }
}
