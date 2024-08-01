package com.yupi.project.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.model.entity.UserInterfaceInfo;


/**
 * @author dio哒
 * @version 1.0
 * @date 2024/4/21 19:39
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}

