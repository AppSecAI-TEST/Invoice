package com.fast.sd4324530.fastweixin.company.message;

/**
 *  
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  ====================================================================
 */
public class QYArticle extends com.fast.sd4324530.fastweixin.message.Article {

    private String picurl;

    public QYArticle(String title, String description, String picUrl, String url) {
        super(title, description, null, url);
        this.picurl = picUrl;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
