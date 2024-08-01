package com.yupi.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yupi.project.common.ErrorCode;
import com.yupi.project.constant.CommonConstant;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.dto.notification.NotificationQueryRequest;
import com.yupi.project.model.entity.Notification;
import com.yupi.project.model.vo.NotificationVO;
import com.yupi.project.service.NotificationService;
import com.yupi.project.mapper.NotificationMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @author guilin
* @description 针对表【notification】的数据库操作Service实现
* @createDate 2024-05-10 20:55:22
*/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService{

//    @Override
//    public void validNotification(Notification notification,List<String> domains, boolean add) {
//        if (notification == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        String title = notification.getTitle();
//        String content = notification.getContent();
//        Date startTime = notification.getStartTime();
//        Date endTime = notification.getEndTime();
//
//        if (ObjectUtil.isNull(startTime) || ObjectUtil.isNull(endTime)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间或结束时间为空");
//        }
//
//        // 创建时，参数不能为空
//        if (add) {
//            if (StringUtils.isAnyBlank(content, title) ) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR);
//            }
//        }
//
//        // 有参数则校验
//        if (StringUtils.isNotBlank(title) && title.length() > 80) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
//        }
//        if (StringUtils.isNotBlank(content) && content.length() > 2048) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
//        }
//
//        //新添加的
//        // 校验域名是否已经存在公告
//        if (!ObjectUtils.isEmpty(domains)) {
//            LambdaQueryWrapper<Notification> queryWrapper = Wrappers.lambdaQuery(Notification.class);
//            // 如果不是添加接口：排除当前的公告
//            if (!add) {
//                queryWrapper.ne(Notification::getId, notification.getId());
//            }
//
//            queryWrapper.and(wrapper -> domains.forEach(domain -> wrapper.or().like(Notification::getDomain, "\"" + domain + "\"")));
//            long count = this.count(queryWrapper);
//
//            if (count > 0 ) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "该域名已存在对应的公告");
//            }
//        }
//    }
//
//    @Override
//    public NotificationVO getNotificationVO(Notification notification) {
//        return null;
//    }
//
//    public QueryWrapper<Notification> getQueryWrapper(NotificationQueryRequest notificationQueryRequest) {
//        if (notificationQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
//
//        }
//        // 获取请求中的数据
//        Long id = notificationQueryRequest.getId();
//        String title = notificationQueryRequest.getTitle();
//        String content = notificationQueryRequest.getContent();
//        Date startTime = notificationQueryRequest.getStartTime();
//        Date endTime = notificationQueryRequest.getEndTime();
//        Integer status = notificationQueryRequest.getStatus();
//        List<String> domains = notificationQueryRequest.getDomain();
//        String sortField = notificationQueryRequest.getSortField();
//        String sortOrder = notificationQueryRequest.getSortOrder();
//
//        // 拼接查询条件
//        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(id != null, "id", id);
//        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
//        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
//        queryWrapper.ge(startTime != null, "startTime", startTime);
//        queryWrapper.le(endTime != null, "endTime", endTime);
//        queryWrapper.eq(status != null, "status", status);
//        queryWrapper.eq("isDelete", false);
//        if (CollectionUtils.isNotEmpty(domains)) {
//            for (String domain : domains) {
//                queryWrapper.like("domain", domain);
//            }
//        }
////        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
////                sortField);
//        return queryWrapper;
//    }

    @Resource
    private NotificationMapper notificationMapper;

    @Override
    public void validNotification(Notification notification, boolean add) {
        if (notification == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String content = notification.getContent();
        String domain = notification.getDomain();
        // 创建时，参数不能为空
        if (add) {
            if (StringUtils.isAnyBlank(content,domain) ) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(content) && content.length() > 500) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    @Override
    public NotificationVO getNotificationVOId(long noticeId) {
        return notificationMapper.getVOById(noticeId);
    }

    @Override
    public List<NotificationVO> getVOByCond(NotificationQueryRequest notificationQueryRequest) {
        List<NotificationVO> notificationVOList = notificationMapper.getVOByCond(notificationQueryRequest);
        return notificationVOList ;
    }

    @Override
    public Page<NotificationVO> getVOByPage(NotificationQueryRequest notificationQueryRequest) {
        long current = notificationQueryRequest.getCurrent();
        long size = notificationQueryRequest.getPageSize();
        Page<Object> page = new Page<>(current, size);
        Page<NotificationVO> notificationVOPage = notificationMapper.getVOByPage(notificationQueryRequest,page);
        return notificationVOPage;
    }
}




