package com.fast.sd4324530.fastweixin.company.message.resp;

import java.util.ArrayList;
import java.util.List;

/**
 *  微信企业号被动响应事件新闻消息
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  @since 1.3.6
 *  ====================================================================
 */
public class QYNewsRespMsg extends QYBaseRespMsg {

    private static final int WX_MAX_SIZE = 10;
    private              int maxSize     = WX_MAX_SIZE;

    private List<com.fast.sd4324530.fastweixin.company.message.QYArticle> articles;

    public QYNewsRespMsg() {
        articles = new ArrayList<com.fast.sd4324530.fastweixin.company.message.QYArticle>(maxSize);
    }

    public QYNewsRespMsg(int maxSize) {
        setMaxSize(maxSize);
        articles = new ArrayList<com.fast.sd4324530.fastweixin.company.message.QYArticle>(maxSize);
    }

    public QYNewsRespMsg(List<com.fast.sd4324530.fastweixin.company.message.QYArticle> articles) {
        this.articles = articles;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        if (maxSize < WX_MAX_SIZE && maxSize >= 1) {
            this.maxSize = maxSize;
        }
        if (articles != null && articles.size() > this.maxSize) {
            articles = articles.subList(0, this.maxSize);
        }
    }

    public List<com.fast.sd4324530.fastweixin.company.message.QYArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<com.fast.sd4324530.fastweixin.company.message.QYArticle> articles) {
        if (articles.size() > this.maxSize) {
            this.articles = articles.subList(0, this.maxSize);
        } else {
            this.articles = articles;
        }
    }

    public void add(String title) {
        add(title, null, null, null);
    }

    public void add(String title, String url) {
        add(title, null, null, url);
    }

    public void add(String title, String picUrl, String url) {
        add(title, null, picUrl, url);
    }

    public void add(String title, String description, String picUrl, String url) {
        add(new com.fast.sd4324530.fastweixin.company.message.QYArticle(title, description, picUrl, url));
    }

    public void add(com.fast.sd4324530.fastweixin.company.message.QYArticle article){
        if (this.articles.size() < maxSize) {
            this.articles.add(article);
        }
    }

    @Override
    public String toXml() {
        com.fast.sd4324530.fastweixin.message.util.MessageBuilder mb = new com.fast.sd4324530.fastweixin.message.util.MessageBuilder(super.toXml());
        mb.addData("MsgType", com.fast.sd4324530.fastweixin.message.RespType.NEWS);
        mb.addTag("ArticleCount", String.valueOf(articles.size()));
        mb.append("<Articles>\n");
        for (com.fast.sd4324530.fastweixin.message.Article article : articles) {
            mb.append(article.toXml());
        }
        mb.append("</Articles>\n");
        mb.surroundWith("xml");
        return mb.toString();
    }
}
