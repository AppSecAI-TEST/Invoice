package com.fast.sd4324530.fastweixin.api.response;

/**
 * 微信API响应报文对象基类
 *
 * @author peiyu
 */
public class BaseResponse extends com.fast.sd4324530.fastweixin.api.entity.BaseModel {

    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        String result = this.errmsg;
        //将接口返回的错误信息转换成中文，方便提示用户出错原因
        if (com.fast.sd4324530.fastweixin.util.StrUtil.isNotBlank(this.errcode) && !com.fast.sd4324530.fastweixin.api.enums.ResultType.SUCCESS.getCode().toString().equals(this.errcode)) {
            com.fast.sd4324530.fastweixin.api.enums.ResultType resultType = com.fast.sd4324530.fastweixin.api.enums.ResultType.get(this.errcode);
            if(com.fast.sd4324530.fastweixin.util.BeanUtil.nonNull(resultType)) {
                result = resultType.getDescription();
            }
        }
        return result;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
