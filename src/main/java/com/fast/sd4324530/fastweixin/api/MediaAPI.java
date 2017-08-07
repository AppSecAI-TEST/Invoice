package com.fast.sd4324530.fastweixin.api;

import com.fast.sd4324530.fastweixin.util.NetWorkCenter;
import com.fast.sd4324530.fastweixin.util.StreamUtil;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多媒体资源API
 *
 * @author peiyu
 */
public class MediaAPI extends BaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(MediaAPI.class);

    public MediaAPI(com.fast.sd4324530.fastweixin.api.config.ApiConfig config) {
        super(config);
    }

    /**
     * 上传资源，会在微信服务器上保存3天，之后会被删除
     *
     * @param type 资源类型
     * @param file 需要上传的文件
     * @return 响应对象
     */
    public com.fast.sd4324530.fastweixin.api.response.UploadMediaResponse uploadMedia(com.fast.sd4324530.fastweixin.api.enums.MediaType type, File file) {
        com.fast.sd4324530.fastweixin.api.response.UploadMediaResponse response;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=#&type=" + type.toString();
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, null, file);
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(r.getErrmsg(), com.fast.sd4324530.fastweixin.api.response.UploadMediaResponse.class);
        return response;
    }

    /**
     * 上传群发文章素材。
     *
     * @param articles 上传的文章信息
     * @return 响应对象
     */
    public com.fast.sd4324530.fastweixin.api.response.UploadMediaResponse uploadNews(List<com.fast.sd4324530.fastweixin.api.entity.Article> articles){
        com.fast.sd4324530.fastweixin.api.response.UploadMediaResponse response;
        String url = BASE_API_URL + "cgi-bin/media/uploadnews?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("articles", articles);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(params));
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(r.getErrmsg(), com.fast.sd4324530.fastweixin.api.response.UploadMediaResponse.class);
        return response;
    }

    /**
     * 上传群发消息图片素材
     * @param file 上传的图片
     * @return 上传结果
     */
    public com.fast.sd4324530.fastweixin.api.response.UploadImgResponse uploadImg(File file){
        com.fast.sd4324530.fastweixin.api.response.UploadImgResponse response;
        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=#";
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, null, file);
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(r.getErrmsg(), com.fast.sd4324530.fastweixin.api.response.UploadImgResponse.class);
        return response;
    }

    /**
     * 下载资源，实现的很不好，反正能用了。搞的晕了，之后会优化
     *
     * @param mediaId 微信提供的资源唯一标识
     * @return 响应对象
     */
    public com.fast.sd4324530.fastweixin.api.response.DownloadMediaResponse downloadMedia(String mediaId) {
        com.fast.sd4324530.fastweixin.api.response.DownloadMediaResponse response = new com.fast.sd4324530.fastweixin.api.response.DownloadMediaResponse();
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + this.config.getAccessToken() + "&media_id=" + mediaId;
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(NetWorkCenter.CONNECT_TIMEOUT).setConnectTimeout(NetWorkCenter.CONNECT_TIMEOUT).setSocketTimeout(NetWorkCenter.CONNECT_TIMEOUT).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse r = client.execute(get);
            if (HttpStatus.SC_OK == r.getStatusLine().getStatusCode()) {
                InputStream inputStream = r.getEntity().getContent();
                Header[] headers = r.getHeaders("Content-disposition");
                if (null != headers && 0 != headers.length) {
                    Header length = r.getHeaders("Content-Length")[0];
                    response.setContent(inputStream, Integer.valueOf(length.getValue()));
                    response.setFileName(headers[0].getElements()[0].getParameterByName("filename").getValue());
                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    StreamUtil.copy(inputStream, out);
                    String json = out.toString();
                    response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(json, com.fast.sd4324530.fastweixin.api.response.DownloadMediaResponse.class);
                }
            }
        } catch (IOException e) {
            LOG.error("IO处理异常", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                LOG.error("异常", e);
            }
        }
        return response;
    }

}
