package com.foryaapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.foryaapicommon.model.entity.UserApiInfo;

/**
 * 用户接口信息服务
 */
public interface UserApiInfoService extends IService<UserApiInfo> {

    void validUserApiInfo(UserApiInfo userApiInfo, boolean add);

    /**
     * 调用接口统计
     * @param apiInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long apiInfoId, long userId);
}
