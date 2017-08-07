package com.fast.sd4324530.fastweixin.api.response;

/**
 * @author peiyu
 */
public class GetMenuResponse extends BaseResponse {

    private com.fast.sd4324530.fastweixin.api.entity.Menu menu;

    public com.fast.sd4324530.fastweixin.api.entity.Menu getMenu() {
        return menu;
    }

    public void setMenu(com.fast.sd4324530.fastweixin.api.entity.Menu menu) {
        this.menu = menu;
    }
}
