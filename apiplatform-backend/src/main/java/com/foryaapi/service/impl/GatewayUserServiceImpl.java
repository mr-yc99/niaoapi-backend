package com.foryaapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foryaapi.common.ErrorCode;
import com.foryaapi.exception.BusinessException;
import com.foryaapi.mapper.UserMapper;
import com.foryaapicommon.model.entity.User;
import com.foryaapicommon.service.GatewayUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class GatewayUserServiceImpl extends ServiceImpl<UserMapper, User> implements GatewayUserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 查询数据库中是否给用户分配ak sk（鉴权）, 返回用户信息，null则不存在
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if(StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
