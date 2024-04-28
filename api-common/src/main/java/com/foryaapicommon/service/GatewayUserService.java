package com.foryaapicommon.service;

import com.foryaapicommon.model.entity.User;


/**
 * 用户服务
 *
 * @author foryaapi
 */
public interface GatewayUserService {

    /**
     * 查询数据库中是否给用户分配ak sk（鉴权）, 返回用户信息，null则不存在
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
