package com.yupi.project.model.dto.notification;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/10 21:04
 */
@Data
public class NotificationUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;


    /**
     * 开始时间
     */
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date startTime;

    /**
     * 结束时间
     */
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
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
