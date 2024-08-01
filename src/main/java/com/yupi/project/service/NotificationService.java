package com.yupi.project.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.project.model.dto.notification.NotificationQueryRequest;
import com.yupi.project.model.entity.Notification;
import com.yupi.project.model.vo.NotificationVO;

import java.util.List;

/**
* @author guilin
* @description 针对表【notification】的数据库操作Service
* @createDate 2024-05-10 20:55:22
*/
public interface NotificationService extends IService<Notification> {


//    /**
//     * 校验
//     *
//     * @param notification
//     * @param domains
//     * @param add
//     */
//    void validNotification(Notification notification,List<String> domains, boolean add);
//
//    NotificationVO getNotificationVO(Notification notification);
    /**
     * 校验
     *
     * @param notification
     * @param add
     */
    void validNotification(Notification notification, boolean add);

    /**
     * 根据ID获取数据封装
     *
     * @param noticeId
     * @return
     */
    NotificationVO getNotificationVOId(long noticeId);

    /**
     * 根据筛选条件获取列表
     *
     * @param notificationQueryRequest
     * @return
     */
    List<NotificationVO> getVOByCond(NotificationQueryRequest notificationQueryRequest);

    /**
     * 分页获取数据封装(SQL处理)
     *
     * @param notificationQueryRequest
     * @return
     */
    Page<NotificationVO> getVOByPage(NotificationQueryRequest notificationQueryRequest);
}
