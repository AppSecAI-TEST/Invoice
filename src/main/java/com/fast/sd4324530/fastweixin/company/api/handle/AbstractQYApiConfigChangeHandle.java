package com.fast.sd4324530.fastweixin.company.api.handle;

import java.util.Observable;

/**
 *  
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  ====================================================================
 */
public abstract class AbstractQYApiConfigChangeHandle implements com.fast.sd4324530.fastweixin.handle.ApiConfigChangeHandle {

    public void update(Observable o, Object arg){
        if(com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull(arg) && arg instanceof com.fast.sd4324530.fastweixin.company.api.config.QYConfigChangeNotice){
            configChange((com.fast.sd4324530.fastweixin.company.api.config.QYConfigChangeNotice) arg);
        }
    }

    /**
     * 子类实现，当配置变化时触发该方法
     * @param notice 消息
     */
    public abstract void configChange(com.fast.sd4324530.fastweixin.company.api.config.QYConfigChangeNotice notice);
}
