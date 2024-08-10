package com.example.entity;

import lombok.Data;

import java.awt.*;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/6 16:45
 */

@Data
public class ImageResponse {

    private String code;
    private String msg;

    private Image data;
}

