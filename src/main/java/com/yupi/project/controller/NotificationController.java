package com.yupi.project.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.model.entity.User;
import com.yupi.project.annotation.AuthCheck;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.DeleteRequest;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.constant.CommonConstant;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.dto.notification.NotificationAddRequest;
import com.yupi.project.model.dto.notification.NotificationQueryRequest;
import com.yupi.project.model.dto.notification.NotificationUpdateRequest;
import com.yupi.project.model.entity.Notification;
import com.yupi.project.model.vo.NotificationVO;
import com.yupi.project.service.NotificationService;
import com.yupi.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/notification")
@Slf4j
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();
    // region 增删改查

    /**
     * 创建
     *
     * @param notificationAddRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/add")
    public BaseResponse<Long> addNotification(@RequestBody NotificationAddRequest notificationAddRequest, HttpServletRequest request) {
        if (notificationAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationAddRequest, notification);
//        List<String> domains = notificationAddRequest.getDomain();
//        if (domains != null) {
//            notification.setDomain(GSON.toJson(domains));
//        }
//        notificationService.validNotification(notification, domains, true);
//        User loginUser = userService.getLoginUser(request);
//        notification.setUserId(loginUser.getId().toString());
//
//        boolean result = notificationService.save(notification);
//
//        if (!result){
//            throw new BusinessException(ErrorCode.OPERATION_ERROR);
//        }
//        long newNotificationId = notification.getId();
//        return ResultUtils.success(newNotificationId);
        // 设置状态
        notification.setStatus(1);
        notificationService.validNotification(notification, true);

        // 获取当前登陆用户
        Date currentTime = new Date();
        notification.setCreateTime(currentTime);
        notification.setUpdateTime(currentTime);

        // 新增
        boolean result = notificationService.save(notification);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newNotificationId = notification.getId();
        return ResultUtils.success(newNotificationId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNotification(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Notification oldNotification = notificationService.getById(id);
        if (oldNotification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldNotification.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = notificationService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param notificationUpdateRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateNotification(@RequestBody NotificationUpdateRequest notificationUpdateRequest,
                                            HttpServletRequest request) {
        if (notificationUpdateRequest == null || notificationUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationUpdateRequest, notification);
        List<String> domains = notificationUpdateRequest.getDomain();
        if (domains != null) {
            notification.setDomain(GSON.toJson(domains));
        }
        // 参数校验
        notificationService.validNotification(notification,false);
        long id = notificationUpdateRequest.getId();
        // 判断是否存在
        //        Notification oldNotification = notificationService.getById(id);
        long count = notificationService.count(new QueryWrapper<Notification>().eq("id", id));
        if (count == 0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        }
        boolean result = notificationService.updateById(notification);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Notification> getNotificationById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notification = notificationService.getById(id);
        return ResultUtils.success(notification);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param notificationQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<Notification>> listNotification(NotificationQueryRequest notificationQueryRequest) {
        Notification notificationQuery = new Notification();
        if (notificationQueryRequest != null) {
            BeanUtils.copyProperties(notificationQueryRequest, notificationQuery);
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>(notificationQuery);
        List<Notification> notificationList = notificationService.list(queryWrapper);
        return ResultUtils.success(notificationList);
    }

    /**
     * 分页获取列表
     *
     * @param notificationQueryRequest
     * @param
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Notification>> listNotificationByPage(NotificationQueryRequest notificationQueryRequest) {
        if (notificationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notification notificationQuery = new Notification();
        BeanUtils.copyProperties(notificationQueryRequest, notificationQuery);
        long current = notificationQueryRequest.getCurrent();
        long size = notificationQueryRequest.getPageSize();
        String sortField = notificationQueryRequest.getSortField();
        String sortOrder = notificationQueryRequest.getSortOrder();
        String content = notificationQuery.getContent();
        // content 需支持模糊搜索
        notificationQuery.setContent(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>(notificationQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<Notification> notificationPage = notificationService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(notificationPage);
    }

    // endregion7529

    @GetMapping("/get/vo")
    public BaseResponse<NotificationVO> getNotificationVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(notificationService.getNotificationVOId(id));
    }
//    @GetMapping("/get/vo")
//    public BaseResponse<NotificationVO> getNotificationVO(@RequestParam String domain) {
//        // 1. 校验参数
//        if (domain.isEmpty()) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "域名为空");
//        }
//
//        // 2. 查询通知
//        Notification notification = notificationService.getOne(new QueryWrapper<Notification>().eq("title","重要通知"));
//                //like("domain", "\"" + domain + "\""));
//        if (notification == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "暂时没有通知");
//        }
//
//        // 3. 校验通知是否开启状态
//        Integer status = notification.getStatus();
//        // 未开启状态
//        if (status == 0) {
//            return ResultUtils.success(null);
//        }
//
//        // 4. 校验是否在开始时间到结束时间内
//        Date startTime = notification.getStartTime();
//        Date endTime = notification.getEndTime();
//        if (startTime == null || endTime == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间或结束时间为空");
//        }
//        // 当前时间
//        Date date = DateUtil.date();
//        // 判断当前时间是否在开始时间到结束时间内
//        if (date.before(startTime) || date.after(endTime)) {
//            return ResultUtils.success(null);
//        }
//        log.info("getNotificationVO: {}", notification);
//        NotificationVO notificationVO = notificationService.getNotificationVOId(notification);
//        return ResultUtils.success(notificationVO);
}

