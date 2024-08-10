package com.example.controller;

import java.net.HttpURLConnection;
import cn.hutool.http.HttpConnection;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.ImageResponse;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/6 16:41
 */
@RestController
@RequestMapping("/random")
public class RandomController {

    //随机诗文
    @GetMapping("/wallpaper")
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
       // String wallpaperUrl = "https://bing.img.run/uhd.php";
        //String redirectUrl = getRedirectUrl(wallpaperUrl);
        //return wallpaperUrl;
        String text = HttpUtil.get("https://v1.jinrishici.com/all.svg");
        //Map<?, ?> map = JSON.parseObject(text, Map.class);
        String regex = "<text[^>]*>([^<]+)</text>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String extractedText = matcher.group(1);
            return extractedText;
        } else {
            return "Text not found";
        }
    }

    //获取重定向地址
//    @SneakyThrows
//    private String getRedirectUrl(String path) {
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setInstanceFollowRedirects(false);
//        conn.setConnectTimeout(5000);
//        String location = conn.getHeaderField("Location");
//        return location;
//    }

    //随机短文
    @GetMapping("/word")
    public String getRandomWork(){
//        HttpResponse response = HttpRequest.get("https://tenapi.cn/v2/yiyan")
//                .execute();
//        return response.body();
        String text = HttpUtil.get("https://api.shadiao.pro/chp");
        Map<?, ?> map = JSON.parseObject(text, Map.class);
        //StringBuffer stringBuffer = new StringBuffer();
        String newText = (String) ((Map<?, ?>) map.get("data")).get("text");
        //stringBuffer.append("{\"text\": \"");
        //stringBuffer.append(text);
        //stringBuffer.append("\"}");
        return newText;
    }

    //随机图片
    @GetMapping("/image")
    public String getRandomImageUrl(){
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("format","json");
//        HttpResponse response = HttpRequest.post("https://t.mwm.moe/fj")
//                .form(paramMap)
//                .execute();
//        String body = response.body();
//        ImageResponse imageResponse = JSONUtil.toBean(body, ImageResponse.class);
//        return imageResponse.getData().getUrl();
        //String wallpaperUrl = "https://api.btstu.cn/sjbz/api.php";
        String text = HttpUtil.get("https://api.btstu.cn/sjbz/api.php?format=json");
        //String redirectUrl = getRedirectUrl(wallpaperUrl);
        return text;
    }


}

