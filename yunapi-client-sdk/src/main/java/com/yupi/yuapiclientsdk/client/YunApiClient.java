package com.yupi.yuapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.yuapiclientsdk.model.User;
import java.util.HashMap;
import java.util.Map;
import static com.yupi.yuapiclientsdk.utils.SignUtils.genSign;

/**
 * 调用第网关的客户端，网关再调用接口
 *
 * @author
 */
public class YunApiClient extends CommonApiClient{

    public YunApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }
    //网关地址
//    private static final String GATEWAY_HOST = "http://localhost:8100";
//
//    private String accessKey;
//
//    private String secretKey;
//
//    public YunApiClient(String accessKey, String secretKey) {
//        this.accessKey = accessKey;
//        this.secretKey = secretKey;
//    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

//    private Map<String, String> getHeaderMap(String body) {
//        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("accessKey", accessKey);
//        // 一定不能直接发送
////        hashMap.put("secretKey", secretKey);
//        hashMap.put("nonce", RandomUtil.randomNumbers(4));
//        hashMap.put("body", body);
//        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        hashMap.put("sign", genSign(body, secretKey));
//        return hashMap;
//    }

    //因为在请求体中有用户名，所以要携带
    public String getUsernameByPost(User user) {
        //把用户信息转换成JSON格式的字符串
        String json = JSONUtil.toJsonStr(user);
        //请求网关地址，把API签名（包含五个参数，(把secretKey变成了sign))添加到请求头中
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json,accessKey,secretKey))//添加请求头
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    //下面是新增接口
    /**
     * 获取每日壁纸
     * @return
     */
    public String getDayWallpaperUrl(){
        return HttpRequest.get(GATEWAY_HOST+"/api/random/wallpaper")
                .addHeaders(getHeaderMap("",accessKey,secretKey))
                .execute().body();
    }

    /**
     * 获取随机文本
     * @return
     */
    public String getRandomWork(){
        return HttpRequest.get(GATEWAY_HOST+"/api/random/word")
                .addHeaders(getHeaderMap("",accessKey,secretKey))
                .execute().body();
    }

    /**
     * 获取随机图片
     * @return
     */
    public String getRandomImageUrl(){
        return HttpRequest.get(GATEWAY_HOST+"/api/random/image")
                .addHeaders(getHeaderMap("",accessKey,secretKey))
                .execute().body();
    }
}
