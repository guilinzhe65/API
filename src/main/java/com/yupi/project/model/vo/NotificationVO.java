package com.yupi.project.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/11 11:26
 */
@Data
public class NotificationVO implements Serializable {
//    /**
//     *  公告id
//     */
//    private long id;
//
//    /**
//     * 公告标题
//     */
//    private String title;
//
//    /**
//     * 公告内容
//     */
//    private String content;
//
//    /**
//     * 更新时间
//     */
//    private Date updateTime;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 所属用户
     */
    private String userId;

    /**
     * 0: 关闭，1: 启用
     */
    private Integer status;

    /**
     * 域名
     */
    private String domain;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
