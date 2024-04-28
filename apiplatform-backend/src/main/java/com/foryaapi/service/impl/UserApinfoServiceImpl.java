package com.foryaapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foryaapi.common.ErrorCode;
import com.foryaapi.exception.BusinessException;
import com.foryaapi.mapper.UserApiInfoMapper;
import com.foryaapi.service.UserApiInfoService;
import com.foryaapicommon.model.entity.UserApiInfo;

import org.springframework.stereotype.Service;

/**
 * 用户接口信息服务实现类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Service
public class UserApinfoServiceImpl extends ServiceImpl<UserApiInfoMapper, UserApiInfo>
    implements UserApiInfoService {

    @Override
    public void validUserApiInfo(UserApiInfo userApiInfo, boolean add) {
        if (userApiInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数必须非空
        if (add) {
            if (userApiInfo.getApiId() <= 0 || userApiInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if (userApiInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于 0");
        }
    }

    // [编程学习交流圈](https://www.code-nav.cn/) 快速入门编程不走弯路！30+ 原创学习路线和专栏、500+ 编程学习指南、1000+ 编程精华文章、20T+ 编程资源汇总

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserApiInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);

//        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }

}




