package com.yupi.project.model.dto.notification;

import com.yupi.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/10 21:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationQueryRequest extends PageRequest implements Serializable {




    /**
     * 公告内容
     */
    private String content;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;



    /**
     * 0: 关闭，1: 启用
     */
    private Integer status;


    /**
     * 域名
     */
    private List<String> domain;



    private static final long serialVersionUID = 1L;
}
