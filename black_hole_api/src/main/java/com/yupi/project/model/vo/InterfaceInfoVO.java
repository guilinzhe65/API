package com.yupi.project.model.vo;

import com.yupi.model.entity.InterfaceInfo;
import com.yupi.project.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息封装视图
 *
 * @author yupi
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    /**
     * 补充字段，调用总次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}