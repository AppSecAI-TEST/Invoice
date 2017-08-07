package com.fast.sd4324530.fastweixin.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据分析API
 *
 * @author peiyu
 */
public class DataCubeAPI extends BaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(DataCubeAPI.class);

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public DataCubeAPI(com.fast.sd4324530.fastweixin.api.config.ApiConfig config) {
        super(config);
    }

    /**
     * 获取用户增减数据，最大跨度为7天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 用户增减数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUserSummaryResponse getUserSummary(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUserSummaryResponse response = null;
        String url = BASE_API_URL + "datacube/getusersummary?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUserSummaryResponse.class);
        return response;
    }

    /**
     * 获取累计用户数据，最大跨度为7天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 用户增减数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUserCumulateResponse getUserCumulate(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUserCumulateResponse response = null;
        String url = BASE_API_URL + "datacube/getusercumulate?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUserCumulateResponse.class);
        return response;
    }

    /**
     * 获取图文群发每日数据
     *
     * @param day 查询日期
     * @return 图文群发每日数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetArticleSummaryResponse getArticleSummary(Date day) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(day, "day is null");
        com.fast.sd4324530.fastweixin.api.response.GetArticleSummaryResponse response = null;
        String url = BASE_API_URL + "datacube/getarticlesummary?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(day));
        param.put("end_date", DATE_FORMAT.format(day));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetArticleSummaryResponse.class);
        return response;
    }

    /**
     * 获取图文群发总数据
     *
     * @param day 查询日期
     * @return 图文群发总数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetArticleTotalResponse getArticleTotal(Date day) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(day, "day is null");
        com.fast.sd4324530.fastweixin.api.response.GetArticleTotalResponse response = null;
        String url = BASE_API_URL + "datacube/getarticletotal?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(day));
        param.put("end_date", DATE_FORMAT.format(day));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetArticleTotalResponse.class);
        return response;
    }

    /**
     * 获取图文统计数据，最大跨度为3天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 图文统计数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUserReadResponse getUserRead(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUserReadResponse response = null;
        String url = BASE_API_URL + "datacube/getuserread?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUserReadResponse.class);
        return response;
    }

    /**
     * 获取图文统计分时数据
     *
     * @param day 查询日期
     * @return 图文统计分时数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUserReadHourResponse getUserReadHour(Date day) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(day, "day is null");
        com.fast.sd4324530.fastweixin.api.response.GetUserReadHourResponse response = null;
        String url = BASE_API_URL + "datacube/getuserreadhour?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(day));
        param.put("end_date", DATE_FORMAT.format(day));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUserReadHourResponse.class);
        return response;
    }

    /**
     * 获取图文分享转发数据，最大跨度为7天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 图文分享转发数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUserShareResponse getUserShare(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUserShareResponse response = null;
        String url = BASE_API_URL + "datacube/getusershare?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUserShareResponse.class);
        return response;
    }

    /**
     * 获取图文分享转发分时数据
     *
     * @param day 查询日期
     * @return 图文分享转发分时数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUserShareHourResponse getUserShareHour(Date day) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(day, "day is null");
        com.fast.sd4324530.fastweixin.api.response.GetUserShareHourResponse response = null;
        String url = BASE_API_URL + "datacube/getusersharehour?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(day));
        param.put("end_date", DATE_FORMAT.format(day));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUserShareHourResponse.class);
        return response;
    }

    /**
     * 获取消息发送概况数据，最大跨度为7天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 消息发送概况数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgResponse getUpstreamMsg(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsg?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgResponse.class);
        return response;
    }

    /**
     * 获取消息分送分时数据
     *
     * @param day 查询日期
     * @return 消息分送分时数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgHourResponse getUpstreamMsgHour(Date day) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(day, "day is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgHourResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsghour?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(day));
        param.put("end_date", DATE_FORMAT.format(day));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgHourResponse.class);
        return response;
    }

    /**
     * 获取消息发送周数据，最大跨度为30天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 消息发送周数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgWeekResponse getUpstreamMsgWeek(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgWeekResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsgweek?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgWeekResponse.class);
        return response;
    }

    /**
     * 获取消息发送月数据，最大跨度为30天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 消息发送月数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgMonthResponse getUpstreamMsgMonth(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgMonthResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsgmonth?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgMonthResponse.class);
        return response;
    }

    /**
     * 获取消息发送分布数据，最大跨度为15天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 消息发送分布数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistResponse getUpstreamMsgDist(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsgdist?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistResponse.class);
        return response;
    }

    /**
     * 获取消息发送分布周数据，最大跨度为30天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 消息发送分布周数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistWeekResponse getUpstreamMsgDistWeek(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistWeekResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsgdistweek?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistWeekResponse.class);
        return response;
    }

    /**
     * 获取消息发送分布月数据，最大跨度为30天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 消息发送分布月数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistMonthResponse getUpstreamMsgDistMonth(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistMonthResponse response = null;
        String url = BASE_API_URL + "datacube/getupstreammsgdistmonth?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetUpstreamMsgDistMonthResponse.class);
        return response;
    }

    /**
     * 获取接口分析数据，最大跨度为30天
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 接口分析数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetInterfaceSummaryResponse getInterfaceSummary(Date beginDate, Date endDate) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(beginDate, "beginDate is null");
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(endDate, "endDate is null");
        com.fast.sd4324530.fastweixin.api.response.GetInterfaceSummaryResponse response = null;
        String url = BASE_API_URL + "datacube/getinterfacesummary?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(beginDate));
        param.put("end_date", DATE_FORMAT.format(endDate));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetInterfaceSummaryResponse.class);
        return response;
    }

    /**
     * 获取接口分析分时数据
     *
     * @param day 查询日期
     * @return 接口分析分时数据
     */
    public com.fast.sd4324530.fastweixin.api.response.GetInterfaceSummaryHourResponse getInterfaceSummaryHour(Date day) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(day, "day is null");
        com.fast.sd4324530.fastweixin.api.response.GetInterfaceSummaryHourResponse response = null;
        String url = BASE_API_URL + "datacube/getinterfacesummaryhour?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin_date", DATE_FORMAT.format(day));
        param.put("end_date", DATE_FORMAT.format(day));
        String json = com.fast.sd4324530.fastweixin.util.JSONUtil.toJson(param);
        com.fast.sd4324530.fastweixin.api.response.BaseResponse r = executePost(url, json);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.api.response.GetInterfaceSummaryHourResponse.class);
        return response;
    }
}
