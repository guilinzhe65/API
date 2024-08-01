package com.yupi.service;


import com.yupi.model.entity.InterfaceInfo;


/**
* @author guilin
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-03-28 14:41:00
*/
public interface InnerInterfaceInfoService  {

    /**
     * 从数据库中查询模拟接口是否匹配
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
