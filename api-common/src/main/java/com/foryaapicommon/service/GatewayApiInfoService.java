package com.foryaapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.foryaapicommon.model.entity.ApiInfo;


/**
* @author xyc
* @description 针对表【api_info(api信息表)】的数据库操作Service
* @createDate 2024-03-10 17:33:49
*/
public interface GatewayApiInfoService{

    /**
     * 查询模拟接口是否存在，返回接口信息，null则不存在
     * @param url
     * @param method
     * @return
     */
    ApiInfo getApiInfo(String url, String method);

}
