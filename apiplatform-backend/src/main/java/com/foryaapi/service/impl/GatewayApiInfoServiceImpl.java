package com.foryaapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foryaapi.common.ErrorCode;
import com.foryaapi.exception.BusinessException;
import com.foryaapi.mapper.ApiInfoMapper;
import com.foryaapicommon.model.entity.ApiInfo;
import com.foryaapicommon.service.GatewayApiInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class GatewayApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements GatewayApiInfoService {

    @Override
    public ApiInfo getApiInfo(String url, String method) {
        if(StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);

        return this.getOne(queryWrapper);
    }
}
