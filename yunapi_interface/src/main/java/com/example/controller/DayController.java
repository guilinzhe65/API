package com.example.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/9 15:51
 */
@RestController
@RequestMapping("/day")
public class DayController {
    //每日一图
    @PostMapping("/wallpaper")
    public String getDayWallpaperUrl(){
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("format","json");
//
//        HttpResponse response = HttpRequest.post("https://tenapi.cn/v2/bing")
//                .form(paramMap)
//                .execute();
//        String body = response.body();
//        ImageResponse imageResponse = JSONUtil.toBean(body, ImageResponse.class);
//        return imageResponse.getData().getUrl();
        String wallpaperUrl = "https://tenapi.cn/v2/bing";
        String redirectUrl = getRedirectUrl(wallpaperUrl);
        return redirectUrl;
    }

    //获取重定向地址
    @SneakyThrows
    private String getRedirectUrl(String path) {
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        String location = conn.getHeaderField("Location");
        return location;
    }
}
