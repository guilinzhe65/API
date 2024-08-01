package com.yupi.project.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/9 17:18
 */
@Data
public class UpdateUserInterfaceInfoDTO implements Serializable {

    private static final long serialVersionUID = 1472097902521779075L;

    private Long userId;

    private Long interfaceId;

    private Long lockNum;


}

