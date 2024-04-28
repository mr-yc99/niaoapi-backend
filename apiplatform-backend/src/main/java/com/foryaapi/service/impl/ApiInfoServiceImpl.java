package com.foryaapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foryaapi.common.ErrorCode;
import com.foryaapi.exception.BusinessException;
import com.foryaapi.mapper.ApiInfoMapper;
import com.foryaapi.model.dto.apiinfo.ApiInfoInvokeRequest;
import com.foryaapicommon.model.entity.ApiInfo;
import com.foryaapi.service.ApiInfoService;
import com.learnjava.apiclientsdk.client.ApiClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
* @author xyc
* @description 针对表【api_info(api信息表)】的数据库操作Service实现
* @createDate 2024-03-10 17:33:49
*/
@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo>
    implements ApiInfoService {

    @Resource
    private ApiClient apiClient;

    @Override
    public void validApiInfo(ApiInfo apiInfo, boolean add) {
        Long id = apiInfo.getId();
        String apiName = apiInfo.getApiName();
        String description = apiInfo.getDescription();
        String url = apiInfo.getUrl();
        String requestHeader = apiInfo.getRequestHeader();
        String responseHeader = apiInfo.getResponseHeader();
        Integer status = apiInfo.getStatus();
        String method = apiInfo.getMethod();
        Long userId = apiInfo.getUserId();
        Date update_time = apiInfo.getUpdate_time();
        Date create_time = apiInfo.getCreate_time();
        Integer is_deleted = apiInfo.getIs_deleted();

        if (apiInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(apiName, url, requestHeader, responseHeader, method) || ObjectUtils.anyNull(id, status, userId)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(apiName) && apiName.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "apiName过长");
        }



    }

    @Override
    public String getInvokeResult(ApiInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request, ApiInfo oldInterfaceInfo) {
        // 接口请求地址
        /*Long id = oldInterfaceInfo.getId();
        String url = oldInterfaceInfo.getUrl();
        String method = oldInterfaceInfo.getMethod();
        // 接口请求路径
        String path = oldInterfaceInfo.getPath();
        String requestParams = interfaceInfoInvokeRequest.getUserRequestParams();

        String result = apiClient.invokeInterface(id, requestParams, url, method, path);
        //*/
        //失败也会返回错误信息
        return null;
    }
}




