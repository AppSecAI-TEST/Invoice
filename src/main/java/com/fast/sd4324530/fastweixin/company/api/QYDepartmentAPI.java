package com.fast.sd4324530.fastweixin.company.api;

import com.fast.sd4324530.fastweixin.api.response.BaseResponse;
import com.fast.sd4324530.fastweixin.company.api.enums.QYResultType;

/**
 *  
 *  ====================================================================
 *  上海聚攒软件开发有限公司
 *  --------------------------------------------------------------------
 *  @author Nottyjay
 *  @version 1.0.beta
 *  ====================================================================
 */
public class QYDepartmentAPI extends QYBaseAPI {

    /**
     * 构造方法，设置apiConfig
     *
     * @param config 微信API配置对象
     */
    public QYDepartmentAPI(com.fast.sd4324530.fastweixin.company.api.config.QYAPIConfig config) {
        super(config);
    }

    /**
     * 获取部门列表
     * @param parentId 父级部门ID。可不填，不填表示顶级部门
     * @return 部门列表
     */
    public com.fast.sd4324530.fastweixin.company.api.response.GetDepartmentListResponse getList(Integer parentId){
        com.fast.sd4324530.fastweixin.company.api.response.GetDepartmentListResponse response;
        String url;
        if(parentId != null) {
            url = BASE_API_URL + "cgi-bin/department/list?access_token=#&id=" + parentId;
        }else{
            url = BASE_API_URL + "cgi-bin/department/list?access_token=#";
        }
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.company.api.response.GetDepartmentListResponse.class);
        return response;
    }

    /**
     * 创建部门
     * @param department 部门信息
     * @return 部门信息
     */
    public com.fast.sd4324530.fastweixin.company.api.response.CreateDepartmentResponse create(com.fast.sd4324530.fastweixin.company.api.entity.QYDepartment department) {
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(department, "department is null");
        com.fast.sd4324530.fastweixin.company.api.response.CreateDepartmentResponse response;
        String url = BASE_API_URL + "cgi-bin/department/create?access_token=#";
        BaseResponse r = executePost(url, department.toJsonString());
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : r.toJsonString();
        response = com.fast.sd4324530.fastweixin.util.JSONUtil.toBean(resultJson, com.fast.sd4324530.fastweixin.company.api.response.CreateDepartmentResponse.class);
        return response;
    }

    /**
     * 更新部门
     * @param department 需要更新的部门
     * @return 更新结果
     */
    public QYResultType update(com.fast.sd4324530.fastweixin.company.api.entity.QYDepartment department){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(department, "department is null");
        String url = BASE_API_URL + "cgi-bin/department/update?access_token=#";
        BaseResponse r = executePost(url, department.toJsonString());
        return QYResultType.get(r.getErrcode());
    }

    /**
     * 删除部门
     * @param id 部门ID
     * @return 删除结果
     */
    public QYResultType delete(Integer id){
        com.fast.sd4324530.fastweixin.util.BeanUtil.requireNonNull(id, "id is null");
        String url = BASE_API_URL + "cgi-bin/department/delete?access_token=#&id=" + id;
        BaseResponse r = executeGet(url);
        return QYResultType.get(r.getErrcode());
    }
}
