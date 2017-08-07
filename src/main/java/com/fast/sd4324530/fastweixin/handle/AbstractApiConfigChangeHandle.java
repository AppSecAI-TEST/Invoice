package com.fast.sd4324530.fastweixin.handle;

import java.util.Observable;

/**
 * 配置变化监听器抽象类
 *
 * @author peiyu
 */
public abstract class AbstractApiConfigChangeHandle implements ApiConfigChangeHandle {

    public void update(Observable o, Object arg) {
        if (com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull(arg) && arg instanceof com.fast.sd4324530.fastweixin.api.config.ConfigChangeNotice) {
            configChange((com.fast.sd4324530.fastweixin.api.config.ConfigChangeNotice) arg);
        }
    }

    /**
     * 子类实现，当配置变化时会触发该方法
     *
     * @param notice 通知对象
     */
    public abstract void configChange(com.fast.sd4324530.fastweixin.api.config.ConfigChangeNotice notice);
}
