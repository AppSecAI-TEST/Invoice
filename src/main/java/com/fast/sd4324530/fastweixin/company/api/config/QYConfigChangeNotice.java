package com.fast.sd4324530.fastweixin.company.api.config;

import java.util.Date;

/**
 *  
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  ====================================================================
 */
public class QYConfigChangeNotice extends com.fast.sd4324530.fastweixin.api.entity.BaseModel {

    private Date noticeTime;
    private String corpID;
    private com.fast.sd4324530.fastweixin.api.config.ChangeType type;
    private String value;

    public QYConfigChangeNotice() {
        this.noticeTime = new Date();
    }

    public QYConfigChangeNotice(String corpID, com.fast.sd4324530.fastweixin.api.config.ChangeType type, String value) {
        this();
        this.corpID = corpID;
        this.type = type;
        this.value = value;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getCorpID() {
        return corpID;
    }

    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }

    public com.fast.sd4324530.fastweixin.api.config.ChangeType getType() {
        return type;
    }

    public void setType(com.fast.sd4324530.fastweixin.api.config.ChangeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return "QYConfigChangeNotice{" +
                "noticeTime" + noticeTime +
                ", corpid='" + corpID + "'" +
                ", type=" + type +
                ", value='" + value + "'" +
                "}";
    }
}
