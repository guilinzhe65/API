package com.yupi.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.model.entity.InterfaceInfo;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.InterfaceInfoMapper;
import com.yupi.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/4/21 19:38
 *
 *
 * 从数据库中查询模拟接口是否匹配，以及请求方法是否匹配
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

        @Resource
        private InterfaceInfoMapper interfaceInfoMapper;

        @Override
        public InterfaceInfo getInterfaceInfo(String url, String method) {
            if (StringUtils.isAnyBlank(url, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("url", url);
            queryWrapper.eq("method", method);
            return interfaceInfoMapper.selectOne(queryWrapper);
        }
}
