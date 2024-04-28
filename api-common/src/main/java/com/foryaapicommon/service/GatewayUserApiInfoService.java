package com.foryaapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.foryaapicommon.model.entity.UserApiInfo;

/**
* @author xyc
* @description 针对表【user_api_info(用户接口调用次数表)】的数据库操作Service
* @createDate 2024-03-12 17:36:55
*/

public interface GatewayUserApiInfoService{

    void validUserApiInfo(UserApiInfo userApiInfo, boolean add);

    /**
     * 统计调用接口次数
     *
     * @param apiId
     * @param userId
     * @return
     */
    boolean invokeCount(Long apiId, Long userId);

    /**
     * 根据剩余次数判断能否调用
     * @param apiId
     * @param userId
     * @return
     */
    Integer invokeLeftNum(Long apiId, Long userId);
}
