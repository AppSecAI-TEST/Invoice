package com.fast.sd4324530.fastweixin.company.api.response;/**
 * Created by Nottyjay on 2015/6/11.
 */

import com.alibaba.fastjson.annotation.JSONField;
import com.fast.sd4324530.fastweixin.api.response.BaseResponse;

import java.util.List;

/**
 * ====================================================================
 * 上海聚攒软件开发有限公司
 * --------------------------------------------------------------------
 *
 * @author Nottyjay
 * @version 1.0.beta
 *          ====================================================================
 */
public class GetQYUserInfo4DepartmentResponse extends BaseResponse {

    @JSONField(name = "userlist")
    public List<com.fast.sd4324530.fastweixin.company.api.entity.QYUser> userList;

    public List<com.fast.sd4324530.fastweixin.company.api.entity.QYUser> getUserList() {
        return userList;
    }

    public void setUserList(List<com.fast.sd4324530.fastweixin.company.api.entity.QYUser> userList) {
        this.userList = userList;
    }
}
