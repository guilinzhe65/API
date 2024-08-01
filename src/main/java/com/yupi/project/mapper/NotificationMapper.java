package com.yupi.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.project.model.dto.notification.NotificationQueryRequest;
import com.yupi.project.model.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.project.model.vo.NotificationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author guilin
* @description 针对表【notification】的数据库操作Mapper
* @createDate 2024-05-10 20:55:22
* @Entity com.yupi.project.model.entity.Notification
*/
public interface NotificationMapper extends BaseMapper<Notification> {
    // 根据ID查找数据
    NotificationVO getVOById(@Param("noticeId") long noticeId);


    // 根据筛选条件查找数据
    List<NotificationVO> getVOByCond(@Param("params") NotificationQueryRequest dataInfoQueryRequest);

    // 分页查找数据
    Page<NotificationVO> getVOByPage(@Param("params") NotificationQueryRequest dataInfoQueryRequest, Page<Object> page);
}




