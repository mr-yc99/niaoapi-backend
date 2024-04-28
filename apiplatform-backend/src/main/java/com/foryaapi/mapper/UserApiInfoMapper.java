package com.foryaapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foryaapicommon.model.entity.UserApiInfo;

/**
* @author xyc
* @description 针对表【user_api_info(用户接口调用次数表)】的数据库操作Mapper
* @createDate 2024-03-12 17:36:55
* @Entity com.foryaapicommon.model.entity.UserApiInfo
*/


public interface UserApiInfoMapper extends BaseMapper<UserApiInfo> {

    Long selectByApiIdAndUserId(Long apiId, Long userId);
}




