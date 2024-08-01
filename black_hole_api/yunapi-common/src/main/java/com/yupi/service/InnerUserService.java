package com.yupi.service;



import com.yupi.model.entity.User;


/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {

    /**
     * 校验用户是否有密钥
     */
    User getInvokeUser(String accessKey);

}
